package org.verba.stardict.definitions;

import org.verba.stardict.PhraseDefinitionRepository;


public interface DictionaryDefinitionsGateway {
	PhraseDefinitionRepository getDictionaryDefinitionsFor(String dictionaryName);
}
