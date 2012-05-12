package org.verba.stardict;

public class WordDefinition {
	private String wordDefinitionText;
	
	public WordDefinition(String aWordDefinitionText) {
		wordDefinitionText = aWordDefinitionText;
	}
	
	public String asText() {
		return wordDefinitionText;
	}

}
