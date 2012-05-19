package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.ITALIC_PHRASE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class ItalicPhraseTest {
	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	ItalicPhrase italicPhrase = new ItalicPhrase();

	@Test
	public void shouldReturnNodeType() {
		assertThat(italicPhrase.getType(), is(ITALIC_PHRASE));
	}

	@Test
	public void shouldPrintItself() {
		italicPhrase.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(italicPhrase);
	}
}
