package org.verba.stardict;

import java.io.IOException;

public class WordDefinitionCoordinatesRepository {
	private DictionaryIndexReader indexReader;

	public WordDefinitionCoordinatesRepository(DictionaryIndexReader anIndexReader) {
		indexReader = anIndexReader;
	}

	public WordDefinitionCoordinates find(String targetWord) throws IOException,
			WordDefinitionCoordinatesNotFoundException {
		WordDefinitionCoordinates wordDefinitionCoordinates = getWordDefinitionCoordinates(targetWord);
		ensureWordDefinitionCoordinatesFound(wordDefinitionCoordinates, targetWord);
		return wordDefinitionCoordinates;
	}

	private WordDefinitionCoordinates getWordDefinitionCoordinates(String targetWord) throws IOException {
		WordDefinitionCoordinates wordDefinitionCoordinates = null;

		while (indexReader.hasNextWordDefinition()) {
			wordDefinitionCoordinates = indexReader.readWordCoordinates();
			if (wordDefinitionCoordinates.matches(targetWord)) {
				break;
			} else {
				wordDefinitionCoordinates = null;
			}
		}

		return wordDefinitionCoordinates;
	}

	private void ensureWordDefinitionCoordinatesFound(WordDefinitionCoordinates wordDefinitionCoordinates,
			String erroneousWord) throws WordDefinitionCoordinatesNotFoundException {
		if (wordDefinitionCoordinates == null) {
			throw new WordDefinitionCoordinatesNotFoundException(String.format(
					"Couldn't locate word definition coordinate in index for [%s]", erroneousWord));
		}
	}

	public static class WordDefinitionCoordinatesNotFoundException extends Exception {
		private static final long serialVersionUID = 4032271917558581243L;

		public WordDefinitionCoordinatesNotFoundException(String message) {
			super(message);
		}
	}

	public void destroy() throws IOException {
		indexReader.close();
	}
}
