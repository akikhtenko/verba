package org.verba.mobile.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilsTest {
	private static final String DUMMY_SQL = "select\n id--, name\n from table/\nanother statement";
	private FileUtils fileUtils = new FileUtils();
	@Mock BufferedReader mockedBufferedReader;

	@Test
	public void shouldParseSql() throws IOException {
		String[] distinctStatements = fileUtils.parseSqlFile(new ByteArrayInputStream(DUMMY_SQL.getBytes()));
		assertThat(distinctStatements[0], is("select id from table"));
		assertThat(distinctStatements[1], is(" another statement"));
	}

	@Test
	public void shouldCloseAfterParsing() throws IOException {
		fileUtils.parseSqlFile(mockedBufferedReader);
		verify(mockedBufferedReader).close();
	}
}
