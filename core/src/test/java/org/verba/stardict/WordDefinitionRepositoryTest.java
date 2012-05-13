package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WordDefinitionRepositoryTest {
	private static final String WORD_DEFINITION = "word definition 1";
	private static final String DICTIONARY_CONTENT = "some_rubbish###" + WORD_DEFINITION + "###another_piece_of_rubbish";
	private static final String CORRUPTED_DICTIONARY_CONTENT = "some_rubbish###word defini";
	
	@Mock
	private WordDefinitionCoordinates mockedWordCoordinates;
	
	@Before
	public void prepareMocks() {
		when(mockedWordCoordinates.getWordDefinitionOffset()).thenReturn(15L);
		when(mockedWordCoordinates.getWordDefinitionLength()).thenReturn(17);
	}
	
	@Test
	public void shouldGetWordDefinitionByWordDefinitionCoordinates() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream(DICTIONARY_CONTENT.getBytes()));
		WordDefinition wordDefinition = wordsRepository.find(mockedWordCoordinates);
		assertThat(wordDefinition.asPlainText(), is(WORD_DEFINITION));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenOffsetIsGreaterThanDictionaryLength() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream("".getBytes()));
		wordsRepository.find(mockedWordCoordinates);
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileLookingForWordDefinition() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream(CORRUPTED_DICTIONARY_CONTENT.getBytes()));
		wordsRepository.find(mockedWordCoordinates);
	}
}
