### Step 4: Evaluate the test sets
There are four evaluation sets from [Spider-CG](https://arxiv.org/abs/2205.02054) - `CG-APP(T)`, `CG-APP(D)`, `CG-SUB(T)`, and `CG-SUB(D)`. More details about the dataset on the paper. They can be evaluated in the following steps:
1. Run the inference code to get the prediction of the test set using the selected checkpoint. You can use your own finetuned checkpoint or also run evaluations on our [checkpoints](https://gmuedu-my.sharepoint.com/:f:/g/personal/drai2_gmu_edu/EpGaXUlbZ2JEj47w1vNN4z4BKjgvseGeGMirT125Xw85gg?e=Mw9tFz).
2. Evaluate the predicted SQL to get the exact match accuracy (EX) and execution accuracy (EX). This code is adapted from [Spider](https://github.com/taoyds/spider) official evaluation script.
```
python inference.py --checkpoint checkpoint_path --data test_data_path --tokenized 
python evaluation.py --gold data/dev_gold.sql --pred pred.sql --etype all
```