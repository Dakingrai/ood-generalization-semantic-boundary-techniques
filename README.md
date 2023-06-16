# Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques
This repository contains the code for ACL 2023 paper: [Improving Generalization in Language Model-Based Text-to-SQL Semantic Parsing: Two Simple Semantic Boundary-Based Techniques](https://arxiv.org/abs/2305.17378).

In this paper, we introduce two semantic-boundary-based techniques to improve compositional and domain generalization in Language Model(LM)-based semantic parsers - (1) Token preprocessing (2) Component Boundary Marking. !![github](https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques/assets/3531451/0fb9eff6-19a7-49c0-8f46-a48130545dfd)

## Environment Setup
Install Python dependency via `pip install -r requirements.txt`.

## Usage
```
git clone https://github.com/Dakingrai/ood-generalization-semantic-boundary-techniques.git
cd ood-generalization-semantic-boundary-techniques
deepspeed main.py configs/train_deepspeed.json
```
 
