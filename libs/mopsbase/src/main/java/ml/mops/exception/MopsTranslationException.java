package ml.mops.exception;

public class MopsTranslationException extends Exception {
	public MopsTranslationException() {
		super("Translation error.");
	}
	public MopsTranslationException(String str) {
		super(str);
	}
}
