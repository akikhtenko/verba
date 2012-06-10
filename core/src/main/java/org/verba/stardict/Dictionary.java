package org.verba.stardict;

import java.io.IOException;

public class Dictionary {
	private PhraseDefinitionCoordinatesRepository phraseDefinitionCoordinatesRepository;
	private PhraseDefinitionRepository phraseDefinitionRepository;

	public Dictionary(PhraseDefinitionCoordinatesRepository aPhraseDefinitionCoordinatesRepository,
			PhraseDefinitionRepository aPhraseDefinitionRepository) {
		phraseDefinitionCoordinatesRepository = aPhraseDefinitionCoordinatesRepository;
		phraseDefinitionRepository = aPhraseDefinitionRepository;
	}

	public PhraseDefinition lookup(String wordToLookFor) throws PhraseDefinitionCoordinatesNotFoundException, IOException {
		PhraseDefinitionCoordinates phraseDefinitionCoordinates = phraseDefinitionCoordinatesRepository.find(wordToLookFor);

		return phraseDefinitionRepository.find(phraseDefinitionCoordinates);
	}
}
