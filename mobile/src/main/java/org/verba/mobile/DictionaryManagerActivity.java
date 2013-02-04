package org.verba.mobile;

import org.robobinding.binder.Binder;
import org.verba.mobile.presentationmodel.DictionaryManagerPresentationModel;
import org.verba.mobile.presentationmodel.DictionaryMenuPresentationModel;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;

public class DictionaryManagerActivity extends RoboSherlockActivity {
	@Inject private DictionaryManagerPresentationModel presentationModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Binder.bind(this, R.layout.dictionary_manager, presentationModel);

		DictionaryMenuPresentationModel menuPresentationModel = new DictionaryMenuPresentationModel(this);
		View menuView = Binder.bindView(this, R.layout.dictionary_manager_menu, menuPresentationModel);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(menuView, new LayoutParams(Gravity.RIGHT));
	}
}