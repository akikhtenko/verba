package org.verba.mobile.widget;

import org.verba.mobile.R;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupWindow;

public abstract class HandleView extends View {
	private static final int ROTATION_180_DEGREES = 180;
	protected PhraseDefinitionView textView;
	protected SelectionActionsView selectionActionsView;
	private Drawable underlyingDrawable;
	private PopupWindow mContainer;
	private int handleAbsoluteXInView;
	private int handleAbsoluteYInView;
	private boolean dragged;
	private boolean active;
	private float touchToWindowOffsetX;
	private float touchToWindowOffsetY;
	private float hotspotX;
	private float hotspotY;
	private int width;
	private int height;
	private float touchOffsetY;
	private int lastParentAbsoluteX;
	private int lastParentAbsoluteY;

	public static enum VerticalPosition {
		TOP, BOTTOM, INLINE
	}

	public HandleView(PhraseDefinitionView aTextView, SelectionActionsView aSelectionActionsView) {
		super(aTextView.getContext());
		textView = aTextView;
		selectionActionsView = aSelectionActionsView;
		createPopupWindow(aTextView);

		initialiseState();
	}

	private void createPopupWindow(PhraseDefinitionView aTextView) {
		mContainer = new PopupWindow(aTextView.getContext());
		mContainer.setClippingEnabled(false);
		mContainer.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setBackgroundDrawable(null);
	}

	public void refreshHandle() {
		moveTo(handleAbsoluteXInView, handleAbsoluteYInView);
	}

	public void initialiseState() {
		underlyingDrawable = getContext().getResources().getDrawable(R.drawable.handle);

		width = underlyingDrawable.getIntrinsicWidth();
		height = underlyingDrawable.getIntrinsicHeight();

		hotspotX = getHotSpot(width);

		switch (getVerticalPosition()) {
		case TOP:
			touchOffsetY = height * 0.7f;
			break;
		case BOTTOM:
			touchOffsetY = -height * 0.3f;
			break;
		default:
			touchOffsetY = height * 0.3f;
		}

		hotspotY = 0;
		invalidate();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(width, height);
	}

	public void show() {
		if (!isPositionVisible()) {
			hide();
			return;
		}
		mContainer.setContentView(this);

		final int[] coords = new int[2];
		textView.getLocationInWindow(coords);
		coords[0] += handleAbsoluteXInView;
		coords[1] += handleAbsoluteYInView;
		mContainer.showAtLocation(textView, 0, coords[0], coords[1]);
//		refreshHandle();
		active = true;
	}

	private void hide() {
		markNotDragging();
		mContainer.dismiss();
	}

