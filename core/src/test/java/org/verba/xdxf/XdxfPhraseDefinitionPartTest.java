package org.verba.xdxf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.stardict.PhraseDefinitionElementType.XDXF;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;

@RunWith(MockitoJUnitRunner.class)
public class XdxfPhraseDefinitionPartTest {
	private static final String WORD_DEFINITION = "word definition";
	@Mock
	private XdxfParser xdxfParser;
	@Captor
	private ArgumentCaptor<InputStream> inputStreamCaptor;

	private XdxfPhraseDefinitionElement xdxfPhraseDefinitionElement;

	@Before
	public void createXdxfPhraseDefinitionPart() {
		xdxfPhraseDefinitionElement = spy(new XdxfPhraseDefinitionElement(WORD_DEFINITION.getBytes()));
		when(xdxfPhraseDefinitionElement.createXdxfParser()).thenReturn(xdxfParser);
	}

	@Test
	public void shouldReturnXdxfType() {
		assertThat(xdxfPhraseDefinitionElement.getType(), is(XDXF));
	}

	@Test
	public void shouldReturnRawContent() {
		assertThat(new String(xdxfPhraseDefinitionElement.bytes()), is(WORD_DEFINITION));
	}

	@Test
	public void shouldParsePhraseDefinition() throws XdxfArticleParseException, IOException {
		xdxfPhraseDefinitionElement.asXdxfArticle();

		verify(xdxfParser).parse(inputStreamCaptor.capture());

		assertThat(IOUtils.toString(inputStreamCaptor.getValue()), is(WORD_DEFINITION));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenParsingException() throws XdxfArticleParseException, IOException {
		when(xdxfParser.parse(any(InputStream.class))).thenThrow(XdxfArticleParseException.class);

		xdxfPhraseDefinitionElement.asXdxfArticle();
	}
}
