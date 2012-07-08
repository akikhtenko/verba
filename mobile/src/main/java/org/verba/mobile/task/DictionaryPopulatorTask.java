package org.verba.mobile.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.stardict.DictionaryEntryDao.DoInTransactionCallback;
import org.verba.mobile.stardict.DictionaryEntryDataObject;
import org.verba.stardict.DictionaryIndexReader;
import org.verba.stardict.PhraseDefinitionCoordinates;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ProgressBar;

public class DictionaryPopulatorTask extends AsyncTask<Void, Integer, Void> {
	private static final int TRANSACTION_BATCH_SIZE = 5000;
	private static final int PROGRESS_DELTA = 100;
	private ProgressBar progressBar;
	private int dictionaryId;
	private int dictionarySize;
	private int countInserted;
	private DictionaryEntryDao dictionaryEntryDao;

	public DictionaryPopulatorTask(ProgressBar progressBar, int dictionaryId, int dictionarySize,
			DictionaryEntryDao dictionaryEntryDao) {
		this.progressBar = progressBar;
		this.dictionaryId = dictionaryId;
		this.dictionarySize = dictionarySize;
		this.dictionaryEntryDao = dictionaryEntryDao;
	}

	@Override
	protected void onPreExecute() {
		countInserted = 0;
		progressBar.setMax(dictionarySize);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			insertEntriesToDictionary();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		progressBar.incrementProgressBy(PROGRESS_DELTA);
	}

	@Override
	protected void onPostExecute(Void result) {
		progressBar.setProgress(progressBar.getMax());
	}

	private void insertEntriesToDictionary() throws IOException {
		DictionaryIndexReader indexReader = getIndexReader();
		try {
			insertAllEntriesToDictionaryFrom(indexReader);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while iterating over index entries", e);
		} finally {
			indexReader.close();
		}
	}

	private void insertAllEntriesToDictionaryFrom(final DictionaryIndexReader indexReader) throws Exception {
		while (indexReader.hasNextPhraseDefinitionCoordinates()) {
			dictionaryEntryDao.doInTransaction(new DoInTransactionCallback() {
				@Override
				public void doInTransaction() throws Exception {
					int countInTransaction = 0;
					while (countInTransaction < TRANSACTION_BATCH_SIZE && indexReader.hasNextPhraseDefinitionCoordinates()) {
						PhraseDefinitionCoordinates phraseDefinitionCoordinates = indexReader.readPhraseDefinitionCoordinates();
						DictionaryEntryDataObject dictionaryEntry = new DictionaryEntryDataObject(dictionaryId,
								phraseDefinitionCoordinates);
						dictionaryEntryDao.addDictionaryEntry(dictionaryEntry);

						countInserted++;
						countInTransaction++;
						publishProgressIfNecessary();
					}
				}
			});
		}
	}

	private void publishProgressIfNecessary() {
		if (countInserted % PROGRESS_DELTA == 0) {
			publishProgress();
		}
	}

	private DictionaryIndexReader getIndexReader() throws FileNotFoundException {
		File indexFile = new File(getVerbaDirectory(), "dictionary.idx");
		InputStream indexStream = new FileInputStream(indexFile);

		return new DictionaryIndexReader(indexStream);
	}

	protected File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}
}