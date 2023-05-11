from typing import Optional
from dataclasses import dataclass, field


@dataclass
class ModelArguments:
	model_name_or_path: str = field(
		metadata = {"help": "Path or the model name --- check on Huggingface website for the list of possible models"}
		)
	run_id: str = field(
		metadata = {"help": "run for load metric"}
		)
	config_name: Optional[str] = field(
		default = None,
		metadata={"help": "Pretrained config name or path if not the same as model_name"},
		)
	tokenizer_name: Optional[str] = field(
		default = None,
		metadata={"help": "Pretrained tokenizer name or path if not the same as model_name"},
		)
	cache_dir: Optional[str] = field(
		default=None,
		metadata={"help": "Where to store the pretrained models downloaded from huggingface.co"},
	)
	use_fast_tokenizer: bool = field(
		default=True,
		metadata={"help": "Whether to use one of the fast tokenizer (backed by the tokenizers library) or not."},
	)
	model_revision: str = field(
		default="main",
		metadata={"help": "The specific model version to use (can be a branch name, tag name or commit id)."},
	)
	use_auth_token: bool = field(
		default=False,
		metadata={
			"help": "Will use the token generated when running `transformers-cli login` (necessary to use this script "
			"with private models)."
		},
	)
