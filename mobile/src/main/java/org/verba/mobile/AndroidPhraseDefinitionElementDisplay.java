package org.verba.mobile;

import org.verba.html.HtmlPhraseDefinitionElement;
import org.verba.mobile.xdxf.AndroidXdxfNodeDisplay;
import org.verba.stardict.PhraseDefinitionElementDisplay;
import org.verba.stardict.PlainMeaningPhraseDefinitionElement;
import org.verba.xdxf.XdxfPhraseDefinitionElement;
import org.verba.xdxf.node.XdxfElement;

import android.text.SpannableStringBuilder;

public class AndroidPhraseDefinitionElementDisplay implements PhraseDefinitionElementDisplay {
	private SpannableStringBuilder spannable;
	private PhraseDefinitionDetailsActivity activity;

	public AndroidPhraseDefinitionElementDisplay(PhraseDefinitionDetailsActivity aDictionaryActivity, SpannableStringBuilder aSpannable) {
		activity = aDictionaryActivity;
		spannable = aSpannable;
	}

	@Override
	public void print(XdxfPhraseDefinitionElement xdxfPhraseDefinitionElement) {
		XdxfElement xdxfArticle = xdxfPhraseDefinitionElement.asXdxfArticle();

		AndroidXdxfNodeDisplay xdxfDisplay = new AndroidXdxfNodeDisplay(activity, spannable);
		xdxfArticle.print(xdxfDisplay);
	}

	@Override
	public void print(HtmlPhraseDefinitionElement htmlPhraseDefinitionElement) {
		spannable.append(new String(htmlPhraseDefinitionElement.bytes()));
	}

	@Override
	public void print(PlainMeaningPhraseDefinitionElement plainMeaningPhraseDefinitionElement) {
		spannable.append(new String(plainMeaningPhraseDefinitionElement.bytes()));
	}

}
