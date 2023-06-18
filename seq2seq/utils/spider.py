import json
import numpy as np
from typing import Optional
from datasets.arrow_dataset import Dataset
from transformers.tokenization_utils_base import PreTrainedTokenizerBase
from .dataset import DataTrainingArguments, normalize, serialize_schema
from .trainer import Seq2SeqTrainer, EvalPrediction
import pdb, argparse
from .natsql2sql.natsql2sql import Args
import copy
import re
import utils

from .natsql2sql.preprocess.sq import SubQuestion
from .natsql2sql.natsql_parser import create_sql_from_natSQL
from token_preprocessing import TokenPreprocessor

replace_tokens = [(' avg (', ' average ('), (' asc ', ' ascending '), (' desc ', ' descending ')]
token_preprocessor = TokenPreprocessor(snake_case=True, camel_case=True, normalize_text=True, replace_tokens=replace_tokens, insert_space_when=['.'])

def spider_get_input(question: str, serialized_schema: str, prefix: str, tokenize=False)->str:
	if tokenize:
		return prefix + question.strip() + " " + token_preprocessor.preprocess(serialized_schema).strip()
	return prefix + question.strip() + " " + serialized_schema.strip()

def spider_get_target(query: str, db_id: str, normalize_query = True, target_with_db_id=False, tokenize=False) -> str:
	if tokenize:
		return copy.deepcopy(token_preprocessor.preprocess(normalize(query)).strip())
	return normalize(query).strip()

def spider_add_serialized_schema(ex: dict, data_training_args: DataTrainingArguments)->dict:
	serialized_schema = serialize_schema(question = ex["question"], db_path=ex["db_path"], db_id = ex["db_id"], db_column_names = ex["db_column_names"], db_table_names = ex["db_table_names"], schema_serialization_type=data_training_args.schema_serialization_type, schema_serialization_randomized=data_training_args.schema_serialization_randomized, schema_serialization_with_db_id=data_training_args.schema_serialization_with_db_id, schema_serialization_with_db_content=data_training_args.schema_serialization_with_db_id, normalize_query=data_training_args.normalize_query)
	return {"serialized_schema": serialized_schema}

def spider_pre_process_function(batch: dict, max_source_length: Optional[int], max_target_length: Optional[int], data_training_args: DataTrainingArguments, tokenizer: PreTrainedTokenizerBase)->dict:
	prefix = data_training_args.source_prefix if data_training_args.source_prefix is not None else ""
	inputs = [
	spider_get_input(question, serialized_schema, prefix, data_training_args.token_preprocessing) for question, serialized_schema in zip(batch["question"], batch["serialized_schema"])
	]
	model_inputs: dict = tokenizer(inputs, max_length=max_source_length, padding = False, truncation = True, return_overflowing_tokens = False)
	targets = [spider_get_target(query, db_id, data_training_args.normalize_query, data_training_args.target_with_db_id, data_training_args.token_preprocessing) for db_id, query in zip(batch["db_id"], batch["query"])]
	with tokenizer.as_target_tokenizer():
		labels = tokenizer(targets, max_length=max_target_length, padding=False, truncation=True, return_overflowing_tokens = False)
	model_inputs["labels"] = labels["input_ids"]
	return model_inputs

class SpiderTrainer(Seq2SeqTrainer):
	def _post_process_function(self, examples: Dataset, features: Dataset, predictions: np.ndarray, stage: str)->EvalPrediction:
		# pdb.set_trace()
		inputs = self.tokenizer.batch_decode([f["input_ids"] for f in features], skip_special_tokens = True)
		label_ids = [f["labels"] for f in features]
		if self.ignore_pad_token_for_loss:
			_label_ids = np.where(label_ids!=-100, label_ids, self.tokenizer.pad_token_id)
		decoded_label_ids = self.tokenizer.batch_decode(_label_ids, skip_special_tokens=True)
		metas = [
		{
			"query": x["query"],
			"question": x["question"],
			"context": context,
			"label": label,
			"db_id": x["db_id"],
			"db_path": x["db_path"],
			"db_table_names": x["db_table_names"],
			"db_column_names": x["db_column_names"],
			"db_foreign_keys": x["db_foreign_keys"],
		}
		for x, context, label in zip(examples, inputs, decoded_label_ids)
		]
		predictions = self.tokenizer.batch_decode(predictions, skip_special_tokens=True)
		assert len(metas) == len(predictions)
		with open(f"{self.args.output_dir}/predictions_{stage}.json", "w") as f:
			json.dump(
				[dict(**{"prediction": prediction}, **meta) for prediction, meta in zip(predictions, metas)],
				f,
				indent=4,
			)
		return EvalPrediction(predictions=predictions, label_ids=label_ids, metas=metas)

	def _compute_metrics(self, eval_prediction: EvalPrediction)->dict:
		raw_pred, label_ids, metas = eval_prediction
		predictions = copy.deepcopy(token_preprocessor.postprocess_all(raw_pred))
		if self.target_with_db_id:
			predictions = [pred.split("|", 1)[-1].strip() for pred in predictions]
		references = metas
		pred = utils.insert_from_natsql_all(predictions)
		final_pred = self.convert_to_sql(pred)
		return self.metric.compute(predictions = final_pred, references = references)

	def convert_to_sql(self, data):
		# args = self.construct_hyper_param()
		natsql2sql_args = Args()
		natsql2sql_args.not_infer_group = True #verified
		tables = json.load(open("data/natsql/tables_for_natsql.json"))
		table_dict = {}
		for t in tables:
			table_dict[t["db_id"]] = t
		
		sqls = json.load(open("data/natsql/dev.json"))

		#todo: replace natsql in sqls

		for i, sql in enumerate(sqls):
			sqls[i]['NatSQL'] = data[i]

		created_sqls = []
		for i,sql in enumerate(sqls):
			if "pattern_tok" in sql:
				sq = SubQuestion(sql["question"],sql["question_type"],sql["table_match"],sql["question_tag"],sql["question_dep"],sql["question_entt"], sql, run_special_replace=False)
			else:
				sq = None
			try:
				query,_,__ = create_sql_from_natSQL(sql["NatSQL"], sql['db_id'], "data/spider/database/"+sql['db_id']+"/"+sql['db_id']+".sqlite", table_dict[sql['db_id']], sq, remove_values=False, remove_groupby_from_natsql=False, args=natsql2sql_args)
				created_sqls.append(query.strip())
			except:
				created_sqls.append("None")
		return created_sqls



