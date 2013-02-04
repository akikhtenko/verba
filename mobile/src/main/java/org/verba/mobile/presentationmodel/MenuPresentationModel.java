package org.verba.mobile.presentationmodel;

import org.robobinding.presentationmodel.PresentationModel;
import org.verba.mobile.CardSetPickerActivity;
import org.verba.mobile.DictionaryManagerActivity;
import org.verba.mobile.PhraseLookupActivity;
import org.verba.mobile.VerbaActivity;
import org.verba.mobile.VerbaPreferences;

import android.content.Intent;

@PresentationModel
public class MenuPresentationModel {
	private VerbaActivity currentVerbaActivity;

	public MenuPresentationModel(VerbaActivity verbaActivity) {
		this.currentVerbaActivity = verbaActivity;
	}

	public void openDictionary() {
		gotoVerbaActivity(PhraseLookupActivity.class);
	}

	public void openCardSetPicker() {
		gotoVerbaActivity(CardSetPickerActivity.class);
	}

	public void openDictionaryManager() {
		gotoVerbaActivity(DictionaryManagerActivity.class);
	}

	public void openVerbaPreferences() {
		gotoVerbaActivity(VerbaPreferences.class);
	}

	private void gotoVerbaActivity(Class<?> verbaActivityClass) {
		Intent commandToOpenActivity = new Intent(currentVerbaActivity, verbaActivityClass);
		currentVerbaActivity.startActivity(commandToOpenActivity);
	}
}
