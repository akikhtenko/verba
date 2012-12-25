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
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

@RunWith(MockitoJUnitRunner.class)
public class PhraseDefinitionRepositoryTest {
	private static final int PHRASE_DEFINITION_LENGTH = 17;
	private static final long PHRASE_DEFINITION_OFFSET = 15L;
	private static final String WORD_DEFINITION = "word definition 1";
	private static final String DICTIONARY_CONTENT = "some_rubbish###" + WORD_DEFINITION + "###another_piece_of_rubbish";
	private static final String ADJUSTED_WORD_DEFINITION = "<ar>" + WORD_DEFINITION + "</ar>";
	private static final String CORRUPTED_DICTIONARY_CONTENT = "some_rubbish###word defini";
	private static final DictionaryMetadata UNIMPORTANT_METADATA = null;

	@Mock
	private PhraseDefinitionCoordinates phraseCoordinates;
	@Mock
	private InputStream inputStream;

	@Before
	public void prepareMocks() {
		when(phraseCoordinates.getPhraseDefinitionOffset()).thenReturn(PHRASE_DEFINITION_OFFSET);
		when(phraseCoordinates.getPhraseDefinitionLength()).thenReturn(PHRASE_DEFINITION_LENGTH);
	}

	@Test
	public void shouldGetPhraseDefinitionByPhraseDefinitionCoordinates() throws IOException {
		PhraseDefinitionRepository wordsRepository =
				new PhraseDefinitionRepository(new ByteArrayInputStream(DICTIONARY_CONTENT.getBytes()), UNIMPORTANT_METADATA);
		PhraseDefinition phraseDefinition = wordsRepository.find(phraseCoordinates);
		XdxfPhraseDefinitionElement phraseDefinitionElement = (XdxfPhraseDefinitionElement) phraseDefinition.elements().next();
		assertThat(new String(phraseDefinitionElement.bytes()), is(ADJUSTED_WORD_DEFINITION));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenOffsetIsGreaterThanDictionaryLength() throws IOException {
		PhraseDefinitionRepository wordsRepository =
				new PhraseDefinitionRepository(new ByteArrayInputStream("".getBytes()), UNIMPORTANT_METADATA);
		wordsRepository.find(phraseCoordinates);
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileLookingForPhraseDefinition() throws IOException {
		PhraseDefinitionRepository wordsRepository =
				new PhraseDefinitionRepository(new ByteArrayInputStream(CORRUPTED_DICTIONARY_CONTENT.getBytes()), UNIMPORTANT_METADATA);
		wordsRepository.find(phraseCoordinates);
	}

	@Test
	public void shouldDestroyPhraseDefinitionRepository() throws IOException {
		new PhraseDefinitionRepository(inputStream, UNIMPORTANT_METADATA).close();
		verify(inputStream).close();
	}
}
