import json
import pdb
import torch
import tqdm
from transformers.models.auto import AutoConfig, AutoTokenizer, AutoModelForSeq2SeqLM
import argparse
import copy
import random
from src.token_preprocessing import TokenPreprocessor
from src.dataset import serialize_schema
from src.get_tables import dump_db_json_schema
from src.files_to_convert_natsql2sql.natsql2sql.preprocess.sq import SubQuestion
from src.files_to_convert_natsql2sql.natsql2sql.natsql_parser import create_sql_from_natSQL
from src.files_to_convert_natsql2sql.natsql2sql.natsql2sql import Args

replace_tokens = [(' avg (', ' average ('), (' asc ', ' ascending '), (' desc ', ' descending ')]
token_preprocessor = TokenPreprocessor(snake_case=True, camel_case=True, normalize_text=True, replace_tokens=replace_tokens, insert_space_when=['.'])

def save_file(data, file_path):
    with open(file_path, 'w') as f:
        for each in data:
            f.write(each + "\n")

def load_data(file_path):
    with open(file_path, "r") as fin:
        raw_data = json.load(fin)
    return raw_data

def test_data_preprocess(data):
    queries = []
    schema = []
    for each in data:
        queries.append({'original': each['question'], 'preprocessed':token_preprocessor.preprocess(each['question'])})
        schema.append({'original': each['schema'], 'preprocessed':token_preprocessor.preprocess(each['schema'])})
    save_file(queries, "output/test_queries.json")
    save_file(schema, "output/test_schema.json")

def insert_from_natsql(data):
	for i, row in enumerate(data):
		data[i] = insert_from_natsql_single(row)
	return data


def insert_from_natsql_single(raw_data):
	data_l = raw_data.split()
	# pdb.set_trace()
	index = -1
	for i,d in enumerate(data_l):
		if data_l[i].lower() == 'order' or data_l[i].lower()  == 'where' or data_l[i].lower()  == 'group':
			index = i
			break
	data_k = raw_data.split('.')
	if data_k[0].strip() != '':
		repl_table_name = data_k[0].split()[-1]
	else:
		repl_table_name = ''
	if ')' in repl_table_name:
		repl_table_name = repl_table_name.split(')')[-2]
					
	if '(' in repl_table_name:
		repl_table_name = repl_table_name.split('(')[-1]
	if index !=-1:
		data_l.insert(index, 'from ' + repl_table_name)
		raw_data = ' '.join(data_l)
	else:
		repl = 'from '+ repl_table_name
		data_l.append(repl.strip())
		raw_data = ' '.join(data_l)
	return raw_data

def convert_to_sql(pred, data_path):
		# args = self.construct_hyper_param()
		natsql2sql_args = Args()
		natsql2sql_args.not_infer_group = True #verified
		tables = json.load(open("data/tables_for_natsql.json"))
		table_dict = {}
		for t in tables:
			table_dict[t["db_id"]] = t
		
		sqls = json.load(open(data_path))

		for i, sql in enumerate(sqls):
			sqls[i]['NatSQL'] = pred[i]

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

def get_input(test):
    input, output = [], []
    for i in range(len(test)):
        db_id = test[i]["db_id"]
        schema = dump_db_json_schema(
                            db_path + "/" + db_id + "/" + db_id + ".sqlite", db_id
                        )
        table_ids = [table_id for table_id, column_name in schema["column_names_original"]]
        column_names = [column_name for table_id, column_name in schema["column_names_original"]]
        test[i]['db_path'] = db_path
        test[i]['db_table_names'] = schema["table_names_original"]
        test[i]['db_column_names'] = {"table_id": table_ids, "column_name": column_names},
        test[i]['db_column_types'] = schema["column_types"]
        test[i]['db_primary_keys'] = [{"column_id": column_id} for column_id in schema["primary_keys"]]
        test[i]['db_foreign_keys'] = [
                            {"column_id": column_id, "other_column_id": other_column_id}
                            for column_id, other_column_id in schema["foreign_keys"]
                        ]
        schema = serialize_schema(question = test[i]["question"], db_path=test[i]['db_path'], db_id = test[i]["db_id"], db_column_names = test[i]["db_column_names"][0], db_table_names = test[i]["db_table_names"], schema_serialization_type='peteshaw', schema_serialization_randomized=False, schema_serialization_with_db_id=True, schema_serialization_with_db_content=True, normalize_query=True)
        input.append(test[i]['question'].strip() + " " + token_preprocessor.preprocess(schema).strip())
        output.append(token_preprocessor.preprocess(schema))
    return input, output
	

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--model", help="model name")
    parser.add_argument("--data", help="data path")
    parser.add_argument("--database", help="database path")
    args = parser.parse_args()
    db_path = args.database
    test = load_data(args.data)
    processed_test = []
    input, output = get_input(test)
    device = "cuda" if torch.cuda.is_available() else "cpu"
    tokenizer = AutoTokenizer.from_pretrained(args.model)
    model = AutoModelForSeq2SeqLM.from_pretrained(args.model).to(device)
    model.eval()
    # batchsize = 2
    test_pred = []
    # for i in tqdm.tqdm(range(len(test)//batchsize+1 if len(test)%batchsize > 0 else 0)):
    #     t_input = tokenizer(input[i*batchsize:(i+1)*batchsize], return_tensors="pt", padding=True, truncation=True)
    for i in tqdm.tqdm(range(len(test))):
        torch.cuda.mem_get_info()
        t_input = tokenizer(input[i], return_tensors="pt", padding=True, truncation=True)
        outputs = model.generate(input_ids = t_input.input_ids.to(device),
                                attention_mask = t_input.attention_mask.to(device),
                                early_stopping=True)
        pred = tokenizer.batch_decode(outputs, skip_special_tokens=True)
        test_pred += pred
        torch.cuda.mem_get_info()
    preds = copy.deepcopy(token_preprocessor.postprocess_all(test_pred))
    preds = insert_from_natsql(preds)
    save_file(preds, "raw_pred.sql")
    sqls = convert_to_sql(preds, args.data)
    save_file(sqls, "pred.sql")
    
