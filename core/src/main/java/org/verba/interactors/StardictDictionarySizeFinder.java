package org.verba.interactors;

import org.verba.boundary.DictionarySizeFinder;
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

public class StardictDictionarySizeFinder implements DictionarySizeFinder {
	private DictionaryMetadataGateway metadataGateway;

	public StardictDictionarySizeFinder(DictionaryMetadataGateway metadataGateway) {
		this.metadataGateway = metadataGateway;
	}

	@Override
	public int getDictionarySize(String dictionaryName) {
		DictionaryMetadata dictionaryMetadata = metadataGateway.getMetadataFor(dictionaryName);

		return dictionaryMetadata.getWordCount();
	}

}
