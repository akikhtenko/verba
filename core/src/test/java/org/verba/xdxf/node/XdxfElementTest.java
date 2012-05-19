package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.xdxf.node.XdxfNodeType.UNKNOWN;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class XdxfElementTest {
	@Mock
	private XdxfNode mockedXdxfNode;
	@Mock
	private XdxfNodeDisplay mockedNodeDisplay;

	private XdxfElement xdxfElementUnderTest;

	@Before
	public void createXdxfElement() {
		xdxfElementUnderTest = new XdxfElement();
	}

	@Test
	public void shouldReturnUnknownType() {
		assertThat(xdxfElementUnderTest.getType(), is(UNKNOWN));
	}

	@Test
	public void shouldConcatenateChildrenTextContents() {
		when(mockedXdxfNode.asPlainText()).thenReturn("1", "2", "3");

		addThreeChildrenToXdxfElement();

		assertThat(xdxfElementUnderTest.asPlainText(), is("123"));
	}

	@Test
	public void shouldSumChildrenContentLengths() {
		when(mockedXdxfNode.getContentLength()).thenReturn(1, 2, 3);

		addThreeChildrenToXdxfElement();

		assertThat(xdxfElementUnderTest.getContentLength(), is(1 + 2 + 3));
	}

	@Test
	public void shouldPrintAllChildren() {
		addThreeChildrenToXdxfElement();

		xdxfElementUnderTest.print(mockedNodeDisplay);

		verify(mockedXdxfNode, times(3)).print(mockedNodeDisplay);
	}

	private void addThreeChildrenToXdxfElement() {
		xdxfElementUnderTest.addChild(mockedXdxfNode);
		xdxfElementUnderTest.addChild(mockedXdxfNode);
		xdxfElementUnderTest.addChild(mockedXdxfNode);
	}
}
