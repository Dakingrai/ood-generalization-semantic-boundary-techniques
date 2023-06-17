# Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques
This repository contains the code for ACL 2023 paper: [Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques](https://arxiv.org/abs/2305.17378).

## Overview
In this paper, we introduce two semantic-boundary-based techniques to improve compositional and domain generalization in language model based semantic parsers - Token preprocessing (Tok) and Component boundary marking (Comp). Token preprocessing consists of preprocessing steps that address the tokenization issues that arise due to SQL naming conventions (Snakecase and Camelcase), dot notations, and SQL keywords. On the other hand, Component boundary marking proposes to align the input and output components by augmenting them with special token pairs as shown in Table 1. 

![github](https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques/assets/3531451/0fb9eff6-19a7-49c0-8f46-a48130545dfd)

## Environment Setup
```
pip install torch==1.12.1+cu116 torchvision==0.13.1+cu116 torchaudio==0.12.1 --extra-index-url https://download.pytorch.org/whl/cu116
pip install -r requirements.txt
```

## Token Preprocessing (Tok)
### Step 1: Download the dataset
Download the datasets: [Dataset](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz). Unpack the datasets somewhere outside this project and put train.json, dev.json, tables.json, and database folder under ./data/ directory.
### Step 2: Configure the Config file
Set the value of `"token_preprocessing"` to be `"true"` in config file. There are two config files under `./configs/` directory - `train.json` and `train_deepspeed.json`. To train without deepspeed, modify the `"train.json"` file, and for training with deepspeed, modify the `"train_deepspeed.json"` file.
### Step 3: Run the program
```
git clone https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git
cd ood-generalization-semantic-boundary-techniques
deepspeed main.py configs/train_deepspeed.json 
```

## Component Boundary Marking (comp)
### Step 1: Download the dataset
Download the datasets: [Dataset](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz). Unpack the datasets somewhere outside this project and put train.json, dev.json, tables.json, and database folder under ./data/ directory.
### Step 2: Configure the Config file
The value of `"token_preprocessing"` should be set to `"false"` for training `"T5-base+Comp"` and `"true"` for training `"T5-base+Tok+Comp"`. There are two config files under `./configs/` directory - `train.json` and `train_deepspeed.json`. To train without deepspeed, modify the `"train.json"` file, and for training with deepspeed, modify the `"train_deepspeed.json"` file.
### Step 3: Run the program
```
git clone https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git
cd ood-generalization-semantic-boundary-techniques
deepspeed main.py configs/train_deepspeed.json 
```peed main.py configs/train_deepspeed.json
```
 
