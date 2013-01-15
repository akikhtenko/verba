package org.verba.mobile.xdxf;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.util.regex.Pattern;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.R;
import org.verba.xdxf.XdxfNodeDisplay;
import org.verba.xdxf.node.BoldPhrase;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.ItalicPhrase;
import org.verba.xdxf.node.KeyPhrase;
import org.verba.xdxf.node.PhraseReference;
import org.verba.xdxf.node.PlainText;
import org.verba.xdxf.node.XdxfNode;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;

public class AndroidXdxfNodeDisplay implements XdxfNodeDisplay {
	private static final char INVISIBLE_CHAR_PREVENTING_UNDESIRABLE_CLICKS = '\u2063';
	private SpannableStringBuilder spannable;
	private PhraseDefinitionDetailsActivity activity;
	Pattern rgbColorPattern = Pattern.compile("^#\\d{6}$");

	public AndroidXdxfNodeDisplay(PhraseDefinitionDetailsActivity aDictionaryActivity, SpannableStringBuilder aSpannable) {
		activity = aDictionaryActivity;
		spannable = aSpannable;
	}

	@Override
	public void print(PlainText plainText) {
		spannable.append(plainText.asPlainText());
	}

	@Override
	public void print(KeyPhrase keyPhrase) {
		applySpan(new TextAppearanceSpan(activity, R.style.KeyPhrase), keyPhrase);
	}

	@Override
	public void print(ColoredPhrase coloredPhrase) {
		if (isNotEmpty(coloredPhrase.getColorCode())) {
			int coloredPhraseStyle = getColoredPhraseStyleResource(coloredPhrase.getColorCode());
			applySpan(new ForegroundColorSpan(coloredPhraseStyle), coloredPhrase);
		}
	}

	@Override
	public void print(BoldPhrase boldPhrase) {
		applySpan(new StyleSpan(Typeface.BOLD), boldPhrase);
	}

	@Override
	public void print(ItalicPhrase italicPhrase) {
		applySpan(new StyleSpan(Typeface.ITALIC), italicPhrase);
	}

	@Override
	public void print(final PhraseReference phraseReference) {
		CharacterStyle linkToAnotherDefinition = new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				activity.lookupAnotherPhrase(phraseReference.asPlainText());
			}
		};

		applySpan(linkToAnotherDefinition, phraseReference);
		spannable.append(INVISIBLE_CHAR_PREVENTING_UNDESIRABLE_CLICKS);
	}

	private void applySpan(CharacterStyle characterStyle, XdxfNode xdxfNode) {
		spannable.setSpan(characterStyle, spannable.length() - xdxfNode.getContentLength(), spannable.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	}

	private int getColoredPhraseStyleResource(String colorCode) {
		int coloredPhraseStyle = activity.getResources().getIdentifier(colorCode.toLowerCase(), "color",
				activity.getApplicationInfo().packageName);

		if (coloredPhraseStyle == 0) {
			if (rgbColorPattern.matcher(colorCode).matches()) {
				coloredPhraseStyle = Color.parseColor(colorCode);
			} else {
				coloredPhraseStyle = activity.getResources().getColor(R.color.default_colored_phrase);
			}
		} else {
			coloredPhraseStyle = activity.getResources().getColor(coloredPhraseStyle);
		}
		return coloredPhraseStyle;
	}
}
