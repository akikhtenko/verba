package org.verba.stardict;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryIndexReaderTest {
	@Mock
	private InputStream mockedInputStream;
	private static final String DUMMY_INDEX_CONTENT = "abc\0aaaabbbbcba\0ccccddddfedcba\0eeeeffff";

	@Test
	public void canReadTargetWordThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(
				DUMMY_INDEX_CONTENT.getBytes()));
		PhraseDefinitionCoordinates wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getTargetPhrase(), is(equalTo("abc")));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getTargetPhrase(), is(equalTo("cba")));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getTargetPhrase(), is(equalTo("fedcba")));
	}

	@Test
	public void canReadPhraseDefinitionOffsetThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(
				DUMMY_INDEX_CONTENT.getBytes()));
		PhraseDefinitionCoordinates wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionOffset(), is(1633771873L));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionOffset(), is(1667457891L));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionOffset(), is(1701143909L));
	}

	@Test
	public void canReadPhraseDefinitionLengthThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(
				DUMMY_INDEX_CONTENT.getBytes()));
		PhraseDefinitionCoordinates wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionLength(), is(1650614882));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionLength(), is(1684300900));
		wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getPhraseDefinitionLength(), is(1717986918));
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingATargetWord() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc".getBytes()));
		indexReader.readPhraseDefinitionCoordinates();
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingAPhraseDefinitionOffset() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0a".getBytes()));
		indexReader.readPhraseDefinitionCoordinates();
	}

	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingAPhraseDefinitionLength() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaab".getBytes()));
		indexReader.readPhraseDefinitionCoordinates();
	}

	@Test
	public void showsWhenThereIsMorePhraseDefinitionsToRead() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(
				"abc\0aaaabbbb".getBytes()));
		assertThat(indexReader.hasNextPhraseDefinitionCoordinates(), is(true));
		indexReader.readPhraseDefinitionCoordinates();
		assertThat(indexReader.hasNextPhraseDefinitionCoordinates(), is(false));
	}

	@Test
	public void repetativeCheckingIfThereIsMorePhraseDefinitionsToReadShouldNotCauseTroubleForNextPhraseDefinitionRead()
			throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(
				"abc\0aaaabbbb".getBytes()));

		indexReader.hasNextPhraseDefinitionCoordinates();
		indexReader.hasNextPhraseDefinitionCoordinates();
		indexReader.hasNextPhraseDefinitionCoordinates();

		PhraseDefinitionCoordinates wordCoordinates = indexReader.readPhraseDefinitionCoordinates();
		assertThat(wordCoordinates.getTargetPhrase(), is(equalTo("abc")));
		assertThat(wordCoordinates.getPhraseDefinitionOffset(), is(1633771873L));
		assertThat(wordCoordinates.getPhraseDefinitionLength(), is(1650614882));
	}

	@Test
	public void closesInputStream() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(mockedInputStream);

		indexReader.close();

		verify(mockedInputStream).close();
	}
}
