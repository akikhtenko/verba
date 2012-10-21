package org.verba.boundary;

import java.util.List;

public interface SuggestionAdviser {
	List<String> getTopSuggestions(String phrasePattern, int limit);
}
