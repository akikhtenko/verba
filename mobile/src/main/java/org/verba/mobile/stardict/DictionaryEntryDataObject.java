package org.verba.mobile.stardict;

import org.verba.stardict.PhraseDefinitionCoordinates;

public class DictionaryEntryDataObject {
	private int id;
	private String phrase;
	private long offset;
	private int length;
	private int dictionaryId;

	public DictionaryEntryDataObject() {
	}

	public DictionaryEntryDataObject(int dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public DictionaryEntryDataObject(int dictionaryId, PhraseDefinitionCoordinates phraseDefinitionCoordinates) {
		this(dictionaryId);
		phrase = phraseDefinitionCoordinates.getTargetPhrase();
		offset = phraseDefinitionCoordinates.getPhraseDefinitionOffset();
		length = phraseDefinitionCoordinates.getPhraseDefinitionLength();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(int dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	@Override
	public String toString() {
		return phrase;
	}

	public PhraseDefinitionCoordinates asPhraseDefinitionCoordinates() {
		return new PhraseDefinitionCoordinates(phrase, offset, length);
	}
}
