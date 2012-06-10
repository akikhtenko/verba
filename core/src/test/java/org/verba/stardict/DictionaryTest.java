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

@RunWith(MockitoJUnitRunner.class)
public class DictionaryTest {
	private static final String PHRASE_TO_LOOK_FOR = "verba";
	@Mock
	private FsPhraseDefinitionCoordinatesRepository phraseDefinitionCoordinatesRepository;
	@Mock
	private PhraseDefinitionRepository phraseDefinitionRepository;
	@Mock
	private PhraseDefinitionCoordinates phraseCoordinates;
	@Mock
	private PhraseDefinition phraseDefinition;

	private Dictionary dictionary;

	@Before
	public void prepareDictionary() {
		dictionary = new Dictionary(phraseDefinitionCoordinatesRepository, phraseDefinitionRepository);
	}

	@Test
	public void shouldLookupAWord() throws IOException, PhraseDefinitionCoordinatesNotFoundException {
		when(phraseDefinitionCoordinatesRepository.find(PHRASE_TO_LOOK_FOR)).thenReturn(phraseCoordinates);
		when(phraseDefinitionRepository.find(phraseCoordinates)).thenReturn(phraseDefinition);

		PhraseDefinition actualPhraseDefinition = dictionary.lookup(PHRASE_TO_LOOK_FOR);

		verify(phraseDefinitionCoordinatesRepository).find(PHRASE_TO_LOOK_FOR);
		verify(phraseDefinitionRepository).find(phraseCoordinates);
		assertThat(actualPhraseDefinition, is(phraseDefinition));
	}
}
