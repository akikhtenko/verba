package org.verba.interactors;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.verba.stardict.metadata.StardictDictionaryMetadataGateway.DICTIONARY_METADATA_FILE_EXTENSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.verba.DictionaryRepository;

public class GetNewDictionaries {
	private File rootDirectory;
	private DictionaryRepository dictionaryRepository;

	public GetNewDictionaries(File rootDirectory, DictionaryRepository dictionaryRepository) {
		this.rootDirectory = rootDirectory;
		this.dictionaryRepository = dictionaryRepository;
	}

	public ArrayList<String> all() {
		ArrayList<String> newDictionaries = new ArrayList<String>();
		for (File metadataFile : getAllMetadataFiles()) {
			String dictionaryName = getBaseName(metadataFile.getName());
			if (isNewDictionary(getBaseName(dictionaryName))) {
				newDictionaries.add(dictionaryName);
			}
		}
		return newDictionaries;
	}

	private boolean isNewDictionary(String baseName) {
		return !dictionaryRepository.exists(baseName); // TODO: && !dictionaryRepository.skipped(baseName);
	}

	private Collection<File> getAllMetadataFiles() {
		return listFiles(rootDirectory, new String[] { DICTIONARY_METADATA_FILE_EXTENSION }, true);
	}
}
