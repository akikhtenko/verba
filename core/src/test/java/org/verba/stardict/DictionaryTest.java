package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
public class DictionaryTest {
	private static final String WORD_TO_LOOK_FOR = "verba";
	@Mock
	private WordDefinitionCoordinatesRepository mockedWordDefinitionCoordinatesRepository;
	@Mock
	private WordDefinitionRepository mockedWordDefinitionRepository;
	@Mock
	private WordDefinitionCoordinates mockedCoordinates;
	@Mock
	private WordDefinition mockedWordDefinition;
	
	private Dictionary dictionary;
	
	@Before
	public void prepareDictionary() {
		dictionary = new Dictionary(mockedWordDefinitionCoordinatesRepository, mockedWordDefinitionRepository);
	}
	
	@Test
	public void shouldLookupAWord() throws IOException, WordDefinitionCoordinatesNotFoundException {
		when(mockedWordDefinitionCoordinatesRepository.find(WORD_TO_LOOK_FOR)).thenReturn(mockedCoordinates);
		when(mockedWordDefinitionRepository.find(mockedCoordinates)).thenReturn(mockedWordDefinition);
		
		WordDefinition wordDefinition = dictionary.lookup(WORD_TO_LOOK_FOR);
		
		verify(mockedWordDefinitionCoordinatesRepository).find(WORD_TO_LOOK_FOR);
		verify(mockedWordDefinitionRepository).find(mockedCoordinates);
		assertThat(wordDefinition, is(mockedWordDefinition));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void shouldFailIfProblemWhenLookingForWordDefinitionCoordinatesInRepository() throws IOException, WordDefinitionCoordinatesNotFoundException {
		when(mockedWordDefinitionCoordinatesRepository.find(WORD_TO_LOOK_FOR)).thenThrow(IOException.class);
		dictionary.lookup(WORD_TO_LOOK_FOR);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void shouldFailIfProblemWhenLookingForWordDefinitionInRepository() throws IOException, WordDefinitionCoordinatesNotFoundException {
		when(mockedWordDefinitionCoordinatesRepository.find(WORD_TO_LOOK_FOR)).thenReturn(mockedCoordinates);
		when(mockedWordDefinitionRepository.find(mockedCoordinates)).thenThrow(IOException.class);
		dictionary.lookup(WORD_TO_LOOK_FOR);
	}
}
