package ml.mops.exception;

public class UnsoportedYetFeature extends Exception {
	public UnsoportedYetFeature() {
		super("Unsuported feature!");
	}
	public UnsoportedYetFeature(String str) {
		super("Unsuported feature: " + str + " !");
	}
}
