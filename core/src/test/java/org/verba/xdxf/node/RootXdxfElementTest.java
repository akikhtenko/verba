package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.verba.xdxf.node.XdxfNodeType.ROOT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RootXdxfElementTest {
	@Test
	public void shouldReturnNodeType() {
		assertThat(new RootXdxfElement().getType(), is(ROOT));
	}
}
