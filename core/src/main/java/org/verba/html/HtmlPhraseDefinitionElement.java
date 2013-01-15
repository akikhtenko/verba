package org.verba.html;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.stardict.PhraseDefinitionElementDisplay;
import org.verba.util.ByteArray;

public class HtmlPhraseDefinitionElement implements PhraseDefinitionElement {
	private ByteArray byteArray;

	public HtmlPhraseDefinitionElement(ByteArray byteArray) {
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
