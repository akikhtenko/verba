package org.verba.mobile;

import static android.widget.Toast.LENGTH_LONG;
import static org.verba.mobile.CardSetViewerActivity.CARD_ID_PARAMETER;

import org.verba.mobile.card.Card;
import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardDao.NoCardFoundException;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class ViewCardActivity extends VerbaActivity {
	@Inject private CardDao cardDao;
	private TextView cardPhraseField;
	private TextView cardDefinitionField;
	private int cardId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cardId = getIntent().getIntExtra(CARD_ID_PARAMETER, -1);
		setupCardPhraseField();
		setupCardDefinitionField();
		populateCardFields();
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.view_card;
	}

	private void setupCardPhraseField() {
		cardPhraseField = (TextView) findViewById(R.id.phrase);
	}

	private void setupCardDefinitionField() {
		cardDefinitionField = (TextView) findViewById(R.id.definition);
	}

	private void populateCardFields() {
		try {
			Card cardToView = cardDao.getCardById(cardId);

			cardPhraseField.setText(cardToView.getPhrase());
			cardDefinitionField.setText(cardToView.getDefinition());
		} catch (NoCardFoundException e) {
			Toast.makeText(this, R.string.validationNoCardFound, LENGTH_LONG);
		}
	}
}