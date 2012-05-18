package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class WordDefinitionCoordinatesRepositoryTest {
	private static final String WORD_TO_LOOK_FOR = "verba";
	@Mock
	private DictionaryIndexReader mockedIndexReader;
	@Mock
	private WordDefinitionCoordinates mockedCoordinates;

	private WordDefinitionCoordinatesRepository index;

	@Before
	public void prepareWordDefinitionCoordinatesRepository() {
		index = new WordDefinitionCoordinatesRepository(mockedIndexReader);
	}

	@Test
	public void shouldLookupDefinitionCoordinatesForAWord() throws IOException,
			WordDefinitionCoordinatesNotFoundException {
		givenThreeWordDefinitionCoordinates();

		WordDefinitionCoordinates wordDefinitionCoordinates = index.find(WORD_TO_LOOK_FOR);

		verify(mockedIndexReader, times(2)).hasNextWordDefinition();
		verify(mockedIndexReader, times(2)).readWordCoordinates();
		assertThat(wordDefinitionCoordinates, is(mockedCoordinates));
	}

	@Test(expected = WordDefinitionCoordinatesNotFoundException.class)
	public void shouldFailWhenCoordinatesNotFOundInDictionaryIndex() throws IOException,
			WordDefinitionCoordinatesNotFoundException {
		givenIndexWithoutAWordToBeFound();

		index.find(WORD_TO_LOOK_FOR);
	}

	@Test
	public void shouldDestroyDisctionaryIndex() throws IOException {
		index.destroy();
		verify(mockedIndexReader).close();
	}

	private void givenIndexWithoutAWordToBeFound() throws IOException {
		when(mockedIndexReader.hasNextWordDefinition()).thenReturn(true, false);
		when(mockedIndexReader.readWordCoordinates()).thenReturn(mockedCoordinates);
		when(mockedCoordinates.matches(WORD_TO_LOOK_FOR)).thenReturn(false);
	}

	private void givenThreeWordDefinitionCoordinates() throws IOException {
		when(mockedIndexReader.hasNextWordDefinition()).thenReturn(true, true, true, false);
		when(mockedIndexReader.readWordCoordinates()).thenReturn(mockedCoordinates);
		when(mockedCoordinates.matches(WORD_TO_LOOK_FOR)).thenReturn(false, true);
	}
}
