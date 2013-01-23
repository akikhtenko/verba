package org.verba.mobile;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

public abstract class VerbaActivity extends RoboSherlockActivity {
	public static final int DEVICE_VERSION   = Build.VERSION.SDK_INT;
	public static final int DEVICE_HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;

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
	private OnClickListener openDictionariesManagerButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenDictionariesManager = new Intent(VerbaActivity.this, DictionariesLoaderActivity.class);
			startActivity(commandToOpenDictionariesManager);
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case R.id.searchMenuItem:
			openScreen(PhraseLookupActivity.class);
			return true;
		case R.id.cardsMenuItem:
			openScreen(CardSetPickerActivity.class);
			return true;
		case R.id.dictionariesMenuItem:
			openScreen(DictionariesLoaderActivity.class);
			return true;
		case R.id.settingsMenuItem:
			openScreen(PhraseLookupActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}

	@SuppressWarnings("rawtypes")
	private void openScreen(Class clazz) {
		Intent commandToOpenDictionary = new Intent(VerbaActivity.this, clazz);
		startActivity(commandToOpenDictionary);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		hackSherlockActionBarToShowOverlayAtAllTimes();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.menu);

		setContentView(getContentLayout());
		setupOpenDictionaryButton();
		setupOpenCardSetPickerButton();
		setupOpenDictionariesManagerButton();
	}

	private void hackSherlockActionBarToShowOverlayAtAllTimes() {
		if (DEVICE_VERSION >= DEVICE_HONEYCOMB) {
			try {
				ViewConfiguration config = ViewConfiguration.get(this);
				Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
				if (menuKeyField != null) {
					menuKeyField.setAccessible(true);
					menuKeyField.setBoolean(config, false);
				}
			} catch (Exception ex) {
				Log.e("Verba", "ABS Overflow Menu forcing hack failed", ex);
			}
		}
	}

	private void setupOpenCardSetPickerButton() {
		ImageButton button = (ImageButton) findViewById(R.id.cardsMenuButton);
		button.setOnClickListener(openCardSetPickerButtonListener);
	}

	private void setupOpenDictionaryButton() {
		ImageButton button = (ImageButton) findViewById(R.id.dictionaryMenuButton);
		button.setOnClickListener(openDictionaryButtonListener);
	}

	private void setupOpenDictionariesManagerButton() {
		ImageButton button = (ImageButton) findViewById(R.id.dictionariesMenuButton);
		button.setOnClickListener(openDictionariesManagerButtonListener);
	}

	protected abstract int getContentLayout();

	protected abstract boolean loadSystemMenu();

	/**
	 * The second half of the hack to always show the action bar overlay button
	 * @see check out hackSherlockActionBarToShowOverlayAtAllTimes() to find the first half
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (DEVICE_VERSION < DEVICE_HONEYCOMB) {
			if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
				openOptionsMenu();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

}
