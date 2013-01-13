package org.verba.stardict.metadata.elementtype;

import org.verba.stardict.PhraseDefinitionElement;
import org.verba.util.ByteArray;

public interface PhraseDefinitionElementType {
	PhraseDefinitionElement parsePhraseDefinitionElement(ByteArray elementByteArray);
}
