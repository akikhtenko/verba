package org.verba.mobile.card;


public class Card {
	private int id;
	private String phrase;
	private String definition;
	private int cardSetId;

	public Card() {
	}

	public Card(int cardSetId) {
		this.cardSetId = cardSetId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public int getCardSetId() {
		return cardSetId;
	}

	public void setCardSetId(int cardSetId) {
		this.cardSetId = cardSetId;
	}

	@Override
	public String toString() {
		return phrase;
	}
}
