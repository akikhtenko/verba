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
public class IndexParserTest {
	
	@Test
	public void canReadWordCoordinates() throws IOException {
		DictionaryIndexReader indexParser = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaabbbb".getBytes()));
		WordCoordinates wordCoordinates = indexParser.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("abc")));
		assertThat(wordCoordinates.getWordDataOffset(), is(equalTo("aaaa")));
		assertThat(wordCoordinates.getWordDataSize(), is(equalTo(1650614882)));
	}
	
	@Test
	public void canReadTargetWordThreeTimesInARow() throws IOException {
		DictionaryIndexReader indexParser = new DictionaryIndexReader(new ByteArrayInputStream("abc\0aaaabbbbcba\0ccccddddfedcba\0eeeeffff".getBytes()));
		WordCoordinates wordCoordinates = indexParser.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("abc")));
		wordCoordinates = indexParser.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("cba")));
		wordCoordinates = indexParser.readWordCoordinates();
		assertThat(wordCoordinates.getTargetWord(), is(equalTo("fedcba")));
	}
}
