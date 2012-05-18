package org.verba.mobile.xdxf;

import java.util.regex.Pattern;

import org.verba.mobile.R;
import org.verba.xdxf.XdxfNodeDisplay;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.KeyPhrase;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;

public class AndroidXdxfNodeDisplay implements XdxfNodeDisplay {
	private SpannableStringBuilder spannable;
	private Context activityContext;
	Pattern rgbColorPattern = Pattern.compile("^#\\d{6}$");

	public AndroidXdxfNodeDisplay(Context anActivityContext, SpannableStringBuilder aSpannable) {
		activityContext = anActivityContext;
		spannable = aSpannable;
	}

	public void print(String plainText) {
		spannable.append(plainText);
	}

	public void print(KeyPhrase keyPhrase) {
		spannable.setSpan(new TextAppearanceSpan(activityContext, R.style.KeyPhrase),
				spannable.length() - keyPhrase.getContentLength(), spannable.length(),
				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	}

	public void print(ColoredPhrase coloredPhrase) {
		int coloredPhraseStyle = getColoredPhraseStyleResource(coloredPhrase.getColorCode());

		spannable.setSpan(new ForegroundColorSpan(coloredPhraseStyle),
				spannable.length() - coloredPhrase.getContentLength(),
				spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
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
