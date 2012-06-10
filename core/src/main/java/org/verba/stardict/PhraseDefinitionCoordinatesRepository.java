package org.verba.stardict;

public interface PhraseDefinitionCoordinatesRepository {
	PhraseDefinitionCoordinates find(String targetWord) throws PhraseDefinitionCoordinatesNotFoundException;
}
