package org.verba.stardict.metadata.elementtype;

public class PhraseDefinitionElementTypeFactory {
	public PhraseDefinitionElementType createFromChar(char chr) {
			switch (chr) {
			case 'x':
				return new XdxfElementType();
			case 'h':
				return new HtmlElementType();
			default:
				return new PlainMeaningElementType();
			}
	}
}
