# Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques
This repository contains the code for ACL 2023 paper: [Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques](https://arxiv.org/abs/2305.17378).

If you find our work helpful, please cite as
```
@article{rai2023improving,
  title={Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques},
  author={Rai, Daking and Wang, Bailin and Zhou, Yilun and Yao, Ziyu},
  journal={arXiv preprint arXiv:2305.17378},
  year={2023}
}
```


## Overview
This paper introduces two semantic-boundary-based techniques, Token preprocessing (Tok) and Component boundary marking (Comp), to improve compositional and domain generalization in language model-based semantic parsers.
At the token level, Token Preprocessing consists of preprocessing steps to handle naming conventions in database schemas and SQL queries such that a pre-trained LM tokenizer can split them into semantically meaningful tokens. At the sequence level, Component boundary marking (Comp) introduces special tokens to mark the semantic boundaries (e.g., phrases) aligned between the source NL and the target SQL as shown in Table 1. These special tokens implicitly help the LM-based parser build more precise input-output correspondences that are crucial for compositional generalization. Please refer to our [paper](https://arxiv.org/abs/2305.17378) for more details.

![github](https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques/assets/3531451/0fb9eff6-19a7-49c0-8f46-a48130545dfd)

## Environment Setup
This project is tested in Python 3.8.5.

To get started, set up the environment:
```
python -m venv env 
source env/bin/activate
pip install torch==1.12.1+cu116 torchvision==0.13.1+cu116 torchaudio==0.12.1 --extra-index-url https://download.pytorch.org/whl/cu116
pip install -r requirements.txt
```

Now, clone the repository. 
```
git clone https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git
cd ood-generalization-semantic-boundary-techniques
```
Note: The code for fine-tuning the T5 models is built upon the codebase from [PICARD](https://github.com/ServiceNow/picard), while the code for converting NatSQL to SQL during model evaluation is integrated using the codebase from [NatSQL](https://github.com/ygan/NatSQL). 

## Token Preprocessing (Tok)
### Step 1: Download the dataset
Download the [datasets](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz) and unpack them somewhere outside this project. Copy `train.json`, `dev.json`, and all test sets from `./data/vanilla/` of the downloaded folder to `./data/` of the project directory.
### Step 2: Configure the Config file
Set the value of `"token_preprocessing"` to be `"true"` in the config file in the config file. Conversely, if the value is set to `"false"`, T5 will be trained without token preprocessing. Configs file can be found under `"./configs/"`. To train without deepspeed, edit the `"./configs/train.json"` file, and for training with deepspeed, edit the `"./configs/train_deepspeed.json"` file.
### Step 3: Run the program
```
deepspeed main.py configs/train_deepspeed.json # for finetuning with deepspeed
python main.py configs/train.json # for finetuning without deepspeed
```
### Step 4: Evaluate the test sets
There are four evaluation sets from [Spider-CG](https://arxiv.org/abs/2205.02054) - `CG-APP(T)`, `CG-APP(D)`, `CG-SUB(T)`, and `CG-SUB(D)`. More details about the dataset on the paper. They can be evaluated in the following steps:
1. Run the inference code to get the prediction of the test set using the selected checkpoint. You can use your own finetuned checkpoint or also run evaluations on our [checkpoints](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz).
2. Evaluate the predicted SQL to get the exact match accuracy (EX) and execution accuracy (EX). This code is adapted from [Spider](https://github.com/taoyds/spider) official evaluation script.
```
python inference.py --checkpoint checkpoint_path --data test_data_path --tokenized 
python evaluation.py --gold data/dev_gold.sql --pred pred.sql --etype all
```
## Component Boundary Marking (comp)
### Step 1: Download the dataset
Download the [datasets](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz) and unpack them somewhere outside this project. Copy `train.json`, `dev.json`, and all test sets from `./data/component_boundary_marking/` of the downloaded folder to `./data/` of the project directory.
### Step 2: Configure the Config file
To train `"T5-base+Comp"`, set the value of `"token_preprocessing"` to be `"false"`. And, to train `"T5-base+Tok+Comp"`, set the value of `"token_preprocessing"` to be `"true"` in the config file. Configs file can be found under `"./configs/"`. To train without deepspeed, edit  the `"./configs/train.json"` file, and for training with deepspeed, edit the `"./configs/train_deepspeed.json"` file.
### Step 3: Run the program
```
deepspeed main.py configs/train_deepspeed.json # for finetuning with deepspeed
python main.py configs/train.json # for finetuning without deepspeed
```

### Step 4: Evaluate the test sets
There are four evaluation sets from [Spider-CG](https://arxiv.org/abs/2205.02054) - `CG-APP(T)`, `CG-APP(D)`, `CG-SUB(T)`, and `CG-SUB(D)`. More details about the dataset on the paper. They can be evaluated in the following steps:
1. Run the inference code to get the prediction of the test set using the selected checkpoint. You can use your own finetuned checkpoint or also run evaluations on our [checkpoints](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz).
2. Evaluate the predicted SQL to get the exact match accuracy (EX) and execution accuracy (EX). This code is adapted from [Spider](https://github.com/taoyds/spider) official evaluation script.
```
python evaluation/inference.py --checkpoint checkpoint_path --data test_data_path --tokenized 
python evaluation/evaluation.py --gold data/dev_gold.sql --pred pred.sql --etype all 
```
## Acknowledgments
We would like to thank all anonymous reviewers for their constructive comments. We also thank Yujian Gan and Xinyun Chen for their help in using the NatSQL and the Spider-SS datasets,
Pengcheng Yin for using the codebase of Attn. Sup and Torsten Scholak, Nathan Schucher, Dzmitry Bahdanau for using the codebase of fine-tuning T5 models. This project was supported by resources provided by the Office of Research Computing at George Mason University (https://orc.gmu.edu) and funded in part by grants from the National Science Foundation (Awards Number 1625039 and 2018631).
 
