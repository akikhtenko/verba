package org.verba.mobile;

import static org.verba.mobile.PhraseLookupActivity.NEW_DICTIONARIES;

import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.interactors.StardictDictionaryPopulator;
import org.verba.interactors.StardictDictionarySizeFinder;
import org.verba.mobile.dictionarycandidate.DictionaryCandidate;
import org.verba.mobile.dictionarycandidate.DictionaryCandidateArrayAdapter;
import org.verba.mobile.dictionarycandidate.ToDictionaryCandidateConverter;
import org.verba.mobile.task.DictionaryPopulatorTask;
import org.verba.stardict.index.DictionaryIndexGateway;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

import roboguice.inject.InjectExtra;
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
	@InjectExtra(NEW_DICTIONARIES) private ArrayList<String> newDictionaries;

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

		setupDictionaryCandidatesListView();
		setupLoadNewDictionariesButton();
		setupCloseDictionariesLoaderButton();
	}

	private void setupLoadNewDictionariesButton() {
		Button loadNewDictionariesButton = (Button) findViewById(R.id.loadNewDictionariesButton);
		loadNewDictionariesButton.setOnClickListener(loadNewDictionariesButtonListener);
	}

	private void setupCloseDictionariesLoaderButton() {
		Button closeDictionariesLoaderButton = (Button) findViewById(R.id.closeNewDictionariesDialogButton);
		closeDictionariesLoaderButton.setOnClickListener(closeDictionariesLoaderButtonListener);
	}

	private void setupDictionaryCandidatesListView() {
		ListView dictionaryCandidatesListView = (ListView) findViewById(R.id.newDictionaries);
		dictionaryCandidates = new ToDictionaryCandidateConverter().convert(newDictionaries);
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
		StardictDictionaryPopulator stardictDictionaryPopulator =
				new StardictDictionaryPopulator(metadataGateway, indexGateway, dictionaryRepository, dictionaryEntryRepository);

		StardictDictionarySizeFinder dictionarySizeFinder = new StardictDictionarySizeFinder(metadataGateway);

		DictionaryPopulatorTask dictionaryPopulator = new DictionaryPopulatorTask(
				dictionaryCandidate.getLoadingProgress(),
				dictionarySizeFinder.getDictionarySize(dictionaryCandidate.getTitle()),
				stardictDictionaryPopulator);

		stardictDictionaryPopulator.setProgressNotificationDelta(POPULATION_PROGRESS_DELTA);
		dictionaryPopulator.setProgressDelta(POPULATION_PROGRESS_DELTA);

		stardictDictionaryPopulator.registerProgressListener(dictionaryPopulator);
		dictionaryPopulator.execute(dictionaryCandidate.getTitle());
	}
}