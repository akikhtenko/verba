package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;

import org.verba.util.InputStreamReader;

public class WordDefinitionRepository {
	private InputStreamReader streamReader;
	
	public WordDefinitionRepository(InputStream aDictionaryPayloadStream) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
	}

	public WordDefinition find(WordDefinitionCoordinates wordCoordinates) throws IOException {
		byte[] wordDefinitionBuffer = streamReader.readBytesAtOffset(wordCoordinates.getWordDefinitionOffset(), wordCoordinates.getWordDefinitionLength());
		return new WordDefinition(new String(wordDefinitionBuffer));
	}
	
}
