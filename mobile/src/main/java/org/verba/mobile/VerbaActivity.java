package org.verba.mobile;

import java.lang.reflect.Field;

import org.robobinding.binder.Binder;
import org.verba.mobile.presentationmodel.MenuPresentationModel;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

public abstract class VerbaActivity extends RoboSherlockActivity {
	public static final int DEVICE_VERSION   = Build.VERSION.SDK_INT;
	public static final int DEVICE_HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MenuPresentationModel menuPresentationModel = new MenuPresentationModel(this);
		View menuView = Binder.bindView(this, R.layout.menu, menuPresentationModel);

		hackSherlockActionBarToShowOverlayAtAllTimes();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(menuView, new LayoutParams(Gravity.RIGHT));
	}

	private void hackSherlockActionBarToShowOverlayAtAllTimes() {
		if (DEVICE_VERSION >= DEVICE_HONEYCOMB) {
			try {
				ViewConfiguration config = ViewConfiguration.get(this);
				Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
				if (menuKeyField != null) {
					menuKeyField.setAccessible(true);
					menuKeyField.setBoolean(config, false);
				}
			} catch (Exception ex) {
				Log.e("Verba", "ABS Overflow Menu forcing hack failed", ex);
			}
		}
	}

	/**
	 * The second half of the hack to always show the action bar overlay button
	 * @see check out hackSherlockActionBarToShowOverlayAtAllTimes() to find the first half
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (DEVICE_VERSION < DEVICE_HONEYCOMB) {
			if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
				openOptionsMenu();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

}
