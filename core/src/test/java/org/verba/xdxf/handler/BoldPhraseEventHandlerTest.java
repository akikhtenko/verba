package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.verba.xdxf.node.BoldPhrase;
import org.xml.sax.Attributes;

public class BoldPhraseEventHandlerTest {
	private static final Attributes DUMMY_ATTRIBUTES = null;

	@Test
	public void shouldReturnMatchingTagName() {
		assertThat(new BoldPhraseEventHandler().getMatchingTag(), is("b"));
	}

	@Test
	public void shouldCreateXdxfElement() {
		assertThat(new BoldPhraseEventHandler().createElement(DUMMY_ATTRIBUTES), instanceOf(BoldPhrase.class));
	}
}
