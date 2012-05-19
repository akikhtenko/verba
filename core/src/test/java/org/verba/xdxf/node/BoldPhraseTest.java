package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.BOLD_PHRASE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class BoldPhraseTest {
	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	BoldPhrase boldPhrase = new BoldPhrase();

	@Test
	public void shouldReturnNodeType() {
		assertThat(boldPhrase.getType(), is(BOLD_PHRASE));
	}

	@Test
	public void shouldPrintItself() {
		boldPhrase.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(boldPhrase);
	}
}
