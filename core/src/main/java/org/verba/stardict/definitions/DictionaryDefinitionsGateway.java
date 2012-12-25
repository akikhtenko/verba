package org.verba.stardict.definitions;

import org.verba.stardict.PhraseDefinitionRepository;
import org.verba.stardict.metadata.DictionaryMetadata;


public interface DictionaryDefinitionsGateway {
	PhraseDefinitionRepository getDictionaryDefinitionsFor(String dictionaryName, DictionaryMetadata dictionaryMetadata);
}
