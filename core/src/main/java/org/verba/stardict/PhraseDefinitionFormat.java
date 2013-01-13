package org.verba.stardict;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.verba.stardict.metadata.elementtype.PhraseDefinitionElementType;

public class PhraseDefinitionFormat {
	private List<PhraseDefinitionElementType> format = new LinkedList<PhraseDefinitionElementType>();

	public void add(PhraseDefinitionElementType phraseDefinitionElementType) {
		format.add(phraseDefinitionElementType);
	}

	public PhraseDefinitionElementType get(int index) {
		return format.get(index);
	}

	public Iterator<PhraseDefinitionElementType> elementTypes() {
		return format.iterator();
	}
}
