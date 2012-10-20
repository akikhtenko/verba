package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.stardict.index.DictionaryIndex;

@RunWith(MockitoJUnitRunner.class)
public class PhraseDefinitionCoordinatesRepositoryTest {
	private static final String WORD_TO_LOOK_FOR = "verba";
	@Mock
	private DictionaryIndex indexReader;
	@Mock
	private PhraseDefinitionCoordinates phraseCoordinates;

	private FsPhraseDefinitionCoordinatesRepository index;

	@Before
	public void preparePhraseDefinitionCoordinatesRepository() {
		index = spy(new FsPhraseDefinitionCoordinatesRepository(null));
		doReturn(indexReader).when(index).getIndexReader();
	}

	@Test
	public void shouldLookupDefinitionCoordinatesForAWord() throws IOException,
			PhraseDefinitionCoordinatesNotFoundException {
		givenThreePhraseDefinitionCoordinates();

		PhraseDefinitionCoordinates phraseDefinitionCoordinates = index.find(WORD_TO_LOOK_FOR);

		verify(indexReader, times(2)).hasNextPhraseDefinitionCoordinates();
		verify(indexReader, times(2)).readPhraseDefinitionCoordinates();
		assertThat(phraseDefinitionCoordinates, is(phraseCoordinates));
	}

	@Test(expected = PhraseDefinitionCoordinatesNotFoundException.class)
	public void shouldFailWhenCoordinatesNotFOundInDictionaryIndex() throws IOException,
			PhraseDefinitionCoordinatesNotFoundException {
		givenIndexWithoutAWordToBeFound();

		index.find(WORD_TO_LOOK_FOR);
	}

	private void givenIndexWithoutAWordToBeFound() throws IOException {
		when(indexReader.hasNextPhraseDefinitionCoordinates()).thenReturn(true, false);
		when(indexReader.readPhraseDefinitionCoordinates()).thenReturn(phraseCoordinates);
		when(phraseCoordinates.matches(WORD_TO_LOOK_FOR)).thenReturn(false);
	}

	private void givenThreePhraseDefinitionCoordinates() throws IOException {
		when(indexReader.hasNextPhraseDefinitionCoordinates()).thenReturn(true, true, true, false);
		when(indexReader.readPhraseDefinitionCoordinates()).thenReturn(phraseCoordinates);
		when(phraseCoordinates.matches(WORD_TO_LOOK_FOR)).thenReturn(false, true);
	}
}
