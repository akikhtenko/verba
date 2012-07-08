package org.verba.mobile.dictionary;

import java.util.List;

import org.verba.mobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DictionaryCandidateArrayAdapter extends ArrayAdapter<DictionaryCandidate> {
	private Activity context;
	public DictionaryCandidateArrayAdapter(Activity context, List<DictionaryCandidate> dictionaries) {
		super(context, R.layout.new_dictionary_list_item, R.id.listItemTitle, dictionaries);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DictionaryCandidate dictionaryCandidate = getItem(position);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.new_dictionary_list_item, null);

			final TextView textView = (TextView) view.findViewById(R.id.listItemTitle);
			textView.setText(dictionaryCandidate.getName());

			final CheckBox checkbox = (CheckBox) view.findViewById(R.id.loadThisDictionaryCheck);
			dictionaryCandidate.setCheckBox(checkbox);

			dictionaryCandidate.setLoadingProgress((ProgressBar) view.findViewById(R.id.loadingDictionaryProgress));

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					checkbox.toggle();
				}
			});
		}

		return view;
	}
}
