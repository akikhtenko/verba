package org.verba.mobile;

import static org.verba.mobile.Application.getVerbaDirectory;
import static org.verba.mobile.PhraseLookupActivity.NEW_DICTIONARIES;

import java.util.ArrayList;
import java.util.List;

import org.verba.mobile.dictionary.DictionariesManager;
import org.verba.mobile.dictionary.DictionaryCandidate;
import org.verba.mobile.dictionary.DictionaryCandidateArrayAdapter;
import org.verba.mobile.dictionary.DictionaryLookupException;
import org.verba.mobile.stardict.DictionaryDataObject;
import org.verba.mobile.task.DictionaryPopulatorTask;
import org.verba.stardict.DictionaryMetadata;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class DictionariesLoaderActivity extends DictionaryActivity {
	private List<DictionaryCandidate> dictionaryCandidates;
	private DictionariesManager dictionariesManager;

	private OnClickListener loadNewDictionariesButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadSelectedDictionaries();
		}
	};

	private OnClickListener closeDictionariesLoaderButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	protected boolean loadSystemMenu() {
		return false;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.dictionaries_loader;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupNewDictionariesList();
		setupLoadNewDictionariesButton();
		setupCloseDictionariesLoaderButton();
		setupDictionariesManager();
	}

	private void setupDictionariesManager() {
		dictionariesManager = new DictionariesManager(getVerbaDirectory(), dictionaryDao);
	}

	private void setupLoadNewDictionariesButton() {
		Button loadNewDictionariesButton = (Button) findViewById(R.id.loadNewDictionariesButton);
		loadNewDictionariesButton.setOnClickListener(loadNewDictionariesButtonListener);
	}

	private void setupCloseDictionariesLoaderButton() {
		Button closeDictionariesLoaderButton = (Button) findViewById(R.id.closeNewDictionariesDialogButton);
		closeDictionariesLoaderButton.setOnClickListener(closeDictionariesLoaderButtonListener);
	}

	private void setupNewDictionariesList() {
		ArrayList<String> dictionaries = getIntent().getStringArrayListExtra(NEW_DICTIONARIES);

		ListView newDictionariesList = (ListView) findViewById(R.id.newDictionaries);
		dictionaryCandidates = convertToDictionaryCandidatesList(dictionaries);
		newDictionariesList.setAdapter(
				new DictionaryCandidateArrayAdapter(this, dictionaryCandidates));
	}

	private void loadSelectedDictionaries() {
		for (DictionaryCandidate dictionaryCandidate: dictionaryCandidates) {
			if (dictionaryCandidate.isSelected()) {
				loadDictionary(dictionaryCandidate);
			} else {
				markDictionarySkipped(dictionaryCandidate);
			}
		}
	}

	private void markDictionarySkipped(DictionaryCandidate dictionaryCandidate) {
		// TODO Auto-generated method stub
	}

	private void loadDictionary(DictionaryCandidate dictionaryCandidate) {
		DictionaryMetadata dictionaryMetadata = lookupDictionaryMetadata(dictionaryCandidate.getName());
		loadNewDictionary(dictionaryCandidate, dictionaryMetadata);
	}

	private DictionaryMetadata lookupDictionaryMetadata(String dictionaryName) {
		try {
			return dictionariesManager.lookupDictionaryMetadata(dictionaryName);
		} catch (DictionaryLookupException e) {
			throw new RuntimeException(e); // For now, then show something to user
		}
	}

	private void loadNewDictionary(DictionaryCandidate dictionaryCandidate, DictionaryMetadata dictionaryMetadata) {
		DictionaryDataObject dictionaryDataObject = new DictionaryDataObject();
		dictionaryDataObject.setName(dictionaryCandidate.getName());
		dictionaryDataObject.setDescription(dictionaryMetadata.getName());

		int dictionaryId = dictionaryDao.addDictionary(dictionaryDataObject);
		populateDictionary(dictionaryId, dictionaryCandidate, dictionaryMetadata);
	}

	private void populateDictionary(int dictionaryId, DictionaryCandidate dictionaryCandidate,
			DictionaryMetadata dictionaryMetadata) {
		new DictionaryPopulatorTask(dictionaryCandidate.getLoadingProgress(), dictionaryId,
				dictionaryMetadata.getWordCount(), dictionaryEntryDao).execute();
	}

	private List<DictionaryCandidate> convertToDictionaryCandidatesList(ArrayList<String> dictionaries) {
		List<DictionaryCandidate> dictionaryCandidatesList = new ArrayList<DictionaryCandidate>();
		for (String dictionaryName: dictionaries) {
			dictionaryCandidatesList.add(new DictionaryCandidate(dictionaryName));
		}
		return dictionaryCandidatesList;
	}
}