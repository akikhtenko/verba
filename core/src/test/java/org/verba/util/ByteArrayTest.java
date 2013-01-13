package org.verba.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

public class ByteArrayTest {
	@Test
	public void shouldReturnNonDelimitedArrayAsIs() {
		Iterator<ByteArray> splitArrayIterator = new ByteArray("some text".getBytes()).split(new byte[] {'|'}).iterator();
		assertThat(splitArrayIterator.hasNext(), is(true));
		assertThat(splitArrayIterator.next().asBytes(), is("some text".getBytes()));
		assertThat(splitArrayIterator.hasNext(), is(false));
	}

	@Test
	public void shouldSplitArrayIntoTwoArrays() {
		Iterator<ByteArray> splitArrayIterator = new ByteArray("some\0text".getBytes()).split(new byte[] {'\0'}).iterator();
		assertThat(splitArrayIterator.hasNext(), is(true));
		assertThat(splitArrayIterator.next().asBytes(), is("some".getBytes()));
		assertThat(splitArrayIterator.hasNext(), is(true));
		assertThat(splitArrayIterator.next().asBytes(), is("text".getBytes()));
		assertThat(splitArrayIterator.hasNext(), is(false));
	}
}
