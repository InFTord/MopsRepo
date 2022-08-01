package ml.mops.exception.configs;

import ml.mops.exception.MopsConfigException;

public class BlankConfigException extends MopsConfigException {
	public BlankConfigException() {
		super("Config is blank");
	}
}
