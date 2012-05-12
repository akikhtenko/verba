package org.verba.stardict;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryIndexReaderTest {
	
	private static final String DUMMY_INDEX_CONTENT = "abc\0aaaabbbbcba\0ccccddddfedcba\0eeeeffff";

	@Test
	public void canReadTargetWordThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(DUMMY_INDEX_CONTENT.getBytes()));
		WordCoordinates wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("abc")));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("cba")));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("fedcba")));
	}
	
	@Test
	public void canReadWordDefinitionOffsetThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(DUMMY_INDEX_CONTENT.getBytes()));
		WordCoordinates wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionOffset(), is(1633771873L));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionOffset(), is(1667457891L));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionOffset(), is(1701143909L));
	}
	
	@Test
	public void canReadWordDefinitionLengthThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream(DUMMY_INDEX_CONTENT.getBytes()));
		WordCoordinates wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionLength(), is(1650614882L));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionLength(), is(1684300900L));
		wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getWordDefinitionLength(), is(1717986918L));
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingATargetWord() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc".getBytes()));
		indexReader.readWordCoordinates();
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingAWordDefinitionOffset() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0a".getBytes()));
		indexReader.readWordCoordinates();
	}
	
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenEndOfStreamReachedWhileReadingAWordDefinitionLength() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaab".getBytes()));
		indexReader.readWordCoordinates();
	}
	
	@Test
	public void showsWhenThereIsMoreWordDefinitionsToRead() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaabbbb".getBytes()));
		assertThat(indexReader.hasNextWordDefinition(), is(true));
		indexReader.readWordCoordinates();
		assertThat(indexReader.hasNextWordDefinition(), is(false));
	}
	
	@Test
	public void repetativeCheckingIfThereIsMoreWordDefinitionsToReadShouldNotCauseTroubleForNextWordDefinitionRead() throws IOException {
		DictionaryIndexReader indexReader = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaabbbb".getBytes()));
		
		indexReader.hasNextWordDefinition();
		indexReader.hasNextWordDefinition();
		indexReader.hasNextWordDefinition();
		
		WordCoordinates wordCoordinates = indexReader.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("abc")));
		assertThat(wordCoordinates.getWordDefinitionOffset(), is(1633771873L));
		assertThat(wordCoordinates.getWordDefinitionLength(), is(1650614882L));
	}
}
