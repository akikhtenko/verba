package org.verba.mobile.task;

import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.IOCase.INSENSITIVE;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.verba.mobile.Application.getVerbaDirectory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.stardict.DictionaryEntryDao.DoInTransactionCallback;
import org.verba.mobile.stardict.DictionaryEntryDataObject;
import org.verba.stardict.DictionaryIndexReader;
import org.verba.stardict.PhraseDefinitionCoordinates;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class DictionaryPopulatorTask extends AsyncTask<Void, Integer, Void> {
	private static final int TRANSACTION_BATCH_SIZE = 5000;
	private static final int PROGRESS_DELTA = 100;
	private static final String DICTIONARY_INDEXFILE_EXTENSION = "idx";
	private ProgressBar progressBar;
	private int dictionaryId;
	private int dictionarySize;
	private int countInserted;
	private String dictionaryName;
	private DictionaryEntryDao dictionaryEntryDao;

	public DictionaryPopulatorTask(ProgressBar progressBar, String dictionaryName, int dictionaryId, int dictionarySize,
			DictionaryEntryDao dictionaryEntryDao) {
		this.progressBar = progressBar;
		this.dictionaryName = dictionaryName;
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
			closeQuietly(indexReader);
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
		return new DictionaryIndexReader(getDictionaryDataSource());
	}

	private InputStream getDictionaryDataSource() throws FileNotFoundException {
		Collection<File> filesFound = findAllFilesMatchingDictionaryName();

		ensureAtLeastOneIndexFileFound(filesFound);

		return new FileInputStream(filesFound.iterator().next());
	}

	private void ensureAtLeastOneIndexFileFound(Collection<File> filesFound) {
		if (filesFound.isEmpty()) {
			throw new RuntimeException("Dictionary index file not found");
		}
	}

	private Collection<File> findAllFilesMatchingDictionaryName() {
		String dictionaryMetadataFileName = dictionaryName + "." + DICTIONARY_INDEXFILE_EXTENSION;
		NameFileFilter dictionaryMetadataFileFilter = new NameFileFilter(dictionaryMetadataFileName, INSENSITIVE);
		return listFiles(getRootDirectory(), dictionaryMetadataFileFilter, TrueFileFilter.INSTANCE);
	}

	protected File getRootDirectory() {
		return getVerbaDirectory();
	}

}