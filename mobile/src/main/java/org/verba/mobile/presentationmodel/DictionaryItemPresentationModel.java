package org.verba.mobile.presentationmodel;

import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.verba.DictionaryDataObject;

public class DictionaryItemPresentationModel implements ItemPresentationModel<DictionaryDataObject> {
	private DictionaryDataObject dictionary;

	public String getDictionaryName() {
		return dictionary.getDescription();
	}

	@Override
	public void updateData(int index, DictionaryDataObject aDictionary) {
		this.dictionary = aDictionary;
	}

}
