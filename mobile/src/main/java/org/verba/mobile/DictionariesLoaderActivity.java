package org.verba.mobile;

import java.util.List;

import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.interactors.GetDictionarySize;
import org.verba.interactors.GetNewDictionaries;
import org.verba.interactors.PopulateDictionary;
import org.verba.mobile.dictionarycandidate.DictionaryCandidate;
import org.verba.mobile.dictionarycandidate.DictionaryCandidateArrayAdapter;
import org.verba.mobile.dictionarycandidate.ToDictionaryCandidateConverter;
import org.verba.mobile.task.DictionaryPopulatorTask;
import org.verba.stardict.index.DictionaryIndexGateway;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.inject.Inject;

public class DictionariesLoaderActivity extends VerbaActivity {
	private static final int POPULATION_PROGRESS_DELTA = 100;
	private List<DictionaryCandidate> dictionaryCandidates;
	@Inject private DictionaryRepository dictionaryRepository;
	@Inject private DictionaryEntryRepository dictionaryEntryRepository;
	@Inject private DictionaryMetadataGateway metadataGateway;
	@Inject private DictionaryIndexGateway indexGateway;
	@Inject private GetNewDictionaries getNewDictionaries;

	private OnClickListener loadNewDictionariesButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadSelectedDictionaries();
		}
	};

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.dictionaries_loader;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupDictionaryCandidatesListView();
		setupLoadNewDictionariesButton();
	}

	private void setupLoadNewDictionariesButton() {
		Button loadNewDictionariesButton = (Button) findViewById(R.id.loadNewDictionariesButton);
		loadNewDictionariesButton.setOnClickListener(loadNewDictionariesButtonListener);
	}

	private void setupDictionaryCandidatesListView() {
		ListView dictionaryCandidatesListView = (ListView) findViewById(R.id.newDictionaries);
		dictionaryCandidates = new ToDictionaryCandidateConverter().convert(getNewDictionaries.all());
		dictionaryCandidatesListView.setAdapter(new DictionaryCandidateArrayAdapter(this, dictionaryCandidates));
	}

	private void loadSelectedDictionaries() {
		for (DictionaryCandidate dictionaryCandidate: dictionaryCandidates) {
			if (dictionaryCandidate.isSelected()) {
				populateDictionary(dictionaryCandidate);
			} else {
				markDictionarySkipped(dictionaryCandidate);
			}
		}
	}

	private void markDictionarySkipped(DictionaryCandidate dictionaryCandidate) {
		// TODO Auto-generated method stub
	}

	private void populateDictionary(DictionaryCandidate dictionaryCandidate) {
		PopulateDictionary populateDictionary =
				new PopulateDictionary(metadataGateway, indexGateway, dictionaryRepository, dictionaryEntryRepository);

		GetDictionarySize dictionarySizeFinder = new GetDictionarySize(metadataGateway);

		DictionaryPopulatorTask dictionaryPopulator = new DictionaryPopulatorTask(
				dictionaryCandidate.getLoadingProgress(),
				dictionarySizeFinder.with(dictionaryCandidate.getTitle()),
				populateDictionary);

		populateDictionary.setProgressNotificationDelta(POPULATION_PROGRESS_DELTA);
		dictionaryPopulator.setProgressDelta(POPULATION_PROGRESS_DELTA);

		populateDictionary.registerProgressListener(dictionaryPopulator);
		dictionaryPopulator.execute(dictionaryCandidate.getTitle());
	}
}