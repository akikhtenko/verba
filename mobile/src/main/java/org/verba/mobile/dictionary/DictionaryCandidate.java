package org.verba.mobile.dictionary;

import android.widget.CheckBox;
import android.widget.ProgressBar;

public class DictionaryCandidate {
	private String title;
	private CheckBox selector;
	private ProgressBar loadingProgress;

	public DictionaryCandidate(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	public ProgressBar getLoadingProgress() {
		return loadingProgress;
	}
	public void setLoadingProgress(ProgressBar loadingProgress) {
		this.loadingProgress = loadingProgress;
	}
	public CheckBox getSelector() {
		return selector;
	}
	public void setSelector(CheckBox selector) {
		this.selector = selector;
	}

	public boolean isSelected() {
		return selector != null && selector.isChecked() ? true : false;
	}
}
