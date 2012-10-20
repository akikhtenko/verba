package org.verba.mobile.dictionary;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.IOCase.INSENSITIVE;
import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataReader;

/**
 * @deprecated to be removed ASAP
 */
@Deprecated
public class DictionariesManager {
	private static final String DICTIONARY_METADATA_EXTENSION = "ifo";
	private File rootDirectory;
	private DictionaryDao dictionaryDao;

	public DictionariesManager(File rootDirectory, DictionaryDao dictionaryDao) {
		this.rootDirectory = rootDirectory;
		this.dictionaryDao = dictionaryDao;
	}

	public ArrayList<String> findNewDictionaries() {
		ArrayList<String> newDictionaries = new ArrayList<String>();
		for (File metadataFile : getAllMetadataFiles()) {
			String dictionaryName = getBaseName(metadataFile.getName());
			if (isNewDIctionary(getBaseName(dictionaryName))) {
				newDictionaries.add(dictionaryName);
			}
		}
		return newDictionaries;
	}

	public DictionaryMetadata lookupDictionaryMetadata(String dictionaryName) throws DictionaryLookupException {
		InputStream dictionaryMetadataSource = getDictionaryMetadataSource(dictionaryName);
		DictionaryMetadataReader dictionaryMetadataReader = new DictionaryMetadataReader(dictionaryMetadataSource);
		try {
			return dictionaryMetadataReader.read();
		} catch (Exception e) {
			throw new DictionaryLookupException(e);
		} finally {
			closeQuietly(dictionaryMetadataSource);
		}

	}

	private boolean isNewDIctionary(String baseName) {
		return !dictionaryDao.dictionaryExists(baseName);
	}

	private Collection<File> getAllMetadataFiles() {
		return listFiles(rootDirectory, new String[] { DICTIONARY_METADATA_EXTENSION }, true);
	}

	private InputStream getDictionaryMetadataSource(String dictionaryName) throws DictionaryLookupException {
		Collection<File> filesFound = findAllFilesMatchingDictionaryName(dictionaryName);

		ensureAtLeastOneMetadataFileFound(filesFound);

		try {
			return new FileInputStream(filesFound.iterator().next());
		} catch (FileNotFoundException e) {
			throw new DictionaryLookupException(e);
		}
	}

	private void ensureAtLeastOneMetadataFileFound(Collection<File> filesFound) {
		if (filesFound.isEmpty()) {
			throw new RuntimeException("Dictionary metadata file not found");
		}
	}

	private Collection<File> findAllFilesMatchingDictionaryName(String dictionaryName) {
		String dictionaryMetadataFileName = dictionaryName + "." + DICTIONARY_METADATA_EXTENSION;
		NameFileFilter dictionaryMetadataFileFilter = new NameFileFilter(dictionaryMetadataFileName, INSENSITIVE);
		return listFiles(rootDirectory, dictionaryMetadataFileFilter, TrueFileFilter.INSTANCE);
	}
}
