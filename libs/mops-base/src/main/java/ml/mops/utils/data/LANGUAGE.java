package ml.mops.utils.data;

import java.util.*;
import java.util.stream.IntStream;

public enum LANGUAGE implements  CharSequence  {
	RUSSIAN("rus"),
	ENGLISH("eng");

	protected String defaultLanguageID = "eng";
	private String langID = defaultLanguageID;
	protected Collection<String> correctLanguages;

	LANGUAGE(String langID) {
		this.langID = langID.toLowerCase(Locale.ROOT);
		correctLanguages = new HashSet<String>();
		for (LANGUAGE language : LANGUAGE.values()) correctLanguages.add(language.toString());
	}


	@Deprecated //Нах я это написал если это не нужно?
	public LANGUAGE getLanguage(String langID) {
		LANGUAGE language = getLanguage(defaultLanguageID); //Ультра тупая не нужная рекурсия но мне лен едлать норм
		for (LANGUAGE l : LANGUAGE.values()) {
			if (l.toString().equalsIgnoreCase(langID)) language = l;
		}
		return language;
	}

	public boolean isCorrectLanguage(CharSequence language) {
		return correctLanguages.contains(language.toString());
	}

	public String[] correctLanguages() {
		return correctLanguages.toArray(new String[correctLanguages.size()]);
	}

	/**
	 * Returns the length of this character sequence.  The length is the number
	 * of 16-bit {@code char}s in the sequence.
	 *
	 * @return the number of {@code char}s in this sequence
	 */
	@Override
	public int length() {
		return langID.length();
	}

	/**
	 * Returns the {@code char} value at the specified index.  An index ranges from zero
	 * to {@code length() - 1}.  The first {@code char} value of the sequence is at
	 * index zero, the next at index one, and so on, as for array
	 * indexing.
	 *
	 * <p>If the {@code char} value specified by the index is a
	 * <a href="{@docRoot}/java.base/java/lang/Character.html#unicode">surrogate</a>, the surrogate
	 * value is returned.
	 *
	 * @param index the index of the {@code char} value to be returned
	 * @return the specified {@code char} value
	 * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than
	 *                                   {@code length()}
	 */
	@Override
	public char charAt(int index) {
		return langID.charAt(index);
	}

	/**
	 * Returns {@code true} if this character sequence is empty.
	 *
	 * @return {@code true} if {@link #length()} is {@code 0}, otherwise
	 * {@code false}
	 * @implSpec The default implementation returns the result of calling {@code length() == 0}.
	 * @since 15
	 */
	@Override
	public boolean isEmpty() {
		return langID.isEmpty();
	}

	/**
	 * Returns a {@code CharSequence} that is a subsequence of this sequence.
	 * The subsequence starts with the {@code char} value at the specified index and
	 * ends with the {@code char} value at index {@code end - 1}.  The length
	 * (in {@code char}s) of the
	 * returned sequence is {@code end - start}, so if {@code start == end}
	 * then an empty sequence is returned.
	 *
	 * @param start the start index, inclusive
	 * @param end   the end index, exclusive
	 * @return the specified subsequence
	 * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
	 *                                   if {@code end} is greater than {@code length()},
	 *                                   or if {@code start} is greater than {@code end}
	 */
	@Override
	public CharSequence subSequence(int start, int end) {
		return langID.subSequence(start, end);
	}

	@Override
	public String toString() {
		return langID;
	}

	/**
	 * Returns a stream of {@code int} zero-extending the {@code char} values
	 * from this sequence.  Any char which maps to a <a
	 * href="{@docRoot}/java.base/java/lang/Character.html#unicode">surrogate code
	 * point</a> is passed through uninterpreted.
	 *
	 * <p>The stream binds to this sequence when the terminal stream operation
	 * commences (specifically, for mutable sequences the spliterator for the
	 * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
	 * If the sequence is modified during that operation then the result is
	 * undefined.
	 *
	 * @return an IntStream of char values from this sequence
	 * @since 1.8
	 */
	@Override
	public IntStream chars() {
		return langID.chars();
	}

	/**
	 * Returns a stream of code point values from this sequence.  Any surrogate
	 * pairs encountered in the sequence are combined as if by {@linkplain
	 * Character#toCodePoint Character.toCodePoint} and the result is passed
	 * to the stream. Any other code units, including ordinary BMP characters,
	 * unpaired surrogates, and undefined code units, are zero-extended to
	 * {@code int} values which are then passed to the stream.
	 *
	 * <p>The stream binds to this sequence when the terminal stream operation
	 * commences (specifically, for mutable sequences the spliterator for the
	 * stream is <a href="../util/Spliterator.html#binding"><em>late-binding</em></a>).
	 * If the sequence is modified during that operation then the result is
	 * undefined.
	 *
	 * @return an IntStream of Unicode code points from this sequence
	 * @since 1.8
	 */
	@Override
	public IntStream codePoints() {
		return langID.codePoints();
	}


}
