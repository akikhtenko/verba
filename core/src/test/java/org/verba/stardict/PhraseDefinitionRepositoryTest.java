package org.verba.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

@RunWith(MockitoJUnitRunner.class)
public class PhraseDefinitionRepositoryTest {
	private static final String WORD_DEFINITION = "word definition 1";
	private static final String DICTIONARY_CONTENT =
			"some_rubbish###" + WORD_DEFINITION + "###another_piece_of_rubbish";
	private static final String ADJJUSTED_WORD_DEFINITION = "<ar>" + WORD_DEFINITION + "</ar>";
	private static final String CORRUPTED_DICTIONARY_CONTENT = "some_rubbish###word defini";

	@Mock
	private PhraseDefinitionCoordinates phraseCoordinates;
	@Mock
	private InputStream inputStream;

	@Before
	public void prepareMocks() {
		when(phraseCoordinates.getPhraseDefinitionOffset()).thenReturn(15L);
		when(phraseCoordinates.getPhraseDefinitionLength()).thenReturn(17);
	}

	@Test
	public void shouldGetPhraseDefinitionByPhraseDefinitionCoordinates() throws IOException {
		PhraseDefinitionRepository wordsRepository = new PhraseDefinitionRepository(new ByteArrayInputStream(
				DICTIONARY_CONTENT.getBytes()));
		PhraseDefinition phraseDefinition = wordsRepository.find(phraseCoordinates);
		XdxfPhraseDefinitionElement phraseDefinitionPart = (XdxfPhraseDefinitionElement) phraseDefinition.parts()
				.next().elements().next();
		assertThat(new String(phraseDefinitionPart.bytes()), is(ADJJUSTED_WORD_DEFINITION));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenOffsetIsGreaterThanDictionaryLength() throws IOException {
		PhraseDefinitionRepository wordsRepository = new PhraseDefinitionRepository(new ByteArrayInputStream("".getBytes()));
		wordsRepository.find(phraseCoordinates);
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileLookingForPhraseDefinition() throws IOException {
		PhraseDefinitionRepository wordsRepository = new PhraseDefinitionRepository(new ByteArrayInputStream(
				CORRUPTED_DICTIONARY_CONTENT.getBytes()));
		wordsRepository.find(phraseCoordinates);
	}

	@Test
	public void shouldDestroyPhraseDefinitionRepository() throws IOException {
		new PhraseDefinitionRepository(inputStream).close();
		verify(inputStream).close();
	}
}
