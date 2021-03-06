package org.verba.mobile.presentationmodel;

import java.util.List;

import org.robobinding.presentationmodel.DependsOnStateOf;
import org.robobinding.presentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.verba.DictionaryDataObject;
import org.verba.interactors.GetDictionary;

import com.google.inject.Inject;

@PresentationModel
public class DictionaryManagerPresentationModel {
	@Inject private GetDictionary getDictionary;

	public void setDictionariesOrderChanged(boolean dictionariesOrderChanged) {
		// all invocations of this method are observed by RoboBinding and cause dictionaries re-read
	}

	@DependsOnStateOf("dictionariesOrderChanged")
	@ItemPresentationModel(DictionaryItemPresentationModel.class)
	public List<DictionaryDataObject> getDictionaries() {
		return getDictionary.all();
	}
}
