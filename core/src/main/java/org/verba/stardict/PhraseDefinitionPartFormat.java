package org.verba.stardict;

import java.util.LinkedList;
import java.util.List;

public class PhraseDefinitionPartFormat {
	private List<PhraseDefinitionElementType> format = new LinkedList<PhraseDefinitionElementType>();

	public void add(PhraseDefinitionElementType phraseDefinitionElementType) {
		format.add(phraseDefinitionElementType);
	}
}