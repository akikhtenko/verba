package org.verba.stardict.metadata.elementtype;

import java.nio.ByteBuffer;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.util.ByteArray;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

public class XdxfElementType implements PhraseDefinitionElementType {

	@Override
	public PhraseDefinitionElement parsePhraseDefinitionElement(ByteArray rawElementData) {
		return new XdxfPhraseDefinitionElement(adjustPhraseDefinitionXdxmElementBuffer(rawElementData.bytes()));
	}

	private byte[] adjustPhraseDefinitionXdxmElementBuffer(byte[] purePhraseDefinitionPartData) {
		ByteBuffer adjustedPhraseDefinitionData = ByteBuffer.allocate(purePhraseDefinitionPartData.length + 9);

		adjustedPhraseDefinitionData.put("<ar>".getBytes());
		adjustedPhraseDefinitionData.put(purePhraseDefinitionPartData);
		adjustedPhraseDefinitionData.put("</ar>".getBytes());

		return adjustedPhraseDefinitionData.array();
	}
}
