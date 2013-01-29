package org.verba.mobile;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class VerbaPreferences extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
