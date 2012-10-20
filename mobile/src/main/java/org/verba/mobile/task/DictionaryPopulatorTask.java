package org.verba.mobile.task;

import org.verba.boundary.DictionaryPopulationProgressListener;
import org.verba.boundary.DictionaryPopulator;

import android.os.AsyncTask;
import android.widget.ProgressBar;

public class DictionaryPopulatorTask extends AsyncTask<String, Integer, Void> implements
		DictionaryPopulationProgressListener {
	private static final int DEFAULT_PROGRESS_DELTA = 100;
	private ProgressBar progressBar;
	private int dictionarySize;
	private DictionaryPopulator dictionaryPopulator;
	private int progressDelta;

	public DictionaryPopulatorTask(ProgressBar progressBar, int dictionarySize,
			DictionaryPopulator dictionaryPopulator) {
		this.progressBar = progressBar;
		this.dictionarySize = dictionarySize;
		this.dictionaryPopulator = dictionaryPopulator;
		this.progressDelta = DEFAULT_PROGRESS_DELTA;
	}

	@Override
	protected void onPreExecute() {
		progressBar.setMax(dictionarySize);
	}

	@Override
	protected Void doInBackground(String... dictionaryNames) {
		dictionaryPopulator.populate(dictionaryNames[0]);

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		progressBar.incrementProgressBy(progressDelta);
	}

	@Override
	protected void onPostExecute(Void result) {
		progressBar.setProgress(progressBar.getMax());
	}

	@Override
	public void onChunkPopulated() {
		publishProgress();
	}

	public void setProgressDelta(int aProgressDelta) {
		progressDelta = aProgressDelta;
	}
}