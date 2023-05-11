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

def spider_get_input(question: str, serialized_schema: str, prefix: str)->str:
	return prefix + question.strip() + " " + utils.preprocess(serialized_schema).strip()

def spider_get_target(query: str, db_id: str, normalize_query = True, target_with_db_id=False) -> str:
	# try:
	# 	assert normalize(query).lower() == rejoin_refine_single(refine(normalize(query), replace=True), replace=True)
	# except:
	# 	print(normalize(query).lower(), rejoin_refine_single(refine(normalize(query), replace=True), replace=True))
	query = copy.deepcopy(utils.preprocess(normalize(query), replace=True).strip())
	return query

# ------------------------------------------------
# ------------------------------------------------
def difference(str1, str2):
	result1 = ''
	result2 = ''
	maxlen=len(str2) if len(str1)<len(str2) else len(str1)
	#loop through the characters
	for i in range(maxlen):
		#use a slice rather than index in case one string longer than other
		letter1=str1[i:i+1]
		letter2=str2[i:i+1]
		#create string with differences
		if letter1 != letter2:
			result1+=letter1
			result2+=letter2
	return result1

def camel_case_preprocess(s):
    _underscorer1 = re.compile(r'(.)([A-Z][a-z]+)')
    _underscorer2 = re.compile('([a-z0-9])([A-Z])')
    subbed = _underscorer1.sub(r'\1\2', s)
    return _underscorer2.sub(r'\1# \2', subbed).lower()

def camel_case_postprocess(data):
    data_split = data.split()
    processed = ""
    remove = False
    for line in data_split:
        if '#' in line:
            processed += " "+ line.replace('#','')
            remove = True
        else:
            if remove:
                processed += line
                remove = False
            else:
                processed += " "+ line
    return processed

# def refine(raw_data, replace = False):
#     data = copy.deepcopy(raw_data)
#     if replace == True:
#         data = data.replace("asc (", "ascend (")
#         data = data.replace("desc (", "descending (")
#         data = data.replace("asc(", "ascend (")
#         data = data.replace("desc(", "descending (")
#         data = data.replace("avg(", "average (")
#         data = data.replace("avg (", "average (")
#     data_split = data.split()
#     refined_word = " "
#     for word in data_split:
#         # if "struct_sep" in word:
#         #    continue
#         if "_" not in word and "." not in word:
#             refined_word += " " + word
#         else:   
#             if "_" in word:
#                 w = word.split("_")
#                 rw1 = " " + " _ ".join(w)
#                 if len(rw1.split(".")) == 1:
#                     refined_word += rw1
#                 else:
#                     temp_refined_word = rw1.split(".")
#                     refined_word +=" "+" . ".join(temp_refined_word)
#             if "." in word and "_" not in word:
#                 w = word.split(".")
#                 refined_word +=" "+" . ".join(w)
#     return refined_word.strip()

# def rejoin_refine_single(raw_data, replace=True):
# 	keywords = ('except_', 'intersect_', 'union_')
# 	# camel_data = camel_case_postprocess(raw_data)
# 	camel_data = raw_data
# 	if replace == True:
# 		camel_data = camel_data.replace("ascend (", "asc (")
# 		camel_data = camel_data.replace("ascend(", "asc (")
# 		camel_data = camel_data.replace("descending (", "desc (")
# 		camel_data = camel_data.replace("descending(", "desc (")
# 		camel_data = camel_data.replace("average(", "avg (")
# 		camel_data = camel_data.replace("average (", "avg (")
# 	data_split = camel_data.split()
# 	refined_data = ""
# 	remove = False
# 	for i, word in enumerate(data_split):
# 		if "_" not in word and "." not in word:
# 			if remove:
# 				refined_data += word
# 				remove = False
# 			else:
# 				refined_data += " " + word
# 		else:
# 			refined_data += word
# 			if data_split[i-1] + word not in keywords:
# 				remove = True
# 	return refined_data.strip()

# def rejoin_refine(data):
#     refined_data = []
#     for d in data:
#         refined_data.append(rejoin_refine_single(utils.refine(d)))
#     return refined_data

# def refine_metas(data):
# 	r_data = []
# 	for d in data:
# 		d['query'] = d['query']
# 		d['context'] = d['context']
# 		d['label'] = rejoin_refine_single(d['label'])
# 		r_data.append(d)
# 	return r_data

# ---------------------------------------------

def spider_add_serialized_schema(ex: dict, data_training_args: DataTrainingArguments)->dict:
	serialized_schema = serialize_schema(question = ex["question"], db_path=ex["db_path"], db_id = ex["db_id"], db_column_names = ex["db_column_names"], db_table_names = ex["db_table_names"], schema_serialization_type=data_training_args.schema_serialization_type, schema_serialization_randomized=data_training_args.schema_serialization_randomized, schema_serialization_with_db_id=data_training_args.schema_serialization_with_db_id, schema_serialization_with_db_content=data_training_args.schema_serialization_with_db_id, normalize_query=data_training_args.normalize_query)
	return {"serialized_schema": serialized_schema}

