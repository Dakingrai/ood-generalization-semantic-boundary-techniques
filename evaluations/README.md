# Evaluate the checkpoints with test sets

## Introduction
The code for evaluation is adapted from [Spider](https://github.com/taoyds/spider) official evaluation script, while the code for converting NatSQL to SQL is from [NatSQL](https://github.com/ygan/NatSQL) codebase. There are in total five evaluation sets from [Spider-CG](https://arxiv.org/abs/2205.02054) - `Spider-dev`, `CG-APP(T)`, `CG-APP(D)`, `CG-SUB(T)`, and `CG-SUB(D)`. More details about the dataset on the paper. 

## Setup
### Step 1: Environment setup
Follow all instructions on the [project homepage](https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git) to set up the Python virtual environment and install all dependencies.

### Step 2: Download data and necessary files
1. Download the [datasets](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz) and copy all the test sets from the `./data/` of the downloaded folder to the `./data/` of the project directory.
2. Download the [Spider dataset](https://yale-lily.github.io/spider) and copy the `database` folder and `tables.json` to the `./data/` of the project directory.

### Step 3: Run the inference
Run the inference code using the script below to get the prediction of the test set using the selected checkpoint.  
Note: Please use --token_preprocessing only if your model is trained with our token preprocessing input.
```
python evaluation/inference.py --checkpoint checkpoint_path --data test_data_path --database database_path --token_preprocessing 
```

### Step 4: Run the evaluation
Evaluate the predicted SQL to get the exact match accuracy (EX) and execution accuracy (EX). This code is adapted from [Spider](https://github.com/taoyds/spider) official evaluation script.
```
python evaluation/evaluation.py --gold gold_test_file_path --pred pred.sql --etype all --db database_path --table data/tables.json
```
