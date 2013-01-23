package org.verba.mobile;

import static org.verba.mobile.utils.SelectionUtils.getPhraseMarkEnd;
import static org.verba.mobile.utils.SelectionUtils.getPhraseMarkStart;
import static org.verba.mobile.utils.SelectionUtils.hasPhraseMark;

import java.util.Iterator;

import org.verba.interactors.LookupPhrase;
import org.verba.mobile.task.LookupPhraseTask;
import org.verba.mobile.utils.WordUtils;
import org.verba.mobile.widget.PhraseDefinitionView;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionElement;
import org.verba.stardict.PhraseDefinitionElementDisplay;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.inject.Inject;

@ContentView(R.layout.phrase_definition_details)
public class PhraseDefinitionDetailsActivity extends VerbaActivity {
	private static final int MARGIN_5_IN_DIP = 5;
	private static final int MARGIN_20_IN_DIP = 20;
	public static final String PHRASE_TO_LOOKUP = "phraseToLookup";
	public static final String CARD_PHRASE_PARAMETER = "cardPhrase";
	public static final String CARD_DEFINITION_PARAMETER = "cardDefinition";
	private WordUtils wordUtils = new WordUtils();
	private int lastTapCharOffsetInItsBox;
	private ActionMode mMode;
	@InjectView(R.id.phraseDefinitionShowcase) private ViewGroup phraseDefinitionShowcase;
	@InjectView(R.id.phraseDefinitionWorkingArea) private ViewGroup phraseDefinitionWorkingArea;
	@Inject private LookupPhrase lookupPhrase;
	@InjectExtra(PHRASE_TO_LOOKUP) private String phraseToLookup;

