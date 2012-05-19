package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.verba.xdxf.node.ItalicPhrase;
import org.xml.sax.Attributes;

public class ItalicPhraseEventHandlerTest {
	private static final Attributes DUMMY_ATTRIBUTES = null;

	@Test
	public void shouldReturnMatchingTagName() {
		assertThat(new ItalicPhraseEventHandler().getMatchingTag(), is("i"));
	}

	@Test
	public void shouldCreateXdxfElement() {
		assertThat(new ItalicPhraseEventHandler().createElement(DUMMY_ATTRIBUTES), instanceOf(ItalicPhrase.class));
	}
}
