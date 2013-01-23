package org.verba.mobile;

import static android.widget.Toast.LENGTH_LONG;
import static org.verba.mobile.CardSetViewerActivity.CARD_ID_PARAMETER;

import org.verba.Card;
import org.verba.interactors.GetCard;
import org.verba.mobile.repository.SqliteCardRepository.NoCardFoundException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

@ContentView(R.layout.view_card)
public class ViewCardActivity extends VerbaActivity {
	@Inject private GetCard getCard;

	@InjectView(R.id.phrase) private TextView cardPhraseField;
	@InjectView(R.id.definition) private TextView cardDefinitionField;
	@InjectExtra(CARD_ID_PARAMETER) private int cardId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		populateCardFields();
	}

	private void populateCardFields() {
		try {
			Card cardToView = getCard.withId(cardId);

			cardPhraseField.setText(cardToView.getPhrase());
			cardDefinitionField.setText(cardToView.getDefinition());
		} catch (NoCardFoundException e) {
			Toast.makeText(this, R.string.validationNoCardFound, LENGTH_LONG).show();
		}
	}
}