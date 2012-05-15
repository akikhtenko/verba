package org.verba.mobile;

import static org.verba.xdxf.node.XdxfNodeType.PLAIN_TEXT;

import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.verba.stardict.WordDefinition;
import org.verba.xdxf.XdxfWordDefinitionPart;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.verba.xdxf.node.XdxfNode;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class WordDefinitionDetailsActivity extends Activity {
	Pattern rgbColorPattern = Pattern.compile("^#\\d{6}$");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_definition_details);

		doDosplay("Looking for definition...");

		new LookupWordDefinitionTask().execute(getIntent().getStringExtra("wordToLookup"));
	}

	private void desplayWordDefinition(WordDefinition wordDefinition) {
		if (wordDefinition == null) {
			doDosplay("Nothing found in the dictionary");
		} else {
			XdxfWordDefinitionPart wordDefinitionPart = (XdxfWordDefinitionPart) wordDefinition.iterator().next();
			doDosplay(asSpannableString(wordDefinitionPart.asXdxfArticle()));
		}
	}

	private void doDosplay(CharSequence toDisplay) {
		TextView wordDefinitionDetailsView = (TextView) findViewById(R.id.wordDefinitionView);
		wordDefinitionDetailsView.setMovementMethod(new ScrollingMovementMethod());
		wordDefinitionDetailsView.setText(toDisplay, BufferType.SPANNABLE);
	}

	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();

		processChild(xdxfArticle, spannable);

		return spannable;
	}

	private int processChild(XdxfNode xdxfNode, SpannableStringBuilder spannable) {
		if (xdxfNode.getType() != PLAIN_TEXT) {
			int totalChildrenLength = 0;
			for (Iterator<XdxfNode> i = ((XdxfElement) xdxfNode).iterator(); i.hasNext();) {
				XdxfNode nextNode = i.next();

				totalChildrenLength += processChild(nextNode, spannable);
			}

			switch (xdxfNode.getType()) {
			case KEY_PHRASE:
				spannable.setSpan(new TextAppearanceSpan(this, R.style.KeyPhrase), spannable.length()
						- totalChildrenLength, spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				break;
			case BOLD_PHRASE:
				spannable.setSpan(new TextAppearanceSpan(this, R.style.BoldPhrase), spannable.length()
						- totalChildrenLength, spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				break;
			case COLORED_PHRASE:
				int coloredPhraseStyle = getColoredPhraseStyleResource(((ColoredPhrase) xdxfNode).getColorCode());

				spannable.setSpan(new ForegroundColorSpan(coloredPhraseStyle),
						spannable.length() - totalChildrenLength, spannable.length(),
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				break;
			default:
				break;
			}

			return totalChildrenLength;
		} else {
			String plainTextNodeValue = xdxfNode.asPlainText();
			spannable.append(plainTextNodeValue);

			return plainTextNodeValue.length();
		}
	}

	private int getColoredPhraseStyleResource(String colorCode) {
		int coloredPhraseStyle =
				getResources().getIdentifier(colorCode.toLowerCase(), "color", getApplicationInfo().packageName);

		if (coloredPhraseStyle == 0) {
			if (rgbColorPattern.matcher(colorCode).matches()) {
				coloredPhraseStyle = Color.parseColor(colorCode);
			} else {
				coloredPhraseStyle = getResources().getColor(R.color.default_colored_phrase);
			}
		} else {
			coloredPhraseStyle = getResources().getColor(coloredPhraseStyle);
		}
		return coloredPhraseStyle;
	}

	private class LookupWordDefinitionTask extends AsyncTask<String, Void, WordDefinition> {
		protected WordDefinition doInBackground(String... wordsToLookup) {
			try {
				return new WordDefinitionLookup().lookupWordDefinition(wordsToLookup[0]);
			} catch (IOException e) {
				throw new RuntimeException(String.format("Unexpected error while looking up [%s]", wordsToLookup[0]), e);
			}
		}

		protected void onPostExecute(WordDefinition wordDefinitionFound) {
			desplayWordDefinition(wordDefinitionFound);
		}
	}
}