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
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;
import org.verba.stardict.WordDefinitionRepository;

import android.os.Environment;

public class WordDefinitionLookup {
	public WordDefinition lookupWordDefinition(String wordToLookup) throws IOException,
			WordDefinitionCoordinatesNotFoundException {
		WordDefinitionCoordinatesRepository coordinatesRepository = getWordDefinitionCoordinatesRepository();

		WordDefinitionRepository definitionsRepository = getWordDefinitionsRepository();

		Dictionary dictionary = createDictionary(coordinatesRepository, definitionsRepository);

		WordDefinition wordDefinition = null;
		try {
			wordDefinition = dictionary.lookup(wordToLookup);
		} finally {
			coordinatesRepository.destroy();
			definitionsRepository.destroy();
		}

		return wordDefinition;
	}

	protected Dictionary createDictionary(WordDefinitionCoordinatesRepository coordinatesRepository,
			WordDefinitionRepository definitionsRepository) {
		return new Dictionary(coordinatesRepository, definitionsRepository);
	}

	protected WordDefinitionCoordinatesRepository getWordDefinitionCoordinatesRepository() throws FileNotFoundException {
		DictionaryIndexReader indexReader = getIndexReader();
		return new WordDefinitionCoordinatesRepository(indexReader);
	}

	protected WordDefinitionRepository getWordDefinitionsRepository() throws FileNotFoundException {
		InputStream dictionaryStream = getDictionaryInputStream();
		return new WordDefinitionRepository(dictionaryStream);
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

	protected File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}
}
