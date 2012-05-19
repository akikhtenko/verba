package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.KEY_PHRASE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class KeyPhraseTest {
	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	KeyPhrase keyPhrase = new KeyPhrase();

	@Test
	public void shouldReturnNodeType() {
		assertThat(keyPhrase.getType(), is(KEY_PHRASE));
	}

	@Test
	public void shouldPrintItself() {
		keyPhrase.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(keyPhrase);
	}
}
