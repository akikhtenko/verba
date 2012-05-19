package org.verba.xdxf.node;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.verba.xdxf.node.XdxfNodeType.PHRASE_REFERENCE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.xdxf.XdxfNodeDisplay;

@RunWith(MockitoJUnitRunner.class)
public class PhraseReferenceTest {
	@Mock
	private XdxfNodeDisplay mockedXdxfNodeDisplay;

	PhraseReference phraseReference = new PhraseReference();

	@Test
	public void shouldReturnNodeType() {
		assertThat(phraseReference.getType(), is(PHRASE_REFERENCE));
	}

	@Test
	public void shouldPrintItself() {
		phraseReference.print(mockedXdxfNodeDisplay);
		verify(mockedXdxfNodeDisplay).print(phraseReference);
	}
}
