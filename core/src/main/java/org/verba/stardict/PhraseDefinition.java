package org.verba.stardict;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PhraseDefinition {
	private List<PhraseDefinitionPart> phraseDefinitionParts = new LinkedList<PhraseDefinitionPart>();

	public void add(PhraseDefinitionPart phraseDefinitionPart) {
		phraseDefinitionParts.add(phraseDefinitionPart);
	}

	public Iterator<PhraseDefinitionPart> iterator() {
		return phraseDefinitionParts.iterator();
	}
}
