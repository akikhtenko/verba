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
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class WordDefinitionDetailsActivity extends Activity {
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
		TextView wordDefinitionDetailsView = (TextView) findViewById(R.id.wordDefinitionView);
		wordDefinitionDetailsView.setMovementMethod(LinkMovementMethod.getInstance());
		wordDefinitionDetailsView.setText(toDisplay, BufferType.SPANNABLE);
	}

	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();

		AndroidXdxfNodeDisplay xdxfDisplay = new AndroidXdxfNodeDisplay(this, spannable);
		xdxfArticle.print(xdxfDisplay);

		return spannable;
	}

	private class LookupWordDefinitionTask extends AsyncTask<String, Void, WordDefinition> {

		protected WordDefinition doInBackground(String... wordsToLookup) {
			try {
				return new WordDefinitionLookup().lookupWordDefinition(wordsToLookup[0]);
			} catch (WordDefinitionCoordinatesNotFoundException e) {
				return null;
			} catch (IOException e) {
				throw new RuntimeException(String.format("Unexpected error while looking up [%s]", wordsToLookup[0]), e);
			}
		}

		protected void onPostExecute(WordDefinition wordDefinitionFound) {
			if (wordDefinitionFound == null) {
				displayTextOnTheScreen("Nothing found in the dictionary");
			} else {
				displayWordDefinition(wordDefinitionFound);
			}
		}
	}
}