package org.verba.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.verba.stardict.Dictionary;
import org.verba.stardict.DictionaryIndexReader;
import org.verba.stardict.WordDefinition;
import org.verba.stardict.WordDefinitionCoordinatesRepository;
import org.verba.stardict.WordDefinitionRepository;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;

import android.os.Environment;

public class WordDefinitionLookup {
	public WordDefinition lookupWordDefinition(String wordToLookup) throws IOException {
		InputStream dictionaryStream = getDictionaryInputStream();
		DictionaryIndexReader indexReader = getIndexReader();
		WordDefinitionCoordinatesRepository coordinatesRepository = new WordDefinitionCoordinatesRepository(
				indexReader);

		WordDefinitionRepository definitionsRepository = new WordDefinitionRepository(dictionaryStream);

		Dictionary dictionary = new Dictionary(coordinatesRepository, definitionsRepository);

		WordDefinition wordDefinition = null;
		try {
			wordDefinition = dictionary.lookup(wordToLookup);
		} catch (WordDefinitionCoordinatesNotFoundException e) {
			return null;
		} finally {
			indexReader.close();
			dictionaryStream.close();
		}

		return wordDefinition;
	}

	private File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}

	private InputStream getDictionaryInputStream() throws FileNotFoundException {
		File dictionaryFile = new File(getVerbaDirectory(), "dictionary.dict");
		return new FileInputStream(dictionaryFile);
	}

	private DictionaryIndexReader getIndexReader() throws FileNotFoundException {
		File indexFile = new File(getVerbaDirectory(), "dictionary.idx");
		InputStream indexStream = new FileInputStream(indexFile);

		return new DictionaryIndexReader(indexStream);
	}
}
