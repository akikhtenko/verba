package org.verba.xdxf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.stardict.WordDefinitionPartType.XDXF;

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
public class XdxfWordDefinitionPartTest {
	private static final String WORD_DEFINITION = "word definition";
	@Mock
	private XdxfParser mockedXdxfParser;
	@Captor
	private ArgumentCaptor<InputStream> inputStreamCaptor;

	private XdxfWordDefinitionPart xdxfWordDefinitionPart;

	@Before
	public void createXdxfWordDefinitionPart() {
		xdxfWordDefinitionPart = spy(new XdxfWordDefinitionPart(WORD_DEFINITION.getBytes()));
		when(xdxfWordDefinitionPart.createXdxfParser()).thenReturn(mockedXdxfParser);
	}

	@Test
	public void shouldReturnXdxfType() {
		assertThat(xdxfWordDefinitionPart.getType(), is(XDXF));
	}

	@Test
	public void shouldReturnRawContent() {
		assertThat(new String(xdxfWordDefinitionPart.bytes()), is(WORD_DEFINITION));
	}

	@Test
	public void shouldParseWordDefinition() throws XdxfArticleParseException, IOException {
		xdxfWordDefinitionPart.asXdxfArticle();

		verify(mockedXdxfParser).parse(inputStreamCaptor.capture());

		assertThat(IOUtils.toString(inputStreamCaptor.getValue()), is(WORD_DEFINITION));
	}

	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void shouldFailWhenParsingException() throws XdxfArticleParseException, IOException {
		when(mockedXdxfParser.parse(any(InputStream.class))).thenThrow(XdxfArticleParseException.class);

		xdxfWordDefinitionPart.asXdxfArticle();
	}
}
