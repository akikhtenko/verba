package org.verba.stardict;

import java.io.IOException;

import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;


public class Dictionary {
	private WordDefinitionCoordinatesRepository wordDefinitionCoordinatesRepository;
	private WordDefinitionRepository wordDefinitionRepository;
	
	public Dictionary(WordDefinitionCoordinatesRepository aWordDefinitionCoordinatesRepository, WordDefinitionRepository aWordDefinitionRepository) {
		wordDefinitionCoordinatesRepository = aWordDefinitionCoordinatesRepository;
		wordDefinitionRepository = aWordDefinitionRepository;
	}

	public WordDefinition lookup(String wordToLookFor) throws WordDefinitionCoordinatesNotFoundException, IOException {
		WordDefinitionCoordinates wordDefinitionCoordinates = wordDefinitionCoordinatesRepository.find(wordToLookFor);
		
		return wordDefinitionRepository.find(wordDefinitionCoordinates);
	}
}
