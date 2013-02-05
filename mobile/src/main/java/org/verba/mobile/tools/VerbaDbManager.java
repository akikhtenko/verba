package org.verba.mobile.tools;

import java.io.IOException;
import java.io.InputStream;

import org.verba.mobile.R;
import org.verba.mobile.utils.FileUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VerbaDbManager extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "verba";

	private Context context;

	public VerbaDbManager(Context aContext) {
		super(aContext, DATABASE_NAME, null, aContext.getResources().getInteger(R.string.databaseVersion));
		context = aContext;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		executeQueriesBatch(db, "create_database.sql");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int migrationNumber = oldVersion;
		while (migrationNumber < newVersion) {
			executeQueriesBatch(db, String.format("update_database_from_%s.sql", migrationNumber));
			migrationNumber++;
		}
	}

	private void executeQueriesBatch(SQLiteDatabase db, String batchFile) {
		try {
			execute(db, batchFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void execute(SQLiteDatabase db, String batchFile) throws IOException {
		InputStream is = context.getResources().getAssets().open(batchFile);
		String[] statements = new FileUtils().parseSqlFile(is);

		for (String statement : statements) {
			db.execSQL(statement);
		}
	}
}
