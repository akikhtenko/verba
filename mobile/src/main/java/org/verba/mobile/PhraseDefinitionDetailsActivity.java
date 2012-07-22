package org.verba.mobile;

import static java.lang.String.format;

import java.util.List;

import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDataObject;
import org.verba.mobile.stardict.DictionaryEntryDataObject;
import org.verba.mobile.task.LookupPhraseDefinitionCoordinatesTask;
import org.verba.mobile.task.LookupPhraseDefinitionTask;
import org.verba.mobile.task.LookupPhraseDefinitionTask.Request;
import org.verba.mobile.task.LookupPhraseDefinitionTask.Response;
import org.verba.mobile.utils.WordUtils;
import org.verba.mobile.widget.PhraseDefinitionView;
import org.verba.mobile.xdxf.AndroidXdxfNodeDisplay;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionPart;
import org.verba.xdxf.XdxfPhraseDefinitionElement;
import org.verba.xdxf.node.XdxfElement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class PhraseDefinitionDetailsActivity extends DictionaryActivity {
	public static final String PHRASE_TO_LOOKUP = "phraseToLookup";
	public static final String CARD_PHRASE_PARAMETER = "cardPhrase";
	public static final String CARD_DEFINITION_PARAMETER = "cardDefinition";
	private WordUtils wordUtils;
	private int lastTapCharOffsetInItsBox;
	private ViewGroup phraseDefinitionsShowcase;
	private boolean definitionsLookupRequested;

	private OnLongClickListener phraseDefinitionDetailsViewLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View eventView) {
			PhraseDefinitionView phraseDefinitionBox = (PhraseDefinitionView) eventView;
			wordUtils.selectWordAtLastTapOffset(phraseDefinitionBox.getText(), lastTapCharOffsetInItsBox);
			phraseDefinitionBox.showSelectionHandles();
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
			commandToOpenEditCardActivity.putExtra(CARD_PHRASE_PARAMETER, getIntent().getStringExtra(PHRASE_TO_LOOKUP));
			commandToOpenEditCardActivity.putExtra(CARD_DEFINITION_PARAMETER, getSelection());
			startActivity(commandToOpenEditCardActivity);
		}
	};

	public PhraseDefinitionDetailsActivity() {
		wordUtils = new WordUtils();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupPhraseDefinitionShowcase();
		showLookingForDefinitionProgress();
	}

	private void showLookingForDefinitionProgress() {
		//displayText("Looking for definition...");
	}

	@Override
	protected void postDictionaryServiceConnected() {
		if (!definitionsLookupRequested) {
			definitionsLookupRequested = true;
			lookupPhraseDefinitionCoordinates();
		}
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.phrase_definition_details;
	}

	private void setupPhraseDefinitionShowcase() {
		phraseDefinitionsShowcase = (ViewGroup) findViewById(R.id.phraseDefinitionsShowcase);
	}

	private void lookupPhraseDefinitionCoordinates() {
		new LookupPhraseDefinitionCoordinatesTask(this, dictionaryEntryDao).execute(
				getIntent().getStringExtra(PHRASE_TO_LOOKUP));
	}

	private void displayText(CharSequence toDisplay) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		PhraseDefinitionView phraseDefinitionBox =
				(PhraseDefinitionView) inflater.inflate(R.layout.phrase_definition_box_template, null);

		setupPhraseDefinitionBoxListeners(phraseDefinitionBox);
		phraseDefinitionBox.setText(toDisplay, BufferType.SPANNABLE);
		phraseDefinitionsShowcase.addView(phraseDefinitionBox);
	}

	private void setupPhraseDefinitionBoxListeners(PhraseDefinitionView phraseDefinitionBox) {
		phraseDefinitionBox.setOnLongClickListener(phraseDefinitionDetailsViewLongClickListener);
		phraseDefinitionBox.setOnTouchListener(phraseDefinitionDetailsViewTouchListener);
		phraseDefinitionBox.setOnUseSelectionButtonClick(onUseSelectionButtonClickListener);
	}

	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();

		AndroidXdxfNodeDisplay xdxfDisplay = new AndroidXdxfNodeDisplay(this, spannable);
		xdxfArticle.print(xdxfDisplay);

		return spannable;
	}

	public void lookupPhraseDefinition(List<DictionaryEntryDataObject> dictionaryEntries) {
		for (DictionaryEntryDataObject dictionaryEntry : dictionaryEntries) {
			DictionaryDataObject dictionary = getDictionaryFor(dictionaryEntry);
			Request phraseDefinitionLookupRequest =
							new Request(dictionary.getName(), dictionaryEntry.asPhraseDefinitionCoordinates());
			new LookupPhraseDefinitionTask(this).execute(phraseDefinitionLookupRequest);
		}
	}

	private DictionaryDataObject getDictionaryFor(DictionaryEntryDataObject dictionaryEntry) {
		try {
			return dictionaryDao.getDictionaryById(dictionaryEntry.getDictionaryId());
		} catch (NoDictionaryFoundException e) {
			throw new RuntimeException(format("Dictionary couldn't be found by id[%s]", dictionaryEntry.getDictionaryId()), e);
		} catch (MoreThanOneDictionaryFoundException e) {
			throw new RuntimeException(format("Multiple dictionaries found by id[%s]", dictionaryEntry.getDictionaryId()), e);
		}
	}

	public void displayPhraseDefinitionCoordinatesNotFound() {
		Toast.makeText(this, R.string.validationNoPhraseDefinitionCoordinatesFound, Toast.LENGTH_SHORT).show();
	}

	public void displayPhraseDefinition(Response phraseDefinitionLookupResponse) {
		PhraseDefinition phraseDefinition = phraseDefinitionLookupResponse.getPhraseDefinition();
		PhraseDefinitionPart phraseDefinitionPart = phraseDefinition.parts().next();
		XdxfPhraseDefinitionElement phraseDefinitionElement =
				(XdxfPhraseDefinitionElement) phraseDefinitionPart.elements().next();
		displayText(asSpannableString(phraseDefinitionElement.asXdxfArticle()));
	}

	public void lookupAnotherPhrase(String phraseToLookup) {
		Intent commandToOpenPhraseDefinitionDetails = new Intent(this, PhraseDefinitionDetailsActivity.class);
		commandToOpenPhraseDefinitionDetails.putExtra(PHRASE_TO_LOOKUP, phraseToLookup);
		startActivity(commandToOpenPhraseDefinitionDetails);
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
}