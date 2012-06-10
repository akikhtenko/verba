package org.verba.stardict;

import java.io.Serializable;

public class PhraseDefinitionCoordinates implements Serializable {
	private static final long serialVersionUID = -819881545923892885L;

	private String targetPhrase;
	private long phraseDefinitionOffset;
	private int phraseDefinitionLength;

	public PhraseDefinitionCoordinates(String aTargetWord, long aPhraseDefinitionOffset, int aPhraseDefinitionLength) {
		targetPhrase = aTargetWord;
		phraseDefinitionOffset = aPhraseDefinitionOffset;
		phraseDefinitionLength = aPhraseDefinitionLength;
	}

	public String getTargetPhrase() {
		return targetPhrase;
	}

	public long getPhraseDefinitionOffset() {
		return phraseDefinitionOffset;
	}

	public int getPhraseDefinitionLength() {
		return phraseDefinitionLength;
	}

	public boolean matches(String phraseToCompareWith) {
		return targetPhrase.startsWith(phraseToCompareWith);
	}

}
