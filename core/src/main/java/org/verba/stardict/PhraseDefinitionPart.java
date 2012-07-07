package org.verba.stardict;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PhraseDefinitionPart {
	private List<PhraseDefinitionElement> phraseDefinitionElements = new LinkedList<PhraseDefinitionElement>();

	public void add(PhraseDefinitionElement phraseDefinitionElement) {
		phraseDefinitionElements.add(phraseDefinitionElement);
	}

	public Iterator<PhraseDefinitionElement> elements() {
		return phraseDefinitionElements.iterator();
	}
}
