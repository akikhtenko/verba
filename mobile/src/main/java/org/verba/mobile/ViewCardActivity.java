package org.verba.mobile;

import static android.widget.Toast.LENGTH_LONG;
import static org.verba.mobile.CardSetViewerActivity.CARD_ID_PARAMETER;

import org.verba.Card;
import org.verba.boundary.CardRetrieval;
import org.verba.mobile.repository.SqliteCardRepository.NoCardFoundException;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class ViewCardActivity extends VerbaActivity {
	@Inject private CardRetrieval cardRetrieval;
	@InjectView(R.id.phrase) private TextView cardPhraseField;
	@InjectView(R.id.definition) private TextView cardDefinitionField;
	@InjectExtra(CARD_ID_PARAMETER) private int cardId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	private void populateCardFields() {
		try {
			Card cardToView = cardRetrieval.getCardById(cardId);

			cardPhraseField.setText(cardToView.getPhrase());
			cardDefinitionField.setText(cardToView.getDefinition());
		} catch (NoCardFoundException e) {
			Toast.makeText(this, R.string.validationNoCardFound, LENGTH_LONG);
		}
	}
}