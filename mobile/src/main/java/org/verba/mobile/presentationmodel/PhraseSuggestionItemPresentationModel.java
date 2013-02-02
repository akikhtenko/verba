package org.verba.mobile.presentationmodel;

import org.robobinding.itempresentationmodel.ItemPresentationModel;

public class PhraseSuggestionItemPresentationModel implements ItemPresentationModel<String> {
	private String suggestion;

	public String getSuggestion() {
		return suggestion;
	}

	@Override
	public void updateData(int index, String aSuggestion) {
		this.suggestion = aSuggestion;
	}

}
