package org.verba.mobile.widget;

import static org.verba.mobile.widget.HandleView.VerticalPosition.BOTTOM;
import android.text.Selection;
import android.text.Spannable;

public class RightHandleView extends HandleView {

	public RightHandleView(PhraseDefinitionView phraseDefinitionView) {
		super(phraseDefinitionView);
	}

	@Override
	protected float getHotSpot(int handleWidth) {
		return handleWidth / 2;
	}

	@Override
	protected void updateSelectionAfterHandleMove(int x, int y) {
		int selectionStart = textView.getSelectionStart();
		int selectionEnd = textView.getSelectionEnd();
		int offset = textView.getHysteresisCharOffsetForCoordinates(x, y, selectionEnd);

		if (selectionEnd == offset || offset < selectionStart) {
			return; // no change, no need to redraw;
		}
		// If the user "closes" the selection entirely they were probably trying to
		// select a single character. Help them out.
		if (offset == selectionStart) {
			offset = selectionStart + 1;
		}
		Selection.setSelection((Spannable) textView.getText(), selectionStart, offset);
		setPositionAt(offset);
	}

	@Override
	protected boolean mirrorDrawable() {
		return false;
	}

	@Override
	protected VerticalPosition getVerticalPosition() {
		return BOTTOM;
	}
}
