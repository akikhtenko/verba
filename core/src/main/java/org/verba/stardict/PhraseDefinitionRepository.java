package org.verba.stardict;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.elementtype.PhraseDefinitionElementType;
import org.verba.stardict.metadata.elementtype.PhraseDefinitionElementTypeFactory;
import org.verba.util.ByteArray;
import org.verba.util.InputStreamReader;

public class PhraseDefinitionRepository implements Closeable {
	private InputStreamReader streamReader;
	private DictionaryMetadata dictionaryMetadata;
	private PhraseDefinitionElementTypeFactory elementTypeFactory = new PhraseDefinitionElementTypeFactory();

	public PhraseDefinitionRepository(InputStream aDictionaryPayloadStream, DictionaryMetadata dictionaryMetadata) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
		this.dictionaryMetadata = dictionaryMetadata;
	}

	public PhraseDefinition find(PhraseDefinitionCoordinates phraseCoordinates) throws IOException {
		PhraseDefinition phraseDefinition = new PhraseDefinition();

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
		byte[] elementDataBytes = rawElementData.bytes();
		PhraseDefinitionElementType elementType = elementTypeFactory.createFromChar((char) elementDataBytes[0]);

		return elementType.parsePhraseDefinitionElement(new ByteArray(elementDataBytes, 1, elementDataBytes.length - 1));
	}

	@Override
	public void close() throws IOException {
		streamReader.close();
	}
}
