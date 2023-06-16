# Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques
This repository contains the code for ACL 2023 paper: [Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques](https://arxiv.org/abs/2305.17378).

In this paper, we introduce two semantic-boundary-based techniques to improve compositional and domain generalization in Language Model(LM)-based semantic parsers - (1) Token preprocessing (2) Component Boundary Marking. ![introduction](https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques/assets/3531451/9d3806b8-dbfe-47d5-9d5b-53e8e5a6a172)

## Environment Setup
Install Python dependency via `pip install -r requirements.txt`.

## Usage
```
git clone https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git
cd ood-generalization-semantic-boundary-techniques
deepspeed main.py configs/train_deepspeed.json
```
 
