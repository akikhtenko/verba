package org.verba.xdxf.handler;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.node.XdxfElement;
import org.xml.sax.Attributes;

@RunWith(MockitoJUnitRunner.class)
public class XdxfEventHandlerTest {
	private static final Attributes DUMMY_ATTRIBUTES = null;
	private static final String DUMMY_TAG_NAME = "dummy";
	private static final String ANOTHER_TAG_NAME = "another-tag";

	@Mock
	private XdxfElement mockedXdxfElement;
	@Mock
	private XdxfEventHandler mockedNextXdxfEventHandler;
	private XdxfEventHandler handlerUNderTest;

	@Before
	public void createHandlerUnderTest() {
		handlerUNderTest = new XdxfEventHandler() {
			@Override
			protected XdxfElement createElement(Attributes attributes) {
				return mockedXdxfElement;
			}

			@Override
			protected String getMatchingTag() {
				return DUMMY_TAG_NAME;
			}
		};
	}

	@Test
	public void shouldCreateXdxfElementInside() {
		assertThat(handlerUNderTest.buildXdxfElement(DUMMY_TAG_NAME, DUMMY_ATTRIBUTES), is(mockedXdxfElement));
	}

	@Test
	public void shouldCreateDefaultXdxfElementWhenNextHandlerIsNotSet() {
		assertThat(handlerUNderTest.buildXdxfElement(ANOTHER_TAG_NAME, DUMMY_ATTRIBUTES), instanceOf(XdxfElement.class));
	}

	@Test
	public void shouldCreateXdxfElementOutside() {
		handlerUNderTest.chainNextHandler(mockedNextXdxfEventHandler);
		handlerUNderTest.buildXdxfElement(ANOTHER_TAG_NAME, DUMMY_ATTRIBUTES);
		verify(mockedNextXdxfEventHandler).buildXdxfElement(ANOTHER_TAG_NAME, DUMMY_ATTRIBUTES);
	}
}
