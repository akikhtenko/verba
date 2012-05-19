package org.verba.xdxf;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.handler.XdxfEventHandler;
import org.verba.xdxf.node.PlainText;
import org.verba.xdxf.node.RootXdxfElement;
import org.verba.xdxf.node.XdxfElement;
import org.verba.xdxf.node.XdxfNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

@RunWith(MockitoJUnitRunner.class)
public class XdxfContentHandlerTest {
	private static final String DUMMUY_TEXT = "123";
	private static final String DUMMY_QNAME = "dummy-qname";
	private static final String DUMMY_NS_URI = null;
	private static final String DUMMY_LOCAL_NAME = null;
	private static final Attributes DUMMY_ATTRIBUTES = null;
	@Mock
	private XdxfEventHandler mockedXdxfEventHandler;
	@Mock
	private XdxfElement mockedXdxfElement;
	@Captor
	private ArgumentCaptor<XdxfNode> xdxfNodeCaptor;

	private XdxfContentHandler xdxfContentHandler;

	@Before
	public void prepareXdxfContentHandler() {
		xdxfContentHandler = new XdxfContentHandler(mockedXdxfEventHandler);
		when(mockedXdxfEventHandler.buildXdxfElement(DUMMY_QNAME, DUMMY_ATTRIBUTES)).thenReturn(mockedXdxfElement);
	}

	@Test
	public void shouldReturnRootElement() throws SAXException {
		xdxfContentHandler.startDocument();

		assertThat(xdxfContentHandler.getXdxfArticle(), instanceOf(RootXdxfElement.class));
	}

	@Test
	public void shouldProcessElementStart() throws SAXException {
		xdxfContentHandler.startDocument();
		xdxfContentHandler.startElement(DUMMY_NS_URI, DUMMY_LOCAL_NAME, DUMMY_QNAME, DUMMY_ATTRIBUTES);

		assertThat(xdxfContentHandler.getXdxfArticle(), is(mockedXdxfElement));
	}

	@Test
	public void shouldProcessElementEnd() throws SAXException {
		xdxfContentHandler.startDocument();
		xdxfContentHandler.startElement(DUMMY_NS_URI, DUMMY_LOCAL_NAME, DUMMY_QNAME, DUMMY_ATTRIBUTES);
		xdxfContentHandler.endElement(DUMMY_NS_URI, DUMMY_LOCAL_NAME, DUMMY_QNAME);

		assertThat(xdxfContentHandler.getXdxfArticle(), instanceOf(RootXdxfElement.class));
	}

	@Test
	public void shouldBufferTextPieces() throws SAXException {
		xdxfContentHandler.startDocument();

		xdxfContentHandler.startElement(DUMMY_NS_URI, DUMMY_LOCAL_NAME, DUMMY_QNAME, DUMMY_ATTRIBUTES);

		xdxfContentHandler.characters(DUMMUY_TEXT.toCharArray(), 0, 1);
		xdxfContentHandler.characters(DUMMUY_TEXT.toCharArray(), 1, 1);
		xdxfContentHandler.characters(DUMMUY_TEXT.toCharArray(), 2, 1);

		xdxfContentHandler.endElement(DUMMY_NS_URI, DUMMY_LOCAL_NAME, DUMMY_QNAME);

		verify(mockedXdxfElement).addChild(xdxfNodeCaptor.capture());

		assertThat(((PlainText) xdxfNodeCaptor.getValue()).asPlainText(), is(DUMMUY_TEXT));
	}
}
