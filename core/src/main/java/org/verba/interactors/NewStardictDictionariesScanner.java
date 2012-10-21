package org.verba.interactors;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.verba.stardict.metadata.StardictDictionaryMetadataGateway.DICTIONARY_METADATA_FILE_EXTENSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.verba.DictionaryRepository;
import org.verba.boundary.NewDictionariesScanner;

public class NewStardictDictionariesScanner implements NewDictionariesScanner {
	private File rootDirectory;
	private DictionaryRepository dictionaryRepository;

	public NewStardictDictionariesScanner(File rootDirectory, DictionaryRepository dictionaryRepository) {
		this.rootDirectory = rootDirectory;
		this.dictionaryRepository = dictionaryRepository;
	}

	@Override
	public ArrayList<String> findNewDictionaries() {
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
