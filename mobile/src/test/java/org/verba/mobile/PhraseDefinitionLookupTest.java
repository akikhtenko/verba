package org.verba.mobile;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.stardict.PhraseDefinitionCoordinates;
import org.verba.stardict.PhraseDefinitionRepository;

@RunWith(MockitoJUnitRunner.class)
public class PhraseDefinitionLookupTest {
	private static final int WORD_DEFINITION_LENGTH = 456;
	private static final long WORD_DEFINITION_OFFSET = 123L;
	private static final String TARGET_WORD = "word";
	@Spy
	private PhraseDefinitionLookup phraseDefinitionLookup;
	@Mock
	private PhraseDefinitionRepository mockedDefinitionRepository;
	//CHECKSTYLE:OFF
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	//CHECKSTYLE:ON

	private PhraseDefinitionCoordinates phraseDefinitionCoordinates;

	@Before
	public void initialize() throws FileNotFoundException {
		doReturn(mockedDefinitionRepository).when(phraseDefinitionLookup).getPhraseDefinitionsRepository();
		phraseDefinitionCoordinates = new PhraseDefinitionCoordinates(TARGET_WORD, WORD_DEFINITION_OFFSET,
				WORD_DEFINITION_LENGTH);
	}

	@Test
	public void shouldLookupWord() throws IOException {
		phraseDefinitionLookup.lookupPhraseDefinition(phraseDefinitionCoordinates);

		verify(mockedDefinitionRepository).find(phraseDefinitionCoordinates);
	}

	@Test
	public void shouldCleanupAfetrWordLookup() throws IOException {
		doThrow(IOException.class).when(mockedDefinitionRepository).find(phraseDefinitionCoordinates);
		thrown.expect(IOException.class);
		phraseDefinitionLookup.lookupPhraseDefinition(phraseDefinitionCoordinates);

		verify(mockedDefinitionRepository).destroy();
	}
}
