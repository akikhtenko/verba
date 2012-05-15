package org.verba.xdxf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

@RunWith(MockitoJUnitRunner.class)
public class XdxfParserTest {
	@Mock
	private InputStream mockedInputStream;
	@Mock
	private SAXParser mockedSaxParser;
	@Mock
	private XMLReader mockedXmlReader;
	@Mock
	private XdxfContentHandler mockedContentHandler;
	@Mock
	private XdxfElement mockedXdxfElement;
	@Spy
	private XdxfParser xdxfParser;

	@Test
	public void shouldParseXdxfArticle() throws IOException, XdxfArticleParseException, ParserConfigurationException,
			SAXException {
		when(xdxfParser.createSaxParser()).thenReturn(mockedSaxParser);
		when(xdxfParser.prepareContentHandler()).thenReturn(mockedContentHandler);
		when(mockedSaxParser.getXMLReader()).thenReturn(mockedXmlReader);
		when(mockedContentHandler.getXdxfArticle()).thenReturn(mockedXdxfElement);

		XdxfElement xdxfArticle = xdxfParser.parse(mockedInputStream);

		verify(mockedXmlReader).setContentHandler(mockedContentHandler);
		verify(mockedXmlReader).parse(any(InputSource.class));
		assertThat(xdxfArticle, is(mockedXdxfElement));
	}

	@Test(expected = XdxfArticleParseException.class)
	public void shouldThrowParseExceptionWhenParsingFails() throws ParserConfigurationException, SAXException,
			XdxfArticleParseException, IOException {
		when(xdxfParser.createSaxParser()).thenReturn(mockedSaxParser);
		when(mockedSaxParser.getXMLReader()).thenReturn(mockedXmlReader);
		doThrow(XdxfArticleParseException.class).when(mockedXmlReader).parse(any(InputSource.class));

		xdxfParser.parse(mockedInputStream);
	}
}
