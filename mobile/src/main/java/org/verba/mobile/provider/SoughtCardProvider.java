package org.verba.mobile.provider;

import static android.widget.Toast.LENGTH_LONG;
import static org.verba.Card.emptyCard;
import static org.verba.mobile.CardSetViewerActivity.CARD_ID_PARAMETER;

import org.verba.Card;
import org.verba.interactors.GetCard;
import org.verba.mobile.R;
import org.verba.mobile.repository.SqliteCardRepository.NoCardFoundException;

import roboguice.inject.InjectExtra;
import android.app.Activity;
import android.widget.Toast;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SoughtCardProvider implements Provider<Card> {
	@InjectExtra(CARD_ID_PARAMETER) private int cardId;
	@Inject private GetCard getCard;
	@Inject private Activity activity;

	@Override
	public Card get() {
		try {
			return getCard.withId(cardId);
		} catch (NoCardFoundException e) {
			Toast.makeText(activity, R.string.validationNoCardFound, LENGTH_LONG).show();
		}

		return emptyCard();
	}

}
