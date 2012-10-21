package org.verba.interactors;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryDataObject;
import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.boundary.PhraseLookup;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionRepository;
import org.verba.stardict.definitions.DictionaryDefinitionsGateway;

public class StardictPhraseLookup implements PhraseLookup {
	private DictionaryRepository dictionaryRepository;
	private DictionaryEntryRepository dictionaryEntryRepository;
	private DictionaryDefinitionsGateway definitionsGateway;

	public StardictPhraseLookup(DictionaryRepository dictionaryRepository,
			DictionaryEntryRepository dictionaryEntryRepository,
			DictionaryDefinitionsGateway definitionsGateway) {
		this.dictionaryRepository = dictionaryRepository;
		this.dictionaryEntryRepository = dictionaryEntryRepository;
		this.definitionsGateway = definitionsGateway;
	}

	@Override
	public List<PhraseDefinition> lookup(String phrase) {
		List<DictionaryEntryDataObject> entriesFound = dictionaryEntryRepository.getEntriesByPhrase(phrase);
		List<PhraseDefinition> foundDefinitions = new ArrayList<PhraseDefinition>();
		for (DictionaryEntryDataObject dictionaryEntry : entriesFound) {
			foundDefinitions.add(lookupDefinitionFor(phrase, dictionaryEntry));
		}

		return foundDefinitions;
	}

	private PhraseDefinition lookupDefinitionFor(String phrase, DictionaryEntryDataObject dictionaryEntry) {
		DictionaryDataObject dictionary = dictionaryRepository.getDictionaryById(dictionaryEntry.getDictionaryId());

		PhraseDefinitionRepository phraseDefinitionRepository =
				definitionsGateway.getDictionaryDefinitionsFor(dictionary.getName());

		try {
			return phraseDefinitionRepository.find(dictionaryEntry.asPhraseDefinitionCoordinates());
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed looking for a definition of [%s]", phrase), e);
		} finally {
			closeQuietly(phraseDefinitionRepository);
		}
	}

}
