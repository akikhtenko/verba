package org.verba.mobile.xdxf;

import java.util.regex.Pattern;

import org.verba.mobile.R;
import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.xdxf.XdxfNodeDisplay;
import org.verba.xdxf.node.BoldPhrase;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.ItalicPhrase;
import org.verba.xdxf.node.KeyPhrase;
import org.verba.xdxf.node.PhraseReference;
import org.verba.xdxf.node.PlainText;
import org.verba.xdxf.node.XdxfNode;

import android.content.Context;
import android.content.Intent;
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
	private SpannableStringBuilder spannable;
	private Context activityContext;
	Pattern rgbColorPattern = Pattern.compile("^#\\d{6}$");

	public AndroidXdxfNodeDisplay(Context anActivityContext, SpannableStringBuilder aSpannable) {
		activityContext = anActivityContext;
		spannable = aSpannable;
	}

	@Override
	public void print(PlainText plainText) {
		spannable.append(plainText.asPlainText());
	}

	@Override
	public void print(KeyPhrase keyPhrase) {
		applySpan(new TextAppearanceSpan(activityContext, R.style.KeyPhrase), keyPhrase);
	}

	@Override
	public void print(ColoredPhrase coloredPhrase) {
		int coloredPhraseStyle = getColoredPhraseStyleResource(coloredPhrase.getColorCode());

		applySpan(new ForegroundColorSpan(coloredPhraseStyle), coloredPhrase);
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
				Intent commandToOpenPhraseDefinitionDetails = new Intent(activityContext,
						PhraseDefinitionDetailsActivity.class);
				commandToOpenPhraseDefinitionDetails.putExtra("wordToLookup", phraseReference.asPlainText());
				activityContext.startActivity(commandToOpenPhraseDefinitionDetails);
			}
		};

		applySpan(linkToAnotherDefinition, phraseReference);
	}

	private void applySpan(CharacterStyle characterStyle, XdxfNode xdxfNode) {
		spannable.setSpan(characterStyle, spannable.length() - xdxfNode.getContentLength(), spannable.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	}

	private int getColoredPhraseStyleResource(String colorCode) {
		int coloredPhraseStyle = activityContext.getResources().getIdentifier(colorCode.toLowerCase(), "color",
				activityContext.getApplicationInfo().packageName);

		if (coloredPhraseStyle == 0) {
			if (rgbColorPattern.matcher(colorCode).matches()) {
				coloredPhraseStyle = Color.parseColor(colorCode);
			} else {
				coloredPhraseStyle = activityContext.getResources().getColor(R.color.default_colored_phrase);
			}
		} else {
			coloredPhraseStyle = activityContext.getResources().getColor(coloredPhraseStyle);
		}
		return coloredPhraseStyle;
	}
}
