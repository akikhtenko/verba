package org.verba.stardict;

import org.verba.html.HtmlPhraseDefinitionElement;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

public interface PhraseDefinitionElementDisplay {
	void print(XdxfPhraseDefinitionElement xdxfPhraseDefinitionElement);

	void print(HtmlPhraseDefinitionElement htmlPhraseDefinitionElement);

	void print(PlainMeaningPhraseDefinitionElement plainMeaningPhraseDefinitionElement);
}
