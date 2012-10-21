package org.verba.mobile.dictionarycandidate;

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
		super(context, R.layout.dictionary_candidates_list_item, R.id.listItemTitle, dictionaries);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DictionaryCandidate dictionaryCandidate = getItem(position);
		View dictionaryCandidateView = convertView;

		if (dictionaryCandidateView == null) {
			dictionaryCandidateView = inflateNewDictionaryCandidateView();

			setDictinaryCandidateTitle(dictionaryCandidateView, dictionaryCandidate.getTitle());
			setLoadingProgress(dictionaryCandidate, dictionaryCandidateView);

			final CheckBox dictionaryCandidateSelector =
					(CheckBox) dictionaryCandidateView.findViewById(R.id.dictionaryCandidateSelector);
			dictionaryCandidate.setSelector(dictionaryCandidateSelector);

			dictionaryCandidateView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dictionaryCandidateSelector.toggle();
				}
			});
		}

		return dictionaryCandidateView;
	}

	private void setLoadingProgress(final DictionaryCandidate dictionaryCandidate, View dictionaryCandidateView) {
		dictionaryCandidate.setLoadingProgress(
				(ProgressBar) dictionaryCandidateView.findViewById(R.id.loadingDictionaryProgress));
	}

	private void setDictinaryCandidateTitle(View dictionaryCandidateView, String dictionaryCandidateTitle) {
		final TextView textView = (TextView) dictionaryCandidateView.findViewById(R.id.listItemTitle);
		textView.setText(dictionaryCandidateTitle);
	}

	private View inflateNewDictionaryCandidateView() {
		LayoutInflater inflator = context.getLayoutInflater();
		return inflator.inflate(R.layout.dictionary_candidates_list_item, null);
	}
}
