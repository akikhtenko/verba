package org.verba.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public abstract class VerbaActivity extends Activity {
	private OnClickListener openDictionaryButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenDictionary = new Intent(VerbaActivity.this, PhraseLookupActivity.class);
			startActivity(commandToOpenDictionary);
		}
	};
	private OnClickListener openCardSetPickerButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenDictionary = new Intent(VerbaActivity.this, CardSetPickerActivity.class);
			startActivity(commandToOpenDictionary);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (loadSystemMenu()) {
			inflateSystemMenu();
		} else {
			setContentView(getContentLayout());
		}
	}

	private void inflateSystemMenu() {
		setContentView(getContentViewWithMenu());

		setupOpenDictionaryButton();
		setupOpenCardSetPickerButton();
	}

	protected View getContentViewWithMenu() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.menu, null);

		ViewGroup layoutContentView = (ViewGroup) parent.findViewById(R.id.layoutContent);

		View contentView = inflater.inflate(getContentLayout(), null);
		layoutContentView.addView(contentView);

		return parent;
	}

	private void setupOpenCardSetPickerButton() {
		Button button = (Button) findViewById(R.id.cardsMenuButton);
		button.setOnClickListener(openCardSetPickerButtonListener);
	}

	private void setupOpenDictionaryButton() {
		ImageButton button = (ImageButton) findViewById(R.id.dictionaryMenuButton);
		button.setOnClickListener(openDictionaryButtonListener);
	}

	protected abstract int getContentLayout();

	protected abstract boolean loadSystemMenu();
}
