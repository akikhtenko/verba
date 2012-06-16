package org.verba.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainPanel extends Activity {
	private OnClickListener openDictionaryButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenDictionary = new Intent(MainPanel.this, PhraseLookupActivity.class);
			startActivity(commandToOpenDictionary);
		}
	};
	private OnClickListener openCardSetPickerButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenDictionary = new Intent(MainPanel.this, CardSetPickerActivity.class);
			startActivity(commandToOpenDictionary);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_panel);

		setupOpenDictionaryButton();
		setupOpenCardSetPickerButton();
	}

	private void setupOpenCardSetPickerButton() {
		Button button = (Button) findViewById(R.id.openCardSetPickerButton);
		button.setOnClickListener(openCardSetPickerButtonListener);
	}

	private void setupOpenDictionaryButton() {
		Button button = (Button) findViewById(R.id.openDictionaryButton);
		button.setOnClickListener(openDictionaryButtonListener);
	}
}