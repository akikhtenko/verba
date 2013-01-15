package org.verba.stardict.metadata.elementtype;

import org.verba.html.HtmlPhraseDefinitionElement;
import org.verba.stardict.PhraseDefinitionElement;
import org.verba.util.ByteArray;

public class HtmlElementType implements PhraseDefinitionElementType {

	@Override
	public PhraseDefinitionElement parsePhraseDefinitionElement(ByteArray rawElementData) {
		return new HtmlPhraseDefinitionElement(rawElementData);
	}
}
