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
import org.verba.xdxf.XdxfWordDefinitionPart;

@RunWith(MockitoJUnitRunner.class)
public class WordDefinitionRepositoryTest {
	private static final String WORD_DEFINITION = "word definition 1";
	private static final String DICTIONARY_CONTENT =
			"some_rubbish###" + WORD_DEFINITION + "###another_piece_of_rubbish";
	private static final String ADJJUSTED_WORD_DEFINITION = "<ar>" + WORD_DEFINITION + "</ar>";
	private static final String CORRUPTED_DICTIONARY_CONTENT = "some_rubbish###word defini";

	@Mock
	private WordDefinitionCoordinates mockedWordCoordinates;
	@Mock
	private InputStream mockedInputStream;

	@Before
	public void prepareMocks() {
		when(mockedWordCoordinates.getWordDefinitionOffset()).thenReturn(15L);
		when(mockedWordCoordinates.getWordDefinitionLength()).thenReturn(17);
	}

	@Test
	public void shouldGetWordDefinitionByWordDefinitionCoordinates() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream(
				DICTIONARY_CONTENT.getBytes()));
		WordDefinition wordDefinition = wordsRepository.find(mockedWordCoordinates);
		XdxfWordDefinitionPart wordDefinitionPart = (XdxfWordDefinitionPart) wordDefinition.iterator().next();
		assertThat(wordDefinitionPart.asPlainText(), is(ADJJUSTED_WORD_DEFINITION));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenOffsetIsGreaterThanDictionaryLength() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream("".getBytes()));
		wordsRepository.find(mockedWordCoordinates);
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileLookingForWordDefinition() throws IOException {
		WordDefinitionRepository wordsRepository = new WordDefinitionRepository(new ByteArrayInputStream(
				CORRUPTED_DICTIONARY_CONTENT.getBytes()));
		wordsRepository.find(mockedWordCoordinates);
	}

	@Test
	public void shouldDestroyWordDefinitionRepository() throws IOException {
		new WordDefinitionRepository(mockedInputStream).destroy();
		verify(mockedInputStream).close();
	}
}
