package org.verba.stardict;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
import org.verba.xdxf.XdxfPhraseDefinitionElement;

public class DictionaryIntegrationTest {
	private static final String WORD_TO_LOOK_FOR = "admission";
	private static final int MILLIS_IN_SECOND = 1000;

	@Test
	@Ignore
	public void shouldLookupAWord() throws IOException, PhraseDefinitionCoordinatesNotFoundException, URISyntaxException {
		URL indexUrl = getClass().getClassLoader().getResource("org/verba/stardict/dictionary.idx");
		File indexFile = new File(indexUrl.toURI());
		InputStream dictionaryStream = getClass().getClassLoader().getResourceAsStream(
				"org/verba/stardict/dictionary.dict");

		PhraseDefinitionCoordinatesRepository coordinatesRepository = new FsPhraseDefinitionCoordinatesRepository(indexFile);

		PhraseDefinitionRepository definitionsRepository = new PhraseDefinitionRepository(dictionaryStream);

		Dictionary dictionary = new Dictionary(coordinatesRepository, definitionsRepository);
		long timeStarted = System.currentTimeMillis();

		try {
			PhraseDefinition phraseDefinition = dictionary.lookup(WORD_TO_LOOK_FOR);
			XdxfPhraseDefinitionElement phraseDefinitionPart = (XdxfPhraseDefinitionElement) phraseDefinition.parts()
					.next().elements().next();

			System.out.println(String.format("%s [%s]", WORD_TO_LOOK_FOR, new String(phraseDefinitionPart.bytes())));
		} finally {
			definitionsRepository.close();
		}

		long spent = System.currentTimeMillis() - timeStarted;

		System.out.println(String.format("\nFinished lookup in %s.%s seconds", spent / MILLIS_IN_SECOND, spent
				% MILLIS_IN_SECOND));
	}
}
