package org.verba.mobile.presentationmodel;

import org.robobinding.presentationmodel.PresentationModel;
import org.verba.mobile.DictionariesLoaderActivity;

import android.app.Activity;
import android.content.Intent;

@PresentationModel
public class DictionaryMenuPresentationModel {
	private Activity activity;

	public DictionaryMenuPresentationModel(Activity activity) {
		this.activity = activity;
	}

	public void openDictionaryLoader() {
		Intent commandToOpenActivity = new Intent(activity, DictionariesLoaderActivity.class);
		activity.startActivity(commandToOpenActivity);
	}
}
