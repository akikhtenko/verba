package org.verba.interactors;

import java.util.List;

import org.verba.DictionaryDataObject;
import org.verba.DictionaryRepository;

public class GetDictionary {
	private DictionaryRepository dictionaryRepository;

	public GetDictionary(DictionaryRepository dictionaryRepository) {
		this.dictionaryRepository = dictionaryRepository;
	}

	public List<DictionaryDataObject> all() {
		return dictionaryRepository.getAllDictionaries();
	}

}
