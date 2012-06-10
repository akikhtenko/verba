package org.verba.mobile;

import org.verba.mobile.task.LookupPhraseDefinitionTask;
import org.verba.mobile.utils.WordUtils;
import org.verba.mobile.widget.PhraseDefinitionView;
import org.verba.mobile.xdxf.AndroidXdxfNodeDisplay;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;
import org.verba.xdxf.XdxfPhraseDefinitionPart;
import org.verba.xdxf.node.XdxfElement;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView.BufferType;

public class PhraseDefinitionDetailsActivity extends Activity {
	private WordUtils wordUtils;
	private int lastTapCharOffset;
	private PhraseDefinitionView phraseDefinitionDetailsView;

	private OnLongClickListener phraseDefinitionDetailsViewLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			wordUtils.selectWordAtLastTapOffset(phraseDefinitionDetailsView.getText(), lastTapCharOffset);
			phraseDefinitionDetailsView.showSelectionHandles();
			return true;
		}
	};

	private OnTouchListener phraseDefinitionDetailsViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				final int x = (int) event.getX();
				final int y = (int) event.getY();

				lastTapCharOffset = phraseDefinitionDetailsView.getCharOffsetForCoordinates(x, y);
			}

			return false;
		}
	};

	public PhraseDefinitionDetailsActivity() {
		wordUtils = new WordUtils();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_definition_details);

		setupPhraseDefinitionDetailsView();

		displayText("Looking for definition...");
		lookupPhraseDefinition();
	}

	private void setupPhraseDefinitionDetailsView() {
		phraseDefinitionDetailsView = (PhraseDefinitionView) findViewById(R.id.phraseDefinitionView);

		phraseDefinitionDetailsView.setOnLongClickListener(phraseDefinitionDetailsViewLongClickListener);
		phraseDefinitionDetailsView.setOnTouchListener(phraseDefinitionDetailsViewTouchListener);
	}

	public void displayPhraseDefinition(PhraseDefinition phraseDefinition) {
		XdxfPhraseDefinitionPart phraseDefinitionPart = (XdxfPhraseDefinitionPart) phraseDefinition.iterator().next();
		displayText(asSpannableString(phraseDefinitionPart.asXdxfArticle()));
	}

	private void lookupPhraseDefinition() {
		PhraseDefinitionCoordinates phraseDefinitionCoordinates = (PhraseDefinitionCoordinates) getIntent()
				.getSerializableExtra("wordToLookup");

		new LookupPhraseDefinitionTask(this).execute(phraseDefinitionCoordinates);
	}

	private void displayText(CharSequence toDisplay) {
		phraseDefinitionDetailsView.setText(toDisplay, BufferType.SPANNABLE);
	}

	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();

		AndroidXdxfNodeDisplay xdxfDisplay = new AndroidXdxfNodeDisplay(this, spannable);
		xdxfArticle.print(xdxfDisplay);

		return spannable;
	}
}