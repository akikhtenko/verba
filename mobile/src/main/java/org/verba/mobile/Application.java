package org.verba.mobile;

import java.io.File;

import android.os.Environment;

public final class Application {
	private Application() {
	}
	public static File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}
}
