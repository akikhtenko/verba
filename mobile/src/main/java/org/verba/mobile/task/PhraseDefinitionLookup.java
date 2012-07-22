package org.verba.mobile.task;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.IOCase.INSENSITIVE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;
import org.verba.stardict.PhraseDefinitionRepository;

public class PhraseDefinitionLookup {
	private static final String DICTIONARY_DATAFILE_EXTENSION = "dict";
	private File rootDirectory;
	private String dictionaryName;

	public PhraseDefinitionLookup(File rootDirectory, String dictionaryName) {
		this.rootDirectory = rootDirectory;
		this.dictionaryName = dictionaryName;
	}

	public PhraseDefinition lookupPhraseDefinition(PhraseDefinitionCoordinates phraseDefinitionCoordinates)
			throws IOException {
		PhraseDefinitionRepository definitionsRepository = getPhraseDefinitionsRepository();

		PhraseDefinition phraseDefinition = null;
		try {
			phraseDefinition = definitionsRepository.find(phraseDefinitionCoordinates);
		} finally {
			definitionsRepository.destroy();
		}

		return phraseDefinition;
	}

	protected PhraseDefinitionRepository getPhraseDefinitionsRepository() throws FileNotFoundException {
		InputStream dictionaryStream = getDictionaryDataSource();
		return new PhraseDefinitionRepository(dictionaryStream);
	}

	private InputStream getDictionaryDataSource() throws FileNotFoundException {
		Collection<File> filesFound = findAllFilesMatchingDictionaryName();

		ensureAtLeastOneDataFileFound(filesFound);

		return new FileInputStream(filesFound.iterator().next());
	}

	private void ensureAtLeastOneDataFileFound(Collection<File> filesFound) {
		if (filesFound.isEmpty()) {
			throw new RuntimeException("Dictionary data file not found");
		}
	}

	private Collection<File> findAllFilesMatchingDictionaryName() {
		String dictionaryMetadataFileName = dictionaryName + "." + DICTIONARY_DATAFILE_EXTENSION;
		NameFileFilter dictionaryMetadataFileFilter = new NameFileFilter(dictionaryMetadataFileName, INSENSITIVE);
		return listFiles(rootDirectory, dictionaryMetadataFileFilter, TrueFileFilter.INSTANCE);
	}
}
