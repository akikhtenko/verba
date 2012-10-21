package org.verba.boundary;

import java.util.List;

import org.verba.stardict.PhraseDefinition;

public interface PhraseLookup {
	List<PhraseDefinition> lookup(String phrase);
}
