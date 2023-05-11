import json
from typing import Callable, Tuple
import logging
import datasets.load
from datasets.dataset_dict import DatasetDict
from datasets.metric import Metric
from datasets.arrow_dataset import Dataset, concatenate_datasets
from transformers.tokenization_utils_fast import PreTrainedTokenizerFast
from transformers.training_args import TrainingArguments
from .args import ModelArguments
from .dataset import (
	DataArguments,
	DataTrainingArguments,
	DatasetSplits,
	TrainSplit,
	_prepare_train_split,
	prepare_splits,
	)

from .spider import spider_add_serialized_schema, spider_pre_process_function

logger = logging.getLogger(__name__)

def _log_duplicate_count(dataset: Dataset, dataset_name: str, split: str)-> None:
	d = dataset.to_dict()
	d_t = [tuple((k, tuple(v)) for k, v in zip(d.keys(), vs)) for vs in zip(*d.values())]
	d_t = set(d_t)
	num_examples = len(d_t)
	duplicate_count = num_examples - len(d_t)
	if duplicate_count > 0:
		logger.warning(f"The split ``{split}`` of the dataset ``{dataset_name}`` contains {duplicate_count} duplicates out of {num_examples} examples")


def load_dataset(data_args: DataArguments, model_args:ModelArguments, data_training_args: DataTrainingArguments, training_args: TrainingArguments, tokenizer: PreTrainedTokenizerFast)->Tuple[Metric, DatasetSplits]:
	print('DataArgs::::{}'.format(data_args))
	_spider_dataset_dict: Callable[[], DatasetDict] = lambda: datasets.load.load_dataset(path = data_args.dataset_paths["spider"], cache_dir = model_args.cache_dir)
	_spider_metric: Callable[[], Metric] = lambda: datasets.load.load_metric(
		path = data_args.metric_paths["spider"], config_name = data_args.metric_config, test_suite_db_dir=data_args.test_suite_db_dir, experiment_id=f"My_experiment_{model_args.run_id}"
		)

	_spider_add_serialized_schema = lambda ex: spider_add_serialized_schema(ex=ex, data_training_args=data_training_args)
	_spider_pre_process_function = lambda batch, max_source_length, max_target_length: spider_pre_process_function(batch, max_source_length, max_target_length, data_training_args, tokenizer)

	_prepare_splits_kwargs = {
	"data_args": data_args,
	"training_args": training_args,
	"data_training_args": data_training_args
	}
	if data_args.dataset == "spider":
		metric = _spider_metric()
		dataset_splits = prepare_splits(dataset_dict = _spider_dataset_dict(),add_serialized_schema = _spider_add_serialized_schema, pre_process_function=_spider_pre_process_function, **_prepare_splits_kwargs)
	else:
		raise NotImplementedError()
	if dataset_splits.train_split is not None:
		_log_duplicate_count(dataset=dataset_splits.train_split.dataset, dataset_name=data_args.dataset, split="train")
	if dataset_splits.eval_split is not None:
		_log_duplicate_count(dataset=dataset_splits.eval_split.dataset, dataset_name=data_args.dataset, split="eval")
	if dataset_splits.test_splits is not None:
		for section, split in dataset_splits.test_splits.items():
			_log_duplicate_count(dataset=split.dataset, dataset_name=data_args.dataset, split=section)
	return metric, dataset_splits
