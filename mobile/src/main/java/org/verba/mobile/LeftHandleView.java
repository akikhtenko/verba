package org.verba.mobile;

import static org.verba.mobile.HandleView.VerticalPosition.BOTTOM;
import android.text.Selection;
import android.text.Spannable;

public class LeftHandleView extends HandleView {

	public LeftHandleView(VerbaTextView verbaTextView, SelectionActionsView aSelectionActionsView) {
		super(verbaTextView, aSelectionActionsView);
	}

	@Override
	protected float getHotSpot(int handleWidth) {
		return handleWidth / 2;
	}

	@Override
	protected void updateSelectionAfterHandleMove(int x, int y) {
		int selectionStart = textView.getSelectionStart();
		int selectionEnd = textView.getSelectionEnd();
		int offset = textView.getHysteresisCharOffsetForCoordinates(x, y, selectionStart);

		if (selectionStart == offset || offset > selectionEnd) {
			return; // no change, no need to redraw;
		}
		// If the user "closes" the selection entirely they were probably trying to
		// select a single character. Help them out.
		if (offset == selectionEnd) {
			offset = selectionEnd - 1;
		}
		Selection.setSelection((Spannable) textView.getText(), offset, selectionEnd);
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
