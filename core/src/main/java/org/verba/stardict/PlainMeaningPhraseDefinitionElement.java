package org.verba.stardict;

import org.verba.util.ByteArray;

public class PlainMeaningPhraseDefinitionElement implements PhraseDefinitionElement {
	private ByteArray byteArray;

	public PlainMeaningPhraseDefinitionElement(ByteArray byteArray) {
		this.byteArray = byteArray;
	}

	public byte[] bytes() {
		return byteArray.bytes();
	}

	@Override
	public void print(PhraseDefinitionElementDisplay phraseDefinitionDisplay) {
		phraseDefinitionDisplay.print(this);
	}
}
