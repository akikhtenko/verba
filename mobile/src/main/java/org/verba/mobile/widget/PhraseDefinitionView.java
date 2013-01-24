package org.verba.mobile.widget;


import org.verba.mobile.utils.SelectionUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.TextView;

public class PhraseDefinitionView extends TextView implements OnScrollChangedListener {
	private static final int HYSTERESIS_OFFSET_THRESHOLD_BASIS = 8;
	private static final int SELECTION_COLOR = 0x4403992B;
	private static final int PHRASE_MARK_COLOR = 0x4405BFFC;

	private HandleView leftHandle = new LeftHandleView(this);
	private HandleView rightHandle = new RightHandleView(this);
	private SelectionManualRemoveListener selectionManualRemoveListener;
	private Paint highlightPaint = new Paint();
	private Paint phraseMarkPaint = new Paint();
	private Path highlightPath = new Path();
	private Path phraseMarkPath = new Path();
	private boolean justShowedSelectionWithHandles;
	private Rect workingAreaVisibleRect;

	{
		highlightPaint.setColor(SELECTION_COLOR);
		highlightPaint.setStyle(Paint.Style.FILL);
		phraseMarkPaint.setColor(PHRASE_MARK_COLOR);
		phraseMarkPaint.setStyle(Paint.Style.FILL);
	}

	public PhraseDefinitionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PhraseDefinitionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PhraseDefinitionView(Context context) {
		super(context);
	}

	public void showSelectionHandles() {
		registerOnScrollListener();

		leftHandle.setPositionAt(getSelectionStart());
		leftHandle.show();

		rightHandle.setPositionAt(getSelectionEnd());
		rightHandle.show();

		justShowedSelectionWithHandles = true;
	}

	public void removeSelectionWithHandles() {
		leftHandle.dismiss();
		rightHandle.dismiss();
		deregisterOnScrollListener();
		Selection.setSelection((Spannable) getText(), getSelectionStart(), getSelectionStart());
	}

	@Override
	protected MovementMethod getDefaultMovementMethod() {
		return ArrowKeyMovementMethod.getInstance();
	}

	public void setSelectionManualRemoveListener(SelectionManualRemoveListener selectionManualRemoveListener) {
		this.selectionManualRemoveListener = selectionManualRemoveListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = super.onTouchEvent(event);

		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			justShowedSelectionWithHandles = false;
			result |= gotoClickedLinkIfAny(event);
		} else if (event.getActionMasked() == MotionEvent.ACTION_UP /*UP event is suppressed when long clicked*/
				&& !justShowedSelectionWithHandles && (leftHandle.isActive() || rightHandle.isActive())) {
			removeSelectionWithHandles();
			if (selectionManualRemoveListener != null) {
				selectionManualRemoveListener.onSelectionManualRemove();
			}
		}

