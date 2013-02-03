package org.verba.mobile;

import org.robobinding.binder.Binder;
import org.verba.mobile.presentationmodel.ViewCardPresentationModel;

import android.os.Bundle;

import com.google.inject.Inject;

public class ViewCardActivity extends VerbaActivity {
	@Inject private ViewCardPresentationModel presentationModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Binder.bind(this, R.layout.view_card, presentationModel);
	}
}