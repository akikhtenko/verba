package org.verba.mobile.task;

import static org.verba.mobile.Application.getVerbaDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;
import org.verba.stardict.PhraseDefinitionRepository;

public class PhraseDefinitionLookup {
	public PhraseDefinition lookupPhraseDefinition(PhraseDefinitionCoordinates phraseDefinitionCoordinates)
			throws IOException {
		PhraseDefinitionRepository definitionsRepository = getPhraseDefinitionsRepository();

		PhraseDefinition phraseDefinition = null;
		try {
			phraseDefinition = definitionsRepository.find(phraseDefinitionCoordinates);
		} finally {
			definitionsRepository.destroy();
		}

		return phraseDefinition;
	}

	protected PhraseDefinitionRepository getPhraseDefinitionsRepository() throws FileNotFoundException {
		InputStream dictionaryStream = getDictionaryInputStream();
		return new PhraseDefinitionRepository(dictionaryStream);
	}

	private InputStream getDictionaryInputStream() throws FileNotFoundException {
		File dictionaryFile = new File(getRootDirectory(), "dictionary.dict");
		return new FileInputStream(dictionaryFile);
	}

	protected File getRootDirectory() {
		return getVerbaDirectory();
	}
}
