package org.verba.mobile.widget;

import org.verba.mobile.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupWindow;

public class SelectionActionsView extends View {
	private static final int PANEL_FROM_HANDLES_OFFSET = 7;
	private PhraseDefinitionView textView;
	private PopupWindow mContainer;
	private View mContentView;
	private int absoluteXInView;
	private int absoluteYInView;
	private boolean active;

	private void createPopupWindow() {
		mContainer = new PopupWindow(textView.getContext());
		mContainer.setClippingEnabled(false);
		mContainer.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setBackgroundDrawable(null);
	}

	public SelectionActionsView(PhraseDefinitionView phraseDefinitionView) {
		super(phraseDefinitionView.getContext());
		textView = phraseDefinitionView;

		createPopupWindow();

		initContentView();

		mContainer.setContentView(mContentView);
	}

	protected void initContentView() {
		LayoutInflater inflater = (LayoutInflater) textView.getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		mContentView = inflater.inflate(R.layout.text_selection_actions, null);
	}

	protected void measureContent() {
		final DisplayMetrics displayMetrics = textView.getContext().getResources().getDisplayMetrics();
		mContentView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.AT_MOST),
				View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.AT_MOST));
	}

	private int viewportToContentVerticalOffset() {
		return textView.getExtendedPaddingTop() - textView.getScrollY() + textView.getVerticalOffset();
	}

	private int getVerticalLocalPosition(int line) {
		return textView.getLayout().getLineTop(line) - mContentView.getMeasuredHeight();
	}

	public void show() {
		measureContent();
		computePanelPositionNearSelectionHandle(textView.getLeftHandle());
		setViewToAssignedPosition();
		active = true;
	}

	public void updateAfterSelectionHandleDrag(HandleView handleView) {
		computePanelPositionNearSelectionHandle(handleView);
		setViewToAssignedPosition();
	}

	public void refreshPanel() {
		setViewToAssignedPosition();
	}

	private void setViewToAssignedPosition() {
		int[] parentCoordinates = new int[2];
		textView.getLocationInWindow(parentCoordinates);
		updatePosition(parentCoordinates[0], parentCoordinates[1]);
	}

	private void computePanelPositionNearSelectionHandle(HandleView handleView) {
		Rect clip = getVisibilityClip();
		int[] parentCoordinates = getParentViewAbsoluteCoordinates();

		computePanelXCoordinate(handleView, clip, parentCoordinates);
		computePanelYCoordinate(handleView, clip, parentCoordinates);
	}

	private void computePanelXCoordinate(HandleView handleView, Rect clip, int[] parentCoordinates) {
		absoluteXInView = handleView.getHandleAbsoluteXInView();
		if (absoluteXInView + parentCoordinates[0] < clip.left) {
			absoluteXInView = clip.left - parentCoordinates[0];
		} else if (absoluteXInView + parentCoordinates[0] + mContentView.getMeasuredWidth() > clip.right) {
			absoluteXInView = clip.right - parentCoordinates[0] - mContentView.getMeasuredWidth();
		}
	}

	private void computePanelYCoordinate(HandleView handleView, Rect clip, int[] parentCoordinates) {
		if (handleView == textView.getLeftHandle()) {
			int line = textView.getLayout().getLineForOffset(getSelectionStart());
			absoluteYInView = getVerticalLocalPosition(line);
			absoluteYInView += viewportToContentVerticalOffset();
			absoluteYInView -= PANEL_FROM_HANDLES_OFFSET;

			if (parentCoordinates[1] + absoluteYInView < clip.top) {
				absoluteYInView = handleView.getHandleAbsoluteYInView() + handleView.getHandleHeight();
				absoluteYInView += PANEL_FROM_HANDLES_OFFSET;
			}
		} else {
			absoluteYInView = handleView.getHandleAbsoluteYInView() + handleView.getHandleHeight();
			absoluteYInView += PANEL_FROM_HANDLES_OFFSET;

			if (parentCoordinates[1] + absoluteYInView + mContentView.getMeasuredHeight() > clip.bottom) {
				int line = textView.getLayout().getLineForOffset(getSelectionEnd());
				absoluteYInView = getVerticalLocalPosition(line);
				absoluteYInView += viewportToContentVerticalOffset();
				absoluteYInView -= PANEL_FROM_HANDLES_OFFSET;
			}
		}
	}

	private void updatePosition(int parentPositionX, int parentPositionY) {
		int positionX = parentPositionX + absoluteXInView;
		int positionY = parentPositionY + absoluteYInView;

		if (!isPositionVisible(positionX, positionY)) {
			hide();
		} else {
			if (mContainer.isShowing()) {
				mContainer.update(positionX, positionY, -1, -1);
			} else {
				mContainer.showAtLocation(textView, Gravity.NO_GRAVITY, positionX, positionY);
			}
		}
	}

	void hide() {
		mContainer.dismiss();
	}

	public void dismiss() {
		hide();
		active = false;
	}

	public boolean isActive() {
		return active;
	}

	private Rect getVisibilityClip() {
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
		parent.getChildVisibleRect(textView, clip, null);

		return clip;
	}


	private boolean isPositionVisible(int posX, int posY) {
		Rect clip = getVisibilityClip();
		return posX >= clip.left
				&& (posX + mContentView.getMeasuredWidth()) <= clip.right
				&& posY >= clip.top
				&& (posY + mContentView.getMeasuredHeight()) <= clip.bottom;
	}

	private int[] getParentViewAbsoluteCoordinates() {
		final int[] parentCoordinates = new int[2];
		textView.getLocationInWindow(parentCoordinates);

		return parentCoordinates;
	}

	private int getSelectionStart() {
		return textView.getSelectionStart();
	}

	private int getSelectionEnd() {
		return textView.getSelectionEnd();
	}
}
