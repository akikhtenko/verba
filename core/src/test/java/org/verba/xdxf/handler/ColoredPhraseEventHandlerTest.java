package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.node.ColoredPhrase;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

@RunWith(MockitoJUnitRunner.class)
public class ColoredPhraseEventHandlerTest {
	private static final String DUMMY_COLOR_CODE = "dummy-color-code";

	@Mock
	private Attributes mockedAttributes;

	@Test
	public void shouldReturnMatchingTagName() {
		assertThat(new ColoredPhraseEventHandler().getMatchingTag(), is("c"));
	}

	@Test
	public void shouldCreateXdxfElement() {
		when(mockedAttributes.getValue("c")).thenReturn(DUMMY_COLOR_CODE);

		XdxfElement xdxfElement = new ColoredPhraseEventHandler().createElement(mockedAttributes);

		assertThat(xdxfElement, instanceOf(ColoredPhrase.class));
		assertThat(((ColoredPhrase) xdxfElement).getColorCode(), is(DUMMY_COLOR_CODE));
	}
}