	public void dismiss() {
		hide();
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	private boolean isPositionVisible() {
		// Always show a dragging handle.
		if (dragged) {
			return true;
		}

		final int extendedPaddingTop = textView.getExtendedPaddingTop();
		final int extendedPaddingBottom = textView.getExtendedPaddingBottom();
		final int compoundPaddingLeft = textView.getCompoundPaddingLeft();
		final int compoundPaddingRight = textView.getCompoundPaddingRight();

		final int left = 0;
		final int right = textView.getWidth();
		final int top = 0;
		final int bottom = textView.getHeight();

		final Rect clip = new Rect();
		clip.left = left + compoundPaddingLeft;
		clip.top = top + extendedPaddingTop;
		clip.right = right - compoundPaddingRight;
		clip.bottom = bottom - extendedPaddingBottom;

		final ViewParent parent = textView.getParent();
		if (parent == null || !parent.getChildVisibleRect(textView, clip, null)) {
			return false;
		}

		final int[] coords = new int[2];
		textView.getLocationInWindow(coords);
		int posX = coords[0] + handleAbsoluteXInView + (int) hotspotX;
		int posY = coords[1] + handleAbsoluteYInView + (int) hotspotY;

		return posX >= clip.left && posX <= clip.right && (posY + height) >= clip.top && posY <= clip.bottom;
	}

	private void moveTo(int x, int y) {
		handleAbsoluteXInView = x - textView.getScrollX();
		handleAbsoluteYInView = y - textView.getScrollY();
		if (isPositionVisible()) {
			int[] viewAbsoluteCoords = new int[2];
			textView.getLocationInWindow(viewAbsoluteCoords);

			if (mContainer.isShowing()) {
				mContainer.update(
								viewAbsoluteCoords[0] + handleAbsoluteXInView,
								viewAbsoluteCoords[1] + handleAbsoluteYInView,
								width,
								height);
			} else {
				show();
			}

			if (dragged) {
				onHandleDraggingAutoScroll(viewAbsoluteCoords);
			}
		} else {
			hide();
		}
	}

	private void onHandleDraggingAutoScroll(int[] viewAbsoluteCoords) {
		if (viewAbsoluteCoords[0] != lastParentAbsoluteX || viewAbsoluteCoords[1] != lastParentAbsoluteY) {
			touchToWindowOffsetX += viewAbsoluteCoords[0] - lastParentAbsoluteX;
			touchToWindowOffsetY += viewAbsoluteCoords[1] - lastParentAbsoluteY;
			lastParentAbsoluteX = viewAbsoluteCoords[0];
			lastParentAbsoluteY = viewAbsoluteCoords[1];
		}
	}

	@Override
	public void onDraw(Canvas c) {
		underlyingDrawable.setBounds(0, 0, width, height);
		if (mirrorDrawable()) {
			c.save();
			c.rotate(ROTATION_180_DEGREES, (getRight() - getLeft()) / 2, (getBottom() - getTop()) / 2);
			underlyingDrawable.draw(c);
			c.restore();
		} else {
			underlyingDrawable.draw(c);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			handleActionDown(ev);
			break;

		case MotionEvent.ACTION_MOVE:
			handleActionMove(ev);

			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			handleActionUp();

		default:
		}
		return true;
	}

	private void handleActionDown(MotionEvent ev) {
		touchToWindowOffsetX = ev.getRawX() - handleAbsoluteXInView;
		touchToWindowOffsetY = ev.getRawY() - handleAbsoluteYInView;
		int[] coords = new int[2];
		textView.getLocationInWindow(coords);
		lastParentAbsoluteX = coords[0];
		lastParentAbsoluteY = coords[1];
		markDragging();
		selectionActionsView.hide();
	}

	private void handleActionMove(MotionEvent ev) {
		final float rawX = ev.getRawX();
		final float rawY = ev.getRawY();

		final float newAbsoluteXInView = rawX - touchToWindowOffsetX + hotspotX;
		final float newAbsoluteYInView = rawY - touchToWindowOffsetY + hotspotY + touchOffsetY;

		int newX = Math.round(newAbsoluteXInView);
		int newY = Math.round(newAbsoluteYInView);

		updateSelectionAfterHandleMove(newX, newY);
	}

	private void handleActionUp() {
		markNotDragging();
		selectionActionsView.updateAfterSelectionHandleDrag(this);
	}

	void setPositionAt(final int offset) {
		int leftCoordinateInView = adjustXByPadding((int) (textView.getLayout().getPrimaryHorizontal(offset) - hotspotX));
		int topCoordinateInView = adjustYByPadding(getBoundsTop(offset));

		moveTo(leftCoordinateInView, topCoordinateInView);
	}

	private int adjustXByPadding(int x) {
		return x + textView.getCompoundPaddingLeft();
	}

	private int adjustYByPadding(int y) {
		return y + textView.getExtendedPaddingTop() + textView.getVerticalOffset();
	}

	private int getBoundsTop(final int offset) {
		int topCoordinateY;

		final int line = textView.getLayout().getLineForOffset(offset);

		switch (getVerticalPosition()) {
		case TOP:
			topCoordinateY = getLineTopYYInView(line) - height;
			break;
		case BOTTOM:
			topCoordinateY = getLineBottomYInView(line);
			break;
		default:
			topCoordinateY = getLineTopYYInView(line);
		}

		return topCoordinateY;
	}

	private int getLineBottomYInView(final int line) {
		return textView.getLayout().getLineBottom(line);
	}

	private int getLineTopYYInView(final int line) {
		return textView.getLayout().getLineTop(line);
	}

	public int getHandleAbsoluteXInView() {
		return handleAbsoluteXInView;
	}

	public int getHandleAbsoluteYInView() {
		return handleAbsoluteYInView;
	}

	public int getHandleWidth() {
		return width;
	}

	public int getHandleHeight() {
		return height;
	}

	private void markNotDragging() {
		dragged = false;
	}

	private void markDragging() {
		dragged = true;
	}

	public boolean isDragged() {
		return dragged;
	}


	protected abstract float getHotSpot(int handleWidth);

	protected abstract boolean mirrorDrawable();

	protected abstract VerticalPosition getVerticalPosition();

	protected abstract void updateSelectionAfterHandleMove(int newX, int newY);
}
