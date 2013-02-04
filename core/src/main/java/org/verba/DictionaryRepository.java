package org.verba;

import java.util.List;

public interface DictionaryRepository {
	List<DictionaryDataObject> getAllDictionaries();
	int addDictionary(DictionaryDataObject dictionaryDataObject);
	boolean exists(String dictionaryName);
	DictionaryDataObject getDictionaryById(int dictionaryId);
}
