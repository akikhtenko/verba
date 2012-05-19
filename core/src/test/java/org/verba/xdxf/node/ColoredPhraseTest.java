package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.COLORED_PHRASE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class ColoredPhraseTest {
	private static final String DUMMY_COLOR_CODE = "dummy-color-code";

	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	ColoredPhrase coloredPhrase = new ColoredPhrase(DUMMY_COLOR_CODE);

	@Test
	public void shouldReturnNodeType() {
		assertThat(coloredPhrase.getType(), is(COLORED_PHRASE));
	}

	@Test
	public void shouldReturnColorCode() {
		assertThat(coloredPhrase.getColorCode(), is(DUMMY_COLOR_CODE));
	}

	@Test
	public void shouldPrintItself() {
		coloredPhrase.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(coloredPhrase);
	}
}
