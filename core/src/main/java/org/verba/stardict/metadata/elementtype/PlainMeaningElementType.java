package org.verba.stardict.metadata.elementtype;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.stardict.PlainMeaningPhraseDefinitionElement;
import org.verba.util.ByteArray;

public class PlainMeaningElementType implements PhraseDefinitionElementType {

	@Override
	public PhraseDefinitionElement parsePhraseDefinitionElement(ByteArray rawElementData) {
		return new PlainMeaningPhraseDefinitionElement(rawElementData);
	}
}
