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

	public WordDefinition lookup(String wordToLookFor) throws WordDefinitionCoordinatesNotFoundException {
		WordDefinitionCoordinates wordDefinitionCoordinates = getWordDefinitionCoordinatesFromIndex(wordToLookFor);
		
		return getWordDefinitionFromRepository(wordDefinitionCoordinates);
	}

	private WordDefinitionCoordinates getWordDefinitionCoordinatesFromIndex(String wordToLookFor) throws WordDefinitionCoordinatesNotFoundException {
		try {
			return wordDefinitionCoordinatesRepository.find(wordToLookFor);
		} catch (IOException e) {
			throw new RuntimeException("Error occured while reading dictionary index");
		}
	}
	
	private WordDefinition getWordDefinitionFromRepository(WordDefinitionCoordinates wordDefinitionCoordinates) {
		try {
			return wordDefinitionRepository.find(wordDefinitionCoordinates);
		} catch (IOException e) {
			throw new RuntimeException("Error occured while looking up words repository by its coordinates");
		}
	}
}
