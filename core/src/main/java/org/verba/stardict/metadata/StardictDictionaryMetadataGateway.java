package org.verba.stardict.metadata;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.InputStream;

import org.verba.util.DictionaryFileFinder;

public class StardictDictionaryMetadataGateway implements DictionaryMetadataGateway {
	public static final String DICTIONARY_METADATA_FILE_EXTENSION = "ifo";
	private DictionaryFileFinder dictionaryFileFinder;

	public StardictDictionaryMetadataGateway(DictionaryFileFinder dictionaryFileFinder) {
		this.dictionaryFileFinder = dictionaryFileFinder;
	}

	@Override
	public DictionaryMetadata getMetadataFor(String dictionaryName) {
		InputStream dictionaryMetadataSource =
				dictionaryFileFinder.getDictionaryFileStream(dictionaryName, DICTIONARY_METADATA_FILE_EXTENSION);
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMetadataSource);
		try {
			return dictionaryMetadataReader.read();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeQuietly(dictionaryMetadataSource);
		}
	}

}