	private OnLongClickListener phraseDefinitionDetailsViewLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View eventView) {
			hideAllSelectionHandles();
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) eventView;
			wordUtils.selectWordAtLastTapOffset(phraseDefinitionBox.getText(), lastTapCharOffsetInItsBox);
			phraseDefinitionBox.showSelectionHandles();
			if (mMode == null) {
				mMode = startActionMode(new AnActionModeOfEpicProportions());
			}
			return true;
		}
	};

	private OnTouchListener phraseDefinitionDetailsViewTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View eventView, MotionEvent event) {
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) eventView;
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				final int x = (int) event.getX();
				final int y = (int) event.getY();

				lastTapCharOffsetInItsBox = phraseDefinitionBox.getCharOffsetForCoordinates(x, y);
			}

			return false;
		}
	};

	private OnClickListener onUseSelectionButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent commandToOpenEditCardActivity = new Intent(PhraseDefinitionDetailsActivity.this, EditCardActivity.class);
			commandToOpenEditCardActivity.putExtra(CARD_PHRASE_PARAMETER, getPhraseToMemorize());
			commandToOpenEditCardActivity.putExtra(CARD_DEFINITION_PARAMETER, getSelection());
			startActivity(commandToOpenEditCardActivity);
		}
	};

	private OnClickListener onSearchButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			lookupAnotherPhrase(getSelection());
		}
	};

	private void hideAllSelectionHandles() {
		for (int i = 0; i < phraseDefinitionShowcase.getChildCount(); i++) {
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) phraseDefinitionShowcase.getChildAt(i);
			phraseDefinitionBox.removeSelectionWithHandles();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showLookingForDefinitionProgress();
		lookupPhraseDefinitions();
	}

	private void showLookingForDefinitionProgress() {
		//displayText("Looking for definition...");
	}

	private void lookupPhraseDefinitions() {
		new LookupPhraseTask(this, lookupPhrase, phraseToLookup).execute();
	}

	public void displayPhraseLookupFailure() {
		Toast.makeText(this, R.string.errorPhraseLookupFailed, Toast.LENGTH_SHORT).show();
	}

	private void displayText(CharSequence toDisplay) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		PhraseDefinitionView phraseDefinitionBox =
				(PhraseDefinitionView) inflater.inflate(R.layout.phrase_definition_box_template, null);

		setupWorkingAreaVisibleRectFor(phraseDefinitionBox);
		setupPhraseDefinitionBoxListeners(phraseDefinitionBox);
		phraseDefinitionBox.setText(toDisplay, BufferType.SPANNABLE);

		phraseDefinitionShowcase.addView(phraseDefinitionBox, getMarginLayoutParams());
	}

	private void setupWorkingAreaVisibleRectFor(PhraseDefinitionView phraseDefinitionBox) {
		Rect clip = new Rect();
		phraseDefinitionWorkingArea.getGlobalVisibleRect(clip);
		phraseDefinitionBox.setWorkingAreaVisibleRect(clip);
	}

	private LinearLayout.LayoutParams getMarginLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(MARGIN_5_IN_DIP, MARGIN_5_IN_DIP, MARGIN_5_IN_DIP, MARGIN_20_IN_DIP);
		return layoutParams;
	}

	private void setupPhraseDefinitionBoxListeners(PhraseDefinitionView phraseDefinitionBox) {
		phraseDefinitionBox.setOnLongClickListener(phraseDefinitionDetailsViewLongClickListener);
		phraseDefinitionBox.setOnTouchListener(phraseDefinitionDetailsViewTouchListener);
		phraseDefinitionBox.setOnUseSelectionButtonClick(onUseSelectionButtonClickListener);
		phraseDefinitionBox.setOnSearchButtonClick(onSearchButtonClickListener);
	}

	public void displayPhraseDefinition(PhraseDefinition phraseDefinition) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		PhraseDefinitionElementDisplay phraseDefinitionDisplay = new AndroidPhraseDefinitionElementDisplay(this, spannable);

		Iterator<PhraseDefinitionElement> phraseDefinitionElements = phraseDefinition.elements();
		while (phraseDefinitionElements.hasNext()) {
			PhraseDefinitionElement phraseDefinitionElement = phraseDefinitionElements.next();
			phraseDefinitionElement.print(phraseDefinitionDisplay);
		}

		displayText(spannable);
	}

	public void lookupAnotherPhrase(String anotherPhraseToLookup) {
		Intent commandToOpenPhraseDefinitionDetails = new Intent(this, PhraseDefinitionDetailsActivity.class);
		commandToOpenPhraseDefinitionDetails.putExtra(PHRASE_TO_LOOKUP, anotherPhraseToLookup);
		startActivity(commandToOpenPhraseDefinitionDetails);
	}

	private String getPhraseToMemorize() {
		View currentlyFocusedView = getCurrentFocus();
		if (currentlyFocusedView != null) {
			// FIX: the line below throws class cast to LinearLayout
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) currentlyFocusedView;
			if (hasPhraseMark((Spannable) phraseDefinitionBox.getText())) {
				int phraseMarkStart = getPhraseMarkStart((Spannable) phraseDefinitionBox.getText());
				int phraseMarkEnd = getPhraseMarkEnd((Spannable) phraseDefinitionBox.getText());
				return phraseDefinitionBox.getText().subSequence(phraseMarkStart, phraseMarkEnd).toString();
			}
		}

		return phraseToLookup;
	}

	private String getSelection() {
		View currentlyFocusedView = getCurrentFocus();
		if (currentlyFocusedView != null) {
			// FIX: the line below throws class cast to LinearLayout
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) currentlyFocusedView;
			int selectionStart = phraseDefinitionBox.getSelectionStart();
			int selectionEnd = phraseDefinitionBox.getSelectionEnd();
			return phraseDefinitionBox.getText().subSequence(selectionStart, selectionEnd).toString();
		} else {
			return null;
		}
	}

	private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			menu.add("Save").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			menu.add("Search").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			menu.add("Refresh").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			menu.add("Save").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			menu.add("Search").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			menu.add("Refresh").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Toast.makeText(PhraseDefinitionDetailsActivity.this, "Got click: " + item, Toast.LENGTH_SHORT).show();
			mode.finish();
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mMode = null;
		}
	}
}