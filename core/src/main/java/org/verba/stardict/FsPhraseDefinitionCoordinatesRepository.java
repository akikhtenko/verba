package org.verba.stardict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class FsPhraseDefinitionCoordinatesRepository implements PhraseDefinitionCoordinatesRepository {
	private File indexFile;

	public FsPhraseDefinitionCoordinatesRepository(File indexFile) {
		this.indexFile = indexFile;
	}

	@Override
	public PhraseDefinitionCoordinates find(String targetWord) throws PhraseDefinitionCoordinatesNotFoundException {
		DictionaryIndexReader indexReader = getIndexReader();
		try {
			PhraseDefinitionCoordinates phraseDefinitionCoordinates = getPhraseDefinitionCoordinates(targetWord, indexReader);
			ensurePhraseDefinitionCoordinatesFound(phraseDefinitionCoordinates, targetWord);

			return phraseDefinitionCoordinates;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(indexReader);
		}
	}

	protected DictionaryIndexReader getIndexReader() {
		try {
			InputStream indexStream = new FileInputStream(indexFile);
			return new DictionaryIndexReader(indexStream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private PhraseDefinitionCoordinates getPhraseDefinitionCoordinates(String targetWord,
			DictionaryIndexReader indexReader) throws IOException {
		PhraseDefinitionCoordinates phraseDefinitionCoordinates = null;

		while (indexReader.hasNextPhraseDefinitionCoordinates()) {
			phraseDefinitionCoordinates = indexReader.readPhraseDefinitionCoordinates();
			if (phraseDefinitionCoordinates.matches(targetWord)) {
				break;
			} else {
				phraseDefinitionCoordinates = null;
			}
		}

		return phraseDefinitionCoordinates;
	}

	private void ensurePhraseDefinitionCoordinatesFound(PhraseDefinitionCoordinates phraseDefinitionCoordinates,
			String erroneousWord) throws PhraseDefinitionCoordinatesNotFoundException {
		if (phraseDefinitionCoordinates == null) {
			throw new PhraseDefinitionCoordinatesNotFoundException(String.format(
					"Couldn't locate word definition coordinate in index for [%s]", erroneousWord));
		}
	}
}
