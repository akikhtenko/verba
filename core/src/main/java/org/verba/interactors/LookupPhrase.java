package org.verba.interactors;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryDataObject;
import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionRepository;
import org.verba.stardict.definitions.DictionaryDefinitionsGateway;
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

public class LookupPhrase {
	private DictionaryRepository dictionaryRepository;
	private DictionaryEntryRepository dictionaryEntryRepository;
	private DictionaryMetadataGateway metadataGateway;
	private DictionaryDefinitionsGateway definitionsGateway;

	public LookupPhrase(DictionaryRepository dictionaryRepository,
			DictionaryEntryRepository dictionaryEntryRepository,
			DictionaryMetadataGateway metadataGateway,
			DictionaryDefinitionsGateway definitionsGateway) {
		this.dictionaryRepository = dictionaryRepository;
		this.dictionaryEntryRepository = dictionaryEntryRepository;
		this.metadataGateway = metadataGateway;
		this.definitionsGateway = definitionsGateway;
	}

	public List<PhraseDefinition> with(String phrase) {
		List<DictionaryEntryDataObject> entriesFound = dictionaryEntryRepository.getEntriesByPhrase(phrase);
		List<PhraseDefinition> foundDefinitions = new ArrayList<PhraseDefinition>();
		for (DictionaryEntryDataObject dictionaryEntry : entriesFound) {
			foundDefinitions.add(lookupDefinitionFor(phrase, dictionaryEntry));
		}

		return foundDefinitions;
	}

	private PhraseDefinition lookupDefinitionFor(String phrase, DictionaryEntryDataObject dictionaryEntry) {
		DictionaryDataObject dictionary = dictionaryRepository.getDictionaryById(dictionaryEntry.getDictionaryId());

		DictionaryMetadata dictionaryMetadata = metadataGateway.getMetadataFor(dictionary.getName());
		PhraseDefinitionRepository phraseDefinitionRepository = definitionsGateway.getDictionaryDefinitionsFor(dictionary.getName(), dictionaryMetadata);

		try {
			return phraseDefinitionRepository.find(dictionaryEntry.asPhraseDefinitionCoordinates());
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed looking up a definition of [%s]", phrase), e);
		} finally {
			closeQuietly(phraseDefinitionRepository);
		}
	}

}
