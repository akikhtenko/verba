package org.verba.interactors;

import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

public class GetDictionarySize {
	private DictionaryMetadataGateway metadataGateway;

	public GetDictionarySize(DictionaryMetadataGateway metadataGateway) {
		this.metadataGateway = metadataGateway;
	}

	public int with(String dictionaryName) {
		DictionaryMetadata dictionaryMetadata = metadataGateway.getMetadataFor(dictionaryName);

		return dictionaryMetadata.getWordCount();
	}

}
