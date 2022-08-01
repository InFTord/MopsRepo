package ml.mops.exception;

public class MopsConfigException extends Exception {
	public MopsConfigException() {
		super("Exception while loading/processing files");
	}
	public MopsConfigException(String str) {
		super(str);
	}
}