def spider_pre_process_function(batch: dict, max_source_length: Optional[int], max_target_length: Optional[int], data_training_args: DataTrainingArguments, tokenizer: PreTrainedTokenizerBase)->dict:
	prefix = data_training_args.source_prefix if data_training_args.source_prefix is not None else ""
	inputs = [
	spider_get_input(question, serialized_schema, prefix) for question, serialized_schema in zip(batch["question"], batch["serialized_schema"])
	]
	model_inputs: dict = tokenizer(inputs, max_length=max_source_length, padding = False, truncation = True, return_overflowing_tokens = False)
	targets = [spider_get_target(query, db_id, data_training_args.normalize_query, data_training_args.target_with_db_id) for db_id, query in zip(batch["db_id"], batch["query"])]
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
		predictions = copy.deepcopy(utils.post_process_all(raw_pred))
		# predictions = self.remove_special_token(predictions)
		if self.target_with_db_id:
			predictions = [pred.split("|", 1)[-1].strip() for pred in predictions]
		references = metas
		# pred = self.insert_from_natsql(predictions)
		final_pred = self.convert_to_sql(predictions)
		return self.metric.compute(predictions = final_pred, references = references)
	
	# def construct_hyper_param(self):
	# 	parser = argparse.ArgumentParser()
	# 	args = parser.parse_args()
	# 	return args

	def convert_to_sql(self, data):
		# args = self.construct_hyper_param()
		natsql2sql_args = Args()
		natsql2sql_args.not_infer_group = True #verified
		tables = json.load(open("seq2seq/utils/NatSQLv1_6/tables_for_natsql.json"))
		table_dict = {}
		for t in tables:
			table_dict[t["db_id"]] = t
		
		sqls = json.load(open("seq2seq/utils/NatSQLv1_6/dev.json"))

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


	# def insert_from_natsql(self, data):
	# 	for i, row in enumerate(data):
	# 		data[i] = self.insert_from_natsql_single(row)
	# 	return data


	# def insert_from_natsql_single(self, raw_data):
	# 	data_l = raw_data.split()
	# 	# pdb.set_trace()
	# 	index = -1

	# 	for i,d in enumerate(data_l):
	# 		if data_l[i].lower() == 'order' or data_l[i].lower()  == 'where' or data_l[i].lower()  == 'group':
	# 			index = i
	# 			break

	# 	data_k = raw_data.split('.')
	# 	if data_k[0].strip() != '':
	# 		repl_table_name = data_k[0].split()[-1]
	# 	else:
	# 		repl_table_name = ''

	# 	if ')' in repl_table_name:
	# 		repl_table_name = repl_table_name.split(')')[-2]
						
	# 	if '(' in repl_table_name:
	# 		repl_table_name = repl_table_name.split('(')[-1]

	# 	if index !=-1:
	# 		data_l.insert(index, 'from ' + repl_table_name)
	# 		raw_data = ' '.join(data_l)

	# 	else:
	# 		repl = 'from '+ repl_table_name
	# 		data_l.append(repl.strip())
	# 		raw_data = ' '.join(data_l)

	# 	return raw_data
	
	# def remove_special_token(self, natsql):
	# 	clean_natsql = []
	# 	for sql in natsql:
	# 		sql = sql.replace('[struct_sep0]', '')
	# 		sql = sql.replace('[struct_sep 0]', '')
	# 		sql = sql.replace('[struct_sep0 ]', '')
	# 		sql = sql.replace('[struct_sep1]', '')
	# 		sql = sql.replace('[struct_sep1 ]', '')
	# 		sql = sql.replace('[struct_sep 1]', '')
	# 		sql = sql.replace('[struct_sep2]', '')
	# 		sql = sql.replace('[struct_sep 2]', '')
	# 		sql = sql.replace('[struct_sep2 ]', '')
	# 		sql = sql.replace('[struct_sep3]', '')
	# 		sql = sql.replace('[struct_sep3 ]', '')
	# 		sql = sql.replace('[struct_sep 3]', '')
	# 		sql = sql.replace('[struct_sep4]', '')
	# 		sql = sql.replace('[struct_sep4 ]', '')
	# 		sql = sql.replace('[struct_sep 4]', '')
	# 		sql = sql.replace('[struct_sep5 ]', '')
	# 		sql = sql.replace('[struct_sep5]', '')
	# 		sql = sql.replace('[struct_sep 5]', '')

	# 		sql = sql.replace('[/struct_sep0]', '')
	# 		sql = sql.replace('[/struct_sep0 ]', '')
	# 		sql = sql.replace('[/struct_sep 0]', '')
	# 		sql = sql.replace('[/struct_sep1]', '')
	# 		sql = sql.replace('[/struct_sep1 ]', '')
	# 		sql = sql.replace('[/struct_sep 1]', '')
	# 		sql = sql.replace('[/struct_sep2]', '')
	# 		sql = sql.replace('[/struct_sep2 ]', '')
	# 		sql = sql.replace('[/struct_sep 2]', '')
	# 		sql = sql.replace('[/struct_sep3]', '')
	# 		sql = sql.replace('[/struct_sep3 ]', '')
	# 		sql = sql.replace('[/struct_sep 3]', '')
	# 		sql = sql.replace('[/struct_sep4]', '')
	# 		sql = sql.replace('[/struct_sep4 ]', '')
	# 		sql = sql.replace('[/struct_sep 4]', '')
	# 		sql = sql.replace('[/struct_sep5 ]', '')
	# 		sql = sql.replace('[/struct_sep5]', '')
	# 		sql = sql.replace('[/struct_sep 5]', '')
	# 		sql = sql.replace('!=', ' !=')
	# 		sql = re.sub(' +', ' ', sql).strip()
	# 		clean_natsql.append(sql)
	# 	return clean_natsql


