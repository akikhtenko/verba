package org.verba.mobile;

import org.robobinding.binder.Binder;
import org.verba.mobile.presentationmodel.PhraseDefinitionLookupPresentationModel;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.inject.Inject;

public class PhraseLookupActivity extends VerbaActivity {
	public static final String NEW_DICTIONARIES = "newDictionaries";
	@Inject private PhraseDefinitionLookupPresentationModel presentationModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		Binder.bind(this, R.layout.phrase_definition_lookup, presentationModel);
	}
}