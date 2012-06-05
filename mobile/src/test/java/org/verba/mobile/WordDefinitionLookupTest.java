package org.verba.mobile;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.stardict.Dictionary;
import org.verba.stardict.WordDefinitionCoordinatesRepository;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;
import org.verba.stardict.WordDefinitionRepository;

@RunWith(MockitoJUnitRunner.class)
public class WordDefinitionLookupTest {
	private static final String DUMMY_WORD = "dummy-word";
	@Spy
	private WordDefinitionLookup wordDefinitionLookup;
	@Mock
	private WordDefinitionCoordinatesRepository mockedCoordinatesRepository;
	@Mock
	private WordDefinitionRepository mockedDefinitionRepository;
	@Mock
	private Dictionary mockedDictionary;

	@Before
	public void setupMocks() throws FileNotFoundException {
		doReturn(mockedCoordinatesRepository).when(wordDefinitionLookup).getWordDefinitionCoordinatesRepository();
		doReturn(mockedDefinitionRepository).when(wordDefinitionLookup).getWordDefinitionsRepository();
		doReturn(mockedDictionary).when(wordDefinitionLookup).createDictionary(mockedCoordinatesRepository,
				mockedDefinitionRepository);
	}

	@Test
	public void shouldLookupWord() throws IOException, WordDefinitionCoordinatesNotFoundException {
		wordDefinitionLookup.lookupWordDefinition(DUMMY_WORD);

		verify(mockedDictionary).lookup(DUMMY_WORD);
	}

	@Test(expected = WordDefinitionCoordinatesNotFoundException.class)
	public void shouldRethrowWhenLookupEndWithException() throws IOException,
			WordDefinitionCoordinatesNotFoundException {
		doThrow(WordDefinitionCoordinatesNotFoundException.class).when(mockedDictionary).lookup(DUMMY_WORD);

		wordDefinitionLookup.lookupWordDefinition(DUMMY_WORD);
	}

	@Test
	public void shouldCleanupAfetrWordLookup() throws IOException, WordDefinitionCoordinatesNotFoundException {
		wordDefinitionLookup.lookupWordDefinition(DUMMY_WORD);

		verify(mockedCoordinatesRepository).destroy();
		verify(mockedDefinitionRepository).destroy();
	}
}
