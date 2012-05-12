package org.verba.stardict;

public class WordDefinitionCoordinates {
	private String targetWord;
	private long wordDefinitionOffset;
	private int wordDefinitionLength;
	
	public WordDefinitionCoordinates(String aTargetWord, long aWordDefinitionOffset, int aWordDefinitionLength) {
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

	public int getWordDefinitionLength() {
		return wordDefinitionLength;
	}

	public boolean matches(String wordToCompareWith) {
		return targetWord.startsWith(wordToCompareWith);
	}

}
