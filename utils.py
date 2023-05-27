import copy

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