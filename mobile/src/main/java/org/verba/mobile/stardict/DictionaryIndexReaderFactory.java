package org.verba.mobile.stardict;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.IOCase.INSENSITIVE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.verba.stardict.index.DictionaryIndex;

/**
 * @deprecated To be removed ASAP
 *
 */
@Deprecated()
public class DictionaryIndexReaderFactory {
	private static final String DICTIONARY_INDEXFILE_EXTENSION = "idx";

	private File baseDirectory;
	private String dictionaryName;

	public DictionaryIndexReaderFactory(File baseDirectory, String dictionaryName) {
		this.baseDirectory = baseDirectory;
		this.dictionaryName = dictionaryName;
	}

	public DictionaryIndex createIndexReader() {
		try {
			return new DictionaryIndex(getDictionaryDataSource());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private InputStream getDictionaryDataSource() throws FileNotFoundException {
		Collection<File> filesFound = findAllFilesMatchingDictionaryName();

		ensureAtLeastOneIndexFileFound(filesFound);

		return new FileInputStream(filesFound.iterator().next());
	}

	private void ensureAtLeastOneIndexFileFound(Collection<File> filesFound) {
		if (filesFound.isEmpty()) {
			throw new RuntimeException("Dictionary index file not found");
		}
	}

	private Collection<File> findAllFilesMatchingDictionaryName() {
		String dictionaryMetadataFileName = dictionaryName + "." + DICTIONARY_INDEXFILE_EXTENSION;
		NameFileFilter dictionaryMetadataFileFilter = new NameFileFilter(dictionaryMetadataFileName, INSENSITIVE);
		return listFiles(baseDirectory, dictionaryMetadataFileFilter, TrueFileFilter.INSTANCE);
	}
}
