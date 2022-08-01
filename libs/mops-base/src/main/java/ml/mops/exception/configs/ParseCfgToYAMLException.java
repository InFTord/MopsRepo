package ml.mops.exception.configs;

import ml.mops.exception.MopsConfigException;

import java.util.Arrays;

public class ParseCfgToYAMLException extends MopsConfigException {
	public ParseCfgToYAMLException(Exception e) {
		super("Error while parsing Config into yaml string\nException:" + e.getLocalizedMessage() + "\nStacktrace:\n\n\n" + Arrays.toString(e.getStackTrace()));
	}
}
