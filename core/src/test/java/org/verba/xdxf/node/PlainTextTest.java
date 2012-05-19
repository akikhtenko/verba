package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.PLAIN_TEXT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class PlainTextTest {
	private static final String DUMMY_TEXT = "dummy-text";

	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	PlainText plainText = new PlainText(DUMMY_TEXT);

	@Test
	public void shouldReturnNodeType() {
		assertThat(plainText.getType(), is(PLAIN_TEXT));
	}

	@Test
	public void shouldGetPayload() {
		assertThat(plainText.asPlainText(), is(DUMMY_TEXT));
	}

	@Test
	public void shouldGetPayloadLength() {
		assertThat(plainText.getContentLength(), is(DUMMY_TEXT.length()));
	}

	@Test
	public void shouldPrintItself() {
		plainText.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(plainText);
	}
}
