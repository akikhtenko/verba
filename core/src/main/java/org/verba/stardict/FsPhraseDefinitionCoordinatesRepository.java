package org.verba.stardict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.verba.stardict.index.DictionaryIndex;

public class FsPhraseDefinitionCoordinatesRepository implements PhraseDefinitionCoordinatesRepository {
	private File indexFile;

	public FsPhraseDefinitionCoordinatesRepository(File indexFile) {
		this.indexFile = indexFile;
	}

	@Override
	public PhraseDefinitionCoordinates find(String targetWord) throws PhraseDefinitionCoordinatesNotFoundException {
		DictionaryIndex indexReader = getIndexReader();
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

	protected DictionaryIndex getIndexReader() {
		try {
			InputStream indexStream = new FileInputStream(indexFile);
			return new DictionaryIndex(indexStream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private PhraseDefinitionCoordinates getPhraseDefinitionCoordinates(String targetWord,
			DictionaryIndex indexReader) throws IOException {
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
