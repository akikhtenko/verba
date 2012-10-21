package org.verba.stardict.definitions;

import org.verba.stardict.PhraseDefinitionRepository;
import org.verba.util.DictionaryFileFinder;

public class StardictDictionaryDefinitionsGateway implements DictionaryDefinitionsGateway {
	private static final String DICTIONARY_DATA_FILE_EXTENSION = "dict";
	private DictionaryFileFinder dictionaryFileFinder;

	public StardictDictionaryDefinitionsGateway(DictionaryFileFinder dictionaryFileFinder) {
		this.dictionaryFileFinder = dictionaryFileFinder;
	}

	@Override
	public PhraseDefinitionRepository getDictionaryDefinitionsFor(String dictionaryName) {
		return new PhraseDefinitionRepository(dictionaryFileFinder.getDictionaryFileStream(dictionaryName,
				DICTIONARY_DATA_FILE_EXTENSION));
	}
}
