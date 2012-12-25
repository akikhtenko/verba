package org.verba.stardict;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.util.InputStreamReader;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

public class PhraseDefinitionRepository implements Closeable {
	private InputStreamReader streamReader;
	private DictionaryMetadata dictionaryMetadata;

	public PhraseDefinitionRepository(InputStream aDictionaryPayloadStream, DictionaryMetadata dictionaryMetadata) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
		this.dictionaryMetadata = dictionaryMetadata;
	}

	public PhraseDefinition find(PhraseDefinitionCoordinates wordCoordinates) throws IOException {
		PhraseDefinition phraseDefinition = new PhraseDefinition();

		// There can be only ONE part in a phrase definition.
		// Though each part can contain multiple elements
		// when PhraseDefinitionFormat is not set the number of elements is only determined
		// by the length of a phrase definition. Otherwise PhraseDefinitionFormat explicitly says
		// how many elements will comprise one phrase definition part.
		XdxfPhraseDefinitionElement phraseDefinitionElement = readFirstPhraseDefinitionElement(wordCoordinates);
		phraseDefinition.add(phraseDefinitionElement);

		return phraseDefinition;
	}

	private XdxfPhraseDefinitionElement readFirstPhraseDefinitionElement(PhraseDefinitionCoordinates phraseCoordinates) throws IOException {
		byte[] phraseDefinitionBuffer = streamReader.readBytesAtOffset(phraseCoordinates.getPhraseDefinitionOffset(),
				phraseCoordinates.getPhraseDefinitionLength());
		return createPhraseDefinitionElement(phraseDefinitionBuffer);
	}

	private XdxfPhraseDefinitionElement createPhraseDefinitionElement(byte[] phraseDefinitionElementBuffer) {
		return new XdxfPhraseDefinitionElement(adjustPhraseDefinitionXdxmElementBuffer(phraseDefinitionElementBuffer));
	}

	private byte[] adjustPhraseDefinitionXdxmElementBuffer(byte[] purePhraseDefinitionPartData) {
		ByteBuffer adjustedPhraseDefinitionData = ByteBuffer.allocate(purePhraseDefinitionPartData.length + 9);

		adjustedPhraseDefinitionData.put("<ar>".getBytes());
		adjustedPhraseDefinitionData.put(purePhraseDefinitionPartData);
		adjustedPhraseDefinitionData.put("</ar>".getBytes());

		return adjustedPhraseDefinitionData.array();
	}

	@Override
	public void close() throws IOException {
		streamReader.close();
	}
}
