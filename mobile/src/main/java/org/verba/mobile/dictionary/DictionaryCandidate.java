package org.verba.mobile.dictionary;

import android.widget.CheckBox;
import android.widget.ProgressBar;

public class DictionaryCandidate {
	private String name;
	private CheckBox checkBox;
	private ProgressBar loadingProgress;

	public DictionaryCandidate(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public ProgressBar getLoadingProgress() {
		return loadingProgress;
	}
	public void setLoadingProgress(ProgressBar loadingProgress) {
		this.loadingProgress = loadingProgress;
	}
	public CheckBox getCheckBox() {
		return checkBox;
	}
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public boolean isSelected() {
		return checkBox != null && checkBox.isChecked() ? true : false;
	}
}
