package org.verba.stardict;

public class WordDefinition {
	private byte[] rawWordDefinition;
	
	public WordDefinition(byte[] wordDefinitionBuffer) {
		rawWordDefinition = wordDefinitionBuffer;
	}
	
	public String asPlainText() {
		return new String(rawWordDefinition);
	}
	
	public byte[] bytes() {
		return rawWordDefinition;
	}
}
