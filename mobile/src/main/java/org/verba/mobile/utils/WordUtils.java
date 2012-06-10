package org.verba.mobile.utils;

import android.text.Selection;
import android.text.Spannable;

public class WordUtils {
	private static final int MAX_LETTERS_IN_A_WORD = 48;

	public void selectWordAtLastTapOffset(CharSequence viewText, int offset) {
		int len = viewText.length();
		int end = Math.min(offset, len);

		if (end < 0) {
			return;
		}

		int start = end;

		for (; start > 0; start--) {
			char c = viewText.charAt(start - 1);
			int type = Character.getType(c);

			if (c != '\''
					&& type != Character.UPPERCASE_LETTER
					&& type != Character.LOWERCASE_LETTER
					&& type != Character.TITLECASE_LETTER
					&& type != Character.MODIFIER_LETTER
					&& type != Character.DECIMAL_DIGIT_NUMBER) {
				break;
			}
		}

		for (; end < len; end++) {
			char c = viewText.charAt(end);
			int type = Character.getType(c);

			if (c != '\''
					&& type != Character.UPPERCASE_LETTER
					&& type != Character.LOWERCASE_LETTER
					&& type != Character.TITLECASE_LETTER
					&& type != Character.MODIFIER_LETTER
					&& type != Character.DECIMAL_DIGIT_NUMBER) {
				break;
			}
		}

		if (start == end || end - start > MAX_LETTERS_IN_A_WORD) {
			return;
		}

		boolean hasLetter = false;
		for (int i = start; i < end; i++) {
			if (Character.isLetter(viewText.charAt(i))) {
				hasLetter = true;
				break;
			}
		}

		if (!hasLetter) {
			return;
		}

		Selection.setSelection((Spannable) viewText, start, end);
	}
}
