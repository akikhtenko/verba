package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.verba.xdxf.node.KeyPhrase;
import org.xml.sax.Attributes;

public class KeyPhraseEventHandlerTest {
	private static final Attributes DUMMY_ATTRIBUTES = null;

	@Test
	public void shouldReturnMatchingTagName() {
		assertThat(new KeyPhraseEventHandler().getMatchingTag(), is("k"));
	}

	@Test
	public void shouldCreateXdxfElement() {
		assertThat(new KeyPhraseEventHandler().createElement(DUMMY_ATTRIBUTES), instanceOf(KeyPhrase.class));
	}
}
