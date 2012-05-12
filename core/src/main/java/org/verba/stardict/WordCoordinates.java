package org.verba.stardict;

public class WordCoordinates {
	private String targetWord;
	private long wordDefinitionOffset;
	private long wordDefinitionLength;
	
	public WordCoordinates(String aTargetWord, long aWordDefinitionOffset, long aWordDefinitionLength) {
		targetWord = aTargetWord;
		wordDefinitionOffset = aWordDefinitionOffset;
		wordDefinitionLength = aWordDefinitionLength;
	}

	public String getTargetWord() {
		return targetWord;
	}

	public long getWordDefinitionOffset() {
		return wordDefinitionOffset;
	}

	public long getWordDefinitionLength() {
		return wordDefinitionLength;
	}

}
