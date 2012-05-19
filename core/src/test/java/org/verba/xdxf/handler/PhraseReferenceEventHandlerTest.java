package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.verba.xdxf.node.PhraseReference;
import org.xml.sax.Attributes;

public class PhraseReferenceEventHandlerTest {
	private static final Attributes DUMMY_ATTRIBUTES = null;

	@Test
	public void shouldReturnMatchingTagName() {
		assertThat(new PhraseReferenceEventHandler().getMatchingTag(), is("kref"));
	}

	@Test
	public void shouldCreateXdxfElement() {
		assertThat(new PhraseReferenceEventHandler().createElement(DUMMY_ATTRIBUTES), instanceOf(PhraseReference.class));
	}
}
