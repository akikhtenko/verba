package org.verba.mobile.utils;

import static android.text.Spanned.SPAN_INTERMEDIATE;
import static android.text.Spanned.SPAN_POINT_POINT;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;

public final class SelectionUtils {
	private static final class START implements NoCopySpan { }
	private static final class END implements NoCopySpan { }
	public static final Object PHRASE_MARK_START = new START();
	public static final Object PHRASE_MARK_END = new END();

	private SelectionUtils() {
	}

	public static int getPhraseMarkStart(Spannable spannable) {
		return spannable.getSpanStart(PHRASE_MARK_START);
	}

	public static int getPhraseMarkEnd(Spannable spannable) {
		return spannable.getSpanStart(PHRASE_MARK_END);
	}

	public static void setSelectionAsPhrase(Spannable spannable) {
		int selectionStart = Selection.getSelectionStart(spannable);
		int selectionEnd = Selection.getSelectionEnd(spannable);

		int currentPhraseMarkStart = getPhraseMarkStart(spannable);
		int currentPhraseMarkEnd = getPhraseMarkEnd(spannable);

		if (currentPhraseMarkStart != selectionStart || currentPhraseMarkEnd != selectionEnd) {
			spannable.setSpan(PHRASE_MARK_START, selectionStart, selectionStart, SPAN_POINT_POINT | SPAN_INTERMEDIATE);
			spannable.setSpan(PHRASE_MARK_END, selectionEnd, selectionEnd, SPAN_POINT_POINT);
		}
	}

	public static void removePhraseMark(Spannable spannable) {
		spannable.removeSpan(PHRASE_MARK_START);
		spannable.removeSpan(PHRASE_MARK_END);
	}

	public static boolean hasPhraseMark(Spannable spannable) {
		int currentPhraseMarkStart = getPhraseMarkStart(spannable);
		int currentPhraseMarkEnd = getPhraseMarkEnd(spannable);

		return currentPhraseMarkStart >= 0 && currentPhraseMarkStart != currentPhraseMarkEnd;
	}
}
