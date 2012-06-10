package org.verba.stardict;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Test;

public class DictionaryIndexReaderIntegrationTest {
	private static final int MILLIS_IN_SECOND = 1000;

	@Test
	@Ignore
	public void shouldParseDictionaryIndexFile() throws IOException {
		InputStream indexStream = getClass().getClassLoader().getResourceAsStream("org/verba/stardict/dictionary.idx");

		DictionaryIndexReader indexReader = new DictionaryIndexReader(new BufferedInputStream(indexStream));
		long timeStarted = System.currentTimeMillis();

		try {
			while (indexReader.hasNextPhraseDefinitionCoordinates()) {
				PhraseDefinitionCoordinates wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
				System.out.println(String.format("%s [%s,%s]", wordCoordinates.getTargetPhrase(),
						wordCoordinates.getPhraseDefinitionOffset(), wordCoordinates.getPhraseDefinitionLength()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			indexReader.close();
		}

		long spent = System.currentTimeMillis() - timeStarted;

		System.out.println(String.format("Finished reading index file in %s.%s seconds", spent / MILLIS_IN_SECOND,
				spent % MILLIS_IN_SECOND));
	}
}
