package org.verba.mobile.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	private static final String NEW_LINE_REPLACEMENT = " ";
	private static final String COMMENT_START = "--";

	public String[] parseSqlFile(InputStream sqlFileStream) throws IOException {
		return parseSqlFile(new BufferedReader(new InputStreamReader(sqlFileStream)));
	}

	protected String[] parseSqlFile(BufferedReader sqlFile) throws IOException {
		String normalizedSql;
		try {
			normalizedSql = normalizeQueries(sqlFile);
		} finally {
			sqlFile.close();
		}

		return normalizedSql.split("/");
	}

	private String normalizeQueries(BufferedReader sqlFile) throws IOException {
		StringBuilder sql = new StringBuilder();

		String line;
		while ((line = sqlFile.readLine()) != null) {
			if (sql.length() != 0) {
				sql.append(NEW_LINE_REPLACEMENT);
			}

			sql.append(removeComments(line.trim()));
		}

		return sql.toString();
	}

	private String removeComments(String line) {
		if (line.indexOf(COMMENT_START) != -1) {
			return line.substring(0, line.indexOf(COMMENT_START));
		} else {
			return line;
		}
	}

}
