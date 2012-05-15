package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.util.InputStreamReader;
import org.verba.xdxf.XdxfWordDefinitionPart;

public class WordDefinitionRepository {
	private InputStreamReader streamReader;

	public WordDefinitionRepository(InputStream aDictionaryPayloadStream) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
	}

	public WordDefinition find(WordDefinitionCoordinates wordCoordinates) throws IOException {
		WordDefinition wordDefinition = new WordDefinition();

		WordDefinitionPart wordDefinitionPart = readWordDefinitionPart(wordCoordinates);
		wordDefinition.add(wordDefinitionPart);

		return wordDefinition;
	}

	private WordDefinitionPart readWordDefinitionPart(WordDefinitionCoordinates wordCoordinates) throws IOException {
		byte[] wordDefinitionBuffer = streamReader.readBytesAtOffset(wordCoordinates.getWordDefinitionOffset(),
				wordCoordinates.getWordDefinitionLength());
		return new XdxfWordDefinitionPart(adjustWordDefinitionPart(wordDefinitionBuffer));
	}

	private byte[] adjustWordDefinitionPart(byte[] pureWordDefinitionPartData) {
		ByteBuffer adjustedWordDefinitionData = ByteBuffer.allocate(pureWordDefinitionPartData.length + 9);

		adjustedWordDefinitionData.put("<ar>".getBytes());
		adjustedWordDefinitionData.put(pureWordDefinitionPartData);
		adjustedWordDefinitionData.put("</ar>".getBytes());

		return adjustedWordDefinitionData.array();
	}
}
