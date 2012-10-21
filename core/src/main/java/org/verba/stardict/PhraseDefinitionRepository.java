package org.verba.stardict;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.util.InputStreamReader;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

public class PhraseDefinitionRepository implements Closeable {
	private InputStreamReader streamReader;

	public PhraseDefinitionRepository(InputStream aDictionaryPayloadStream) {
		streamReader = new InputStreamReader(aDictionaryPayloadStream);
	}

	public PhraseDefinition find(PhraseDefinitionCoordinates wordCoordinates) throws IOException {
		PhraseDefinition phraseDefinition = new PhraseDefinition();

		PhraseDefinitionPart phraseDefinitionPart = readPhraseDefinitionPart(wordCoordinates);
		phraseDefinition.add(phraseDefinitionPart);

		return phraseDefinition;
	}

	@Override
	public void close() throws IOException {
		streamReader.close();
	}

	private PhraseDefinitionPart readPhraseDefinitionPart(PhraseDefinitionCoordinates phraseCoordinates)
			throws IOException {
		byte[] phraseDefinitionBuffer = streamReader.readBytesAtOffset(phraseCoordinates.getPhraseDefinitionOffset(),
				phraseCoordinates.getPhraseDefinitionLength());
		return createPhraseDefinitionPart(phraseDefinitionBuffer);
	}

	private PhraseDefinitionPart createPhraseDefinitionPart(byte[] phraseDefinitionBuffer) {
		PhraseDefinitionPart phraseDefinitionPart = new PhraseDefinitionPart();
		phraseDefinitionPart.add(new XdxfPhraseDefinitionElement(adjustPhraseDefinitionPart(phraseDefinitionBuffer)));
		return phraseDefinitionPart;
	}

	private byte[] adjustPhraseDefinitionPart(byte[] purePhraseDefinitionPartData) {
		ByteBuffer adjustedPhraseDefinitionData = ByteBuffer.allocate(purePhraseDefinitionPartData.length + 9);

		adjustedPhraseDefinitionData.put("<ar>".getBytes());
		adjustedPhraseDefinitionData.put(purePhraseDefinitionPartData);
		adjustedPhraseDefinitionData.put("</ar>".getBytes());

		return adjustedPhraseDefinitionData.array();
	}
}
