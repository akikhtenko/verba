package org.verba.stardict;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;

public class DictionaryIntegrationTest {
	private static final String WORD_TO_LOOK_FOR = "admission";
	private static final int MILLIS_IN_SECOND = 1000;

	@Test
	public void shouldLookupAWord() throws IOException, WordDefinitionCoordinatesNotFoundException {
		InputStream indexStream = getClass().getClassLoader().getResourceAsStream("org/verba/stardict/dictionary.idx");
		InputStream dictionaryStream = getClass().getClassLoader().getResourceAsStream("org/verba/stardict/dictionary.dict");
		
		DictionaryIndexReader indexReader = new DictionaryIndexReader(indexStream);
		WordDefinitionCoordinatesRepository coordinatesRepository = new WordDefinitionCoordinatesRepository(indexReader);
		
		WordDefinitionRepository definitionsRepository = new WordDefinitionRepository(dictionaryStream);
		
		Dictionary dictionary = new Dictionary(coordinatesRepository, definitionsRepository);
		long timeStarted = System.currentTimeMillis();
		
		try {
			WordDefinition wordDefinition = dictionary.lookup(WORD_TO_LOOK_FOR);
			System.out.println(String.format("%s [%s]", WORD_TO_LOOK_FOR, wordDefinition.asPlainText()));
		} finally {
			indexReader.close();
			dictionaryStream.close();
		}
		
		long spent = System.currentTimeMillis() - timeStarted;
		
		System.out.println(String.format("\nFinished lookup in %s.%s seconds", spent / MILLIS_IN_SECOND, spent % MILLIS_IN_SECOND));
	}
}
