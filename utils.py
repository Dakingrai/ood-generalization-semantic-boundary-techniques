import copy
import json

def preprocess(raw_data, replace = False):
    data = copy.deepcopy(raw_data)
    data = remove_from_clause(data) # removes from clause
    if replace == True: # replacing some keywords
        data = data.replace(" asc ", " ascend ")
        data = data.replace(" desc ", "descending ")
        data = data.replace("avg(", "average (")
        data = data.replace("avg (", "average (")
    data_split = data.split()
    refined_word = " "
    for word in data_split: # Inserting spaces betwee "_" and "."
        if "_" not in word and "." not in word:
            refined_word += " " + word
        else:   
            if "_" in word:
                w = word.split("_")
                rw1 = " " + " _ ".join(w)
                if len(rw1.split(".")) == 1:
                    refined_word += rw1
                else:
                    temp_refined_word = rw1.split(".")
                    refined_word +=" "+" . ".join(temp_refined_word)
            if "." in word and "_" not in word:
                w = word.split(".")
                refined_word +=" "+" . ".join(w)
    return refined_word.strip()

def preprocess_all(data):
    refined_data = []
    for d in data:
        refined_data.append(preprocess(d))
    return refined_data

def post_process(raw_data, replace=True):
    """Post process the predicted query to make it executable"""
    raw_data = preprocess(raw_data)
    keywords = ('except_', 'intersect_', 'union_')
    camel_data = raw_data
    if replace == True:
        camel_data = camel_data.replace(" ascend ", " asc ")
        camel_data = camel_data.replace(" descending ", " desc ")
        camel_data = camel_data.replace("average(", "avg (")
        camel_data = camel_data.replace("average (", "avg (")
    data_split = camel_data.split()
    refined_data = ""
    remove = False
    for i, word in enumerate(data_split):
        if "_" not in word and "." not in word:
            if remove:
                refined_data += word
                remove = False
            else:
                refined_data += " " + word
        else:
            refined_data += word
            if data_split[i-1] + word not in keywords:
                remove = True
    return refined_data.strip()

def post_process_all(data):
    refined_data = []
    for d in data:
        refined_data.append(post_process(d))
    refined_data = insert_from_natsql_all(refined_data)
    return refined_data

def remove_from_clause(data):
    index = -1
    data_l = data.split()
    for i,d in enumerate(data_l):
        if d == 'from':
            index = i
            break
    if index != -1:
        del data_l[index:index+2]
        data = ' '.join(data_l)
    return data

def insert_from_natsql(raw_data):
        """Insert FROM clause as they are removed during preprocessing"""
        data_l = raw_data.split()
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

def insert_from_natsql_all(data):
    refined_data = []
    for d in data:
        refined_data.append(insert_from_natsql(d))
    return refined_data