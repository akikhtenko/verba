package org.verba.stardict.index;

import org.verba.util.DictionaryFileFinder;

public class StardictDictionaryIndexGateway implements DictionaryIndexGateway {
	private static final String DICTIONARY_INDEX_FILE_EXTENSION = "idx";
	private DictionaryFileFinder dictionaryFileFinder;

	public StardictDictionaryIndexGateway(DictionaryFileFinder dictionaryFileFinder) {
		this.dictionaryFileFinder = dictionaryFileFinder;
	}

	@Override
	public DictionaryIndex getDictionaryIndexFor(String dictionaryName) {
		return new DictionaryIndex(dictionaryFileFinder.getDictionaryFileStream(dictionaryName,
				DICTIONARY_INDEX_FILE_EXTENSION));
	}
}
