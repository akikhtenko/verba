package org.verba.mobile;

import org.robobinding.binder.Binder;
import org.robobinding.viewattribute.adapterview.DataSetAdapter;
import org.verba.DictionaryDataObject;
import org.verba.DictionaryRepository;
import org.verba.mobile.presentationmodel.DictionaryManagerPresentationModel;
import org.verba.mobile.presentationmodel.DictionaryMenuPresentationModel;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;
import com.mobeta.android.dslv.DragSortListView;

public class DictionaryManagerActivity extends RoboSherlockActivity {
	@Inject private DictionaryManagerPresentationModel presentationModel;
	@Inject private DictionaryRepository dictionaryRepository;
	@InjectView(R.id.dictionaries) DragSortListView dictionaries;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@SuppressWarnings("unchecked")
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				DataSetAdapter<DictionaryDataObject> adapter = (DataSetAdapter<DictionaryDataObject>) dictionaries.getInputAdapter();
				dictionaryRepository.moveToPosition(adapter.getItem(from), adapter.getItem(to).getPosition());
				presentationModel.setDictionariesOrderChanged(true);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Binder.bind(this, R.layout.dictionary_manager, presentationModel);

		DictionaryMenuPresentationModel menuPresentationModel = new DictionaryMenuPresentationModel(this);
		View menuView = Binder.bindView(this, R.layout.dictionary_manager_menu, menuPresentationModel);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(menuView, new LayoutParams(Gravity.RIGHT));

		dictionaries.setDropListener(onDrop);
	}
}