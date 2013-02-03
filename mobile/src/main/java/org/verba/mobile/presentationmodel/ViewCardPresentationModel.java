package org.verba.mobile.presentationmodel;

import org.robobinding.presentationmodel.PresentationModel;
import org.verba.Card;
import org.verba.mobile.provider.SoughtCard;

import com.google.inject.Inject;

@PresentationModel
public class ViewCardPresentationModel {
	@Inject @SoughtCard private Card cardToView;

	public String getCardPhrase() {
		return cardToView.getPhrase();
	}
	public String getCardDefinition() {
		return cardToView.getDefinition();
	}
}