		return result;
	}

	private boolean gotoClickedLinkIfAny(MotionEvent event) {
		boolean result = false;
		int x = (int) event.getX();
		int y = (int) event.getY();

		int touchCharOffset = getCharOffsetForCoordinates(x, y);
		Spannable viewText = (Spannable) getText();
		ClickableSpan[] links = viewText.getSpans(touchCharOffset, touchCharOffset, ClickableSpan.class);

		if (links.length != 0) {
			links[0].onClick(this);

			result = true;
		}
		return result;
	}

	@Override
	public boolean onPreDraw() {
		autoScrollUpWhenDraggingLeftHandle();
		autoScrollDownWhenDraggingRightHandle();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		// translate in by padding
		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop() + getVerticalOffset());

		drawSelection(canvas);
		drawPhraseMark(canvas);

		canvas.restore();
	}

	private void drawSelection(Canvas canvas) {
		int selStart = getSelectionStart();
		int selEnd = getSelectionEnd();

		if (hasSelection()) {
			getLayout().getSelectionPath(selStart, selEnd, highlightPath);
		} else if (!highlightPath.isEmpty()) {
			highlightPath.rewind();
		}

		canvas.drawPath(highlightPath, highlightPaint);
	}

	private void drawPhraseMark(Canvas canvas) {
		Spannable spannableText = (Spannable) getText();
		int phraseMarkStart = SelectionUtils.getPhraseMarkStart(spannableText);
		int phraseMarkEnd = SelectionUtils.getPhraseMarkEnd(spannableText);

		if (SelectionUtils.hasPhraseMark(spannableText)) {
			getLayout().getSelectionPath(phraseMarkStart, phraseMarkEnd, phraseMarkPath);
		} else if (!phraseMarkPath.isEmpty()) {
			phraseMarkPath.rewind();
		}

		canvas.drawPath(phraseMarkPath, phraseMarkPaint);
	}

	public void markSelectionAsCardTitle() {
		SelectionUtils.removePhraseMark((Spannable) getText());
		invalidate(); // removes the previous mark from the text view
		SelectionUtils.setSelectionAsPhrase((Spannable) getText());
		removeSelectionWithHandles();
	}

	@Override
	public void onScrollChanged() {
		if (leftHandle.isActive()) {
			leftHandle.refreshHandle();
		}
		if (rightHandle.isActive()) {
			rightHandle.refreshHandle();
		}
	}

	public void setWorkingAreaVisibleRect(Rect workingAreaVisibleRect) {
		this.workingAreaVisibleRect = workingAreaVisibleRect;
	}

	private void autoScrollDownWhenDraggingRightHandle() {
		if (rightHandle.isDragged()) {
			int curs = getSelectionEnd();
			if (curs >= 0) {
				bringPointIntoView(curs);
			}
		}
	}

	private void autoScrollUpWhenDraggingLeftHandle() {
		if (leftHandle.isDragged()) {
			int curs = getSelectionStart();
			if (curs >= 0) {
				bringPointIntoView(curs);
			}
		}
	}

	public int getCharOffsetForCoordinates(int x, int y) {
		if (getLayout() == null) {
			return -1;
		}

		int yCoordinate = getAdjustedYCoordinate(y);

		int line = getLayout().getLineForVertical(yCoordinate);
		return getCharOffsetForHorizontal(line, x);
	}

	int getHysteresisCharOffsetForCoordinates(int x, int y, int previousOffset) {
		Layout layout = getLayout();
		if (layout == null) {
			return -1;
		}

		int yCoordinate = getAdjustedYCoordinate(y);

		int line = getLayout().getLineForVertical(yCoordinate);

		final int previousLine = layout.getLineForOffset(previousOffset);
		final int previousLineTop = layout.getLineTop(previousLine);
		final int previousLineBottom = layout.getLineBottom(previousLine);
		final int hysteresisThreshold = (previousLineBottom - previousLineTop) / HYSTERESIS_OFFSET_THRESHOLD_BASIS;

		// If new line is just before or after previous line and y position is less than
		// hysteresisThreshold away from previous line, keep cursor on previous line.
		if (((line == previousLine + 1) && ((yCoordinate - previousLineBottom) < hysteresisThreshold))
				|| ((line == previousLine - 1) && ((previousLineTop - yCoordinate) < hysteresisThreshold))) {
			line = previousLine;
		}

		return getCharOffsetForHorizontal(line, x);
	}

	private int getCharOffsetForHorizontal(int line, int x) {
		int xCoordinate = getAdjustedXCoordinate(x);
		return getLayout().getOffsetForHorizontal(line, xCoordinate);
	}

	int getVerticalOffset() {
		int verticalOffset = 0;
		final int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;

		Layout layout = getLayout();

		if (gravity != Gravity.TOP) {
			int effectiveBoxHeight;

			effectiveBoxHeight = getMeasuredHeight() - getExtendedPaddingTop() - getExtendedPaddingBottom();
			int layoutHeight = layout.getHeight();

			if (layoutHeight < effectiveBoxHeight) {
				verticalOffset = effectiveBoxHeight - layoutHeight;
				if (gravity != Gravity.BOTTOM) {
					// (gravity == Gravity.CENTER_VERTICAL)
					verticalOffset = verticalOffset >> 1;
				}
			}
		}
		return verticalOffset;
	}

	private void registerOnScrollListener() {
		ViewTreeObserver vto = getViewTreeObserver();
		if (vto != null) {
			vto.addOnScrollChangedListener(this);
		}
	}

	private void deregisterOnScrollListener() {
		ViewTreeObserver vto = getViewTreeObserver();
		if (vto != null) {
			vto.removeOnScrollChangedListener(this);
		}
	}

	private int getAdjustedXCoordinate(int x) {
		int xCoordinate = x - getTotalPaddingLeft();
		// Clamp the position to inside of the view.
		xCoordinate = Math.max(0, xCoordinate);
		xCoordinate = Math.min(getWidth() - getTotalPaddingRight() - 1, xCoordinate);
		xCoordinate += getScrollX();

		return xCoordinate;
	}

	private int getAdjustedYCoordinate(int y) {
		int yCoordinate = y - getTotalPaddingTop();
		// Clamp the position to inside of the view.
		yCoordinate = Math.max(0, yCoordinate);
		yCoordinate = Math.min(getHeight() - getTotalPaddingBottom() - 1, yCoordinate);
		yCoordinate += getScrollY();

		return yCoordinate;
	}

}
