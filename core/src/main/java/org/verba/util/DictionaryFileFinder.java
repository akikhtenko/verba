package org.verba.util;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.IOCase.INSENSITIVE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class DictionaryFileFinder {
	private File baseDirectory;

	public DictionaryFileFinder(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public InputStream getDictionaryFileStream(String dictionaryName, String fileExtension) {
		Collection<File> filesFound = findAllFilesMatchingDictionaryName(dictionaryName + "." + fileExtension);

		ensureAtLeastOneSuchDictionaryFileFound(filesFound);

		try {
			return new FileInputStream(filesFound.iterator().next());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void ensureAtLeastOneSuchDictionaryFileFound(Collection<File> filesFound) {
		if (filesFound.isEmpty()) {
			throw new RuntimeException("Requested dictionary file not found");
		}
	}

	private Collection<File> findAllFilesMatchingDictionaryName(String fileName) {
		NameFileFilter dictionaryFileFilter = new NameFileFilter(fileName, INSENSITIVE);
		return listFiles(baseDirectory, dictionaryFileFilter, TrueFileFilter.INSTANCE);
	}
}
