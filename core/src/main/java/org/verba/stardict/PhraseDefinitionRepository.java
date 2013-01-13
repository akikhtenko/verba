package org.verba.stardict;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.util.ByteArray;
import org.verba.util.InputStreamReader;

public class PhraseDefinitionRepository implements Closeable {
	private InputStreamReader streamReader;
	private DictionaryMetadata dictionaryMetadata;

	public PhraseDefinitionRepository(InputStream aDictionaryPayloadStream, DictionaryMetadata dictionaryMetadata) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
		this.dictionaryMetadata = dictionaryMetadata;
	}

	public PhraseDefinition find(PhraseDefinitionCoordinates phraseCoordinates) throws IOException {
		PhraseDefinition phraseDefinition = new PhraseDefinition();

		// There can be only ONE part in a phrase definition.
		// Though each part can contain multiple elements
		// when PhraseDefinitionFormat is not set the number of elements is only determined
		// by the length of a phrase definition. Otherwise PhraseDefinitionFormat explicitly says
		// how many elements will comprise one phrase definition part.
		byte[] phraseDefinitionBuffer = streamReader.readBytesAtOffset(phraseCoordinates.getPhraseDefinitionOffset(), phraseCoordinates.getPhraseDefinitionLength());
		int elementIndex = 0;
		for (ByteArray elementByteArray: new ByteArray(phraseDefinitionBuffer).split(new byte[] {'\0'})) {
			phraseDefinition.add(parsePhraseDefinitionElement(elementByteArray, elementIndex++));
		}

		return phraseDefinition;
	}

	private PhraseDefinitionElement parsePhraseDefinitionElement(ByteArray elementByteArray, int elementIndex) {
		PhraseDefinitionFormat phraseDefinitionFormat = dictionaryMetadata.getPhraseDefinitionFormat();
		return phraseDefinitionFormat == null
				? readPhraseDefinitionElementFromUntypedDefinition(elementByteArray)
				: phraseDefinitionFormat.get(elementIndex).parsePhraseDefinitionElement(elementByteArray);
	}

	private PhraseDefinitionElement readPhraseDefinitionElementFromUntypedDefinition(ByteArray rawElementData) {
		return null;
	}

	@Override
	public void close() throws IOException {
		streamReader.close();
	}
}
