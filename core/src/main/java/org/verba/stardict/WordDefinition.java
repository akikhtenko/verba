package org.verba.stardict;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class WordDefinition {
	private List<WordDefinitionPart> wordDefinitionParts = new LinkedList<WordDefinitionPart>();
	
	public void add(WordDefinitionPart wordDefinitionPart) {
		wordDefinitionParts.add(wordDefinitionPart);
	}
	
	public Iterator<WordDefinitionPart> iterator() {
		return wordDefinitionParts.iterator();
	}
}
