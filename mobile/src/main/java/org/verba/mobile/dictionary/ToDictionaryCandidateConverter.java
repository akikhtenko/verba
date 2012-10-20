package org.verba.mobile.dictionary;

import java.util.ArrayList;
import java.util.List;

public class ToDictionaryCandidateConverter {
	public List<DictionaryCandidate> convert(ArrayList<String> dictionaries) {
		List<DictionaryCandidate> dictionaryCandidatesList = new ArrayList<DictionaryCandidate>();
		for (String dictionaryName: dictionaries) {
			dictionaryCandidatesList.add(new DictionaryCandidate(dictionaryName));
		}
		return dictionaryCandidatesList;
	}
}
