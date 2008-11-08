package info.bliki.wiki.model;

public class Reference {
	public final static String CHARACTER_REFS = "abcdefghijklmnopqrstuvwxyz"
			+ "ABCDEFGHIJKLMNOPQRTSUVWXYZ";

	private String fReference;
	private String fNameAttribute;
	private int fCounter;

	public Reference(String reference) {
		this(reference, "");
	}

	public Reference(String reference, String nameAttribute) {
		fReference = reference;
		fNameAttribute = nameAttribute;
		fCounter = 0;
	}

	public String getRefString() {
		return fReference;
	}

	public int incCounter() {
		return ++fCounter;
	}

	public int getCounter() {
		return fCounter;
	}
	
	public String getAttribute() {
		return fNameAttribute;
	}
}
