package org.verba.stardict;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class DictionaryIndexReaderIntegrationTest {
	private static final int MILLIS_IN_SECOND = 1000;

	@Test
	public void shouldParseDictionaryIndexFile() throws IOException {
		InputStream indexStream = getClass().getClassLoader().getResourceAsStream("org/verba/stardict/dictionary_index.idx");
		
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new BufferedInputStream(indexStream));
		long timeStarted = System.currentTimeMillis();
		
		try {
			while(indexReader.hasNextWordDefinition()) {
				WordCoordinates wordCoordinates = indexReader.readWordCoordinates();
				System.out.println(String.format("%s [%s,%s]", wordCoordinates.getTargetWord(), wordCoordinates.getWordDefinitionOffset(), wordCoordinates.getWordDefinitionLength()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (indexStream != null) {
				indexStream.close();
			}
		}
		
		long spent = System.currentTimeMillis() - timeStarted;
		
		System.out.println(String.format("Finished reading index file in %s.%s seconds", spent / MILLIS_IN_SECOND, spent % MILLIS_IN_SECOND));
	}
}
