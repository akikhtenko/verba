package org.verba.mobile;

import java.io.IOException;

import org.verba.mobile.xdxf.AndroidXdxfNodeDisplay;
import org.verba.stardict.WordDefinition;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;
import org.verba.xdxf.XdxfWordDefinitionPart;
import org.verba.xdxf.node.XdxfElement;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView.BufferType;

public class WordDefinitionDetailsActivity extends Activity {
	private WordUtils wordUtils;
	private int lastTapCharOffset;

	public WordDefinitionDetailsActivity() {
		wordUtils = new WordUtils();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_definition_details);

		displayTextOnTheScreen("Looking for definition...");

		new LookupWordDefinitionTask().execute(getIntent().getStringExtra("wordToLookup"));
	}

	private void displayWordDefinition(WordDefinition wordDefinition) {
		XdxfWordDefinitionPart wordDefinitionPart = (XdxfWordDefinitionPart) wordDefinition.iterator().next();
		displayTextOnTheScreen(asSpannableString(wordDefinitionPart.asXdxfArticle()));
	}

	private void displayTextOnTheScreen(CharSequence toDisplay) {
		final VerbaTextView wordDefinitionDetailsView = (VerbaTextView) findViewById(R.id.wordDefinitionView);
		wordDefinitionDetailsView.setText(toDisplay, BufferType.SPANNABLE);

		wordDefinitionDetailsView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				wordUtils.selectWordAtLastTapOffset(wordDefinitionDetailsView.getText(), lastTapCharOffset);
				wordDefinitionDetailsView.showSelectionHandles();
				return true;
			}
		});

		wordDefinitionDetailsView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					final int x = (int) event.getX();
					final int y = (int) event.getY();

					lastTapCharOffset = wordDefinitionDetailsView.getCharOffsetForCoordinates(x, y);
				}

				return false;
			}
		});
	}

	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();

		AndroidXdxfNodeDisplay xdxfDisplay = new AndroidXdxfNodeDisplay(this, spannable);
		xdxfArticle.print(xdxfDisplay);

		return spannable;
	}
//
//	private void setupWordSelectionListener() {
//		TextView wordDefinitionDetailsView = (TextView) findViewById(R.id.wordDefinitionView);
//
//		wordDefinitionDetailsView.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				Layout layout = ((TextView) v).getLayout();
//				int x = (int) event.getX();
//				int y = (int) event.getY();
//				if (layout != null) {
//					int line = layout.getLineForVertical(y);
//					int charIndex = layout.getOffsetForHorizontal(line, x);
//				}
//				return true;
//			}
//		});
//	}

	private class LookupWordDefinitionTask extends AsyncTask<String, Void, WordDefinition> {

		@Override
		protected WordDefinition doInBackground(String... wordsToLookup) {
			try {
				return new WordDefinitionLookup().lookupWordDefinition(wordsToLookup[0]);
			} catch (WordDefinitionCoordinatesNotFoundException e) {
				return null;
			} catch (IOException e) {
				throw new RuntimeException(String.format("Unexpected error while looking up [%s]", wordsToLookup[0]), e);
			}
		}

		@Override
		protected void onPostExecute(WordDefinition wordDefinitionFound) {
			if (wordDefinitionFound == null) {
				displayTextOnTheScreen("Nothing found in the dictionary");
			} else {
				displayWordDefinition(wordDefinitionFound);
			}
		}
	}
}