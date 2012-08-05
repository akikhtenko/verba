package org.verba.mobile;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static org.verba.mobile.PhraseDefinitionDetailsActivity.CARD_DEFINITION_PARAMETER;
import static org.verba.mobile.PhraseDefinitionDetailsActivity.CARD_PHRASE_PARAMETER;

import java.util.List;

import org.verba.mobile.card.Card;
import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSet;
import org.verba.mobile.card.CardSetDao;

import roboguice.inject.InjectView;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.inject.Inject;

public class EditCardActivity extends VerbaActivity implements OnClickListener {
	private static final int DIALOG_ADD_CARD_SET = 0;
	@Inject private CardSetDao cardSetDao;
	@Inject private CardDao cardDao;
	@InjectView(R.id.cardPhrase) private EditText cardPhraseField;
	@InjectView(R.id.cardDefinition) private EditText cardDefinitionField;
	@InjectView(R.id.cardSetsList) private Spinner cardSetsList;
	private EditText cardSetNameField;

	private OnClickListener addCardSetButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_ADD_CARD_SET);
		}
	};
	private OnClickListener doAddCardSetButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String cardSetName = cardSetNameField.getText().toString();
			if (cardSetName == null || cardSetName.equalsIgnoreCase("")) {
				makeText(EditCardActivity.this, R.string.validationEmptyCardSetName, LENGTH_SHORT).show();
			} else {
				addCardSet(cardSetName);
				populateCardSetsList();
				makeText(EditCardActivity.this, R.string.cardSetAdded, LENGTH_SHORT).show();
				dismissDialog(DIALOG_ADD_CARD_SET);
			}
		}

		private void addCardSet(String cardSetName) {
			//TODO: add check for existing card set with the same name
			CardSet cardSet = new CardSet();
			cardSet.setName(cardSetName);
			cardSetDao.addCardSet(cardSet);
		}
	};

	@Override
	public void onClick(View v) {
		CardSet cardSet = (CardSet) cardSetsList.getSelectedItem();
		if (cardSet == null) {
			makeText(this, R.string.validationNoCardSetSelected, LENGTH_SHORT).show();
		} else {
			addCardToCardSet(cardSet);
			makeText(this, R.string.cardAddedToCardSet, LENGTH_SHORT).show();
			finish();
		}
	}

	private void addCardToCardSet(CardSet cardSet) {
		Card card = new Card();
		card.setCardSetId(cardSet.getId());
		card.setPhrase(cardPhraseField.getText().toString());
		card.setDefinition(cardDefinitionField.getText().toString());
		cardDao.addCard(card);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		populateCardSetsList();
		setupAddCardSetButton();
		cardPhraseField.setText(getIntent().getStringExtra(CARD_PHRASE_PARAMETER));
		cardDefinitionField.setText(getIntent().getStringExtra(CARD_DEFINITION_PARAMETER));
		setupSaveButton();
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.edit_card;
	}


	private void setupAddCardSetButton() {
		Button button = (Button) findViewById(R.id.addCardSetButton);
		button.setOnClickListener(addCardSetButtonListener);
	}

	private void setupSaveButton() {
		Button button = (Button) findViewById(R.id.saveCardButton);
		button.setOnClickListener(this);
	}

	private void populateCardSetsList() {
		List<CardSet> cardSets = cardSetDao.getAllCardSets();
		ArrayAdapter<CardSet> cardSetsDatasource = new ArrayAdapter<CardSet>(this, R.layout.card_set_item,
				R.id.cardSetTitle, cardSets);
		cardSetsList.setAdapter(cardSetsDatasource);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = createAddCarSetDialog();
		setupDoAddCardSetButton(dialog);
		setupCardSetNameField(dialog);

		return dialog;
	}

	private void setupDoAddCardSetButton(Dialog dialog) {
		Button button = (Button) dialog.findViewById(R.id.doAddCardSetButton);
		button.setOnClickListener(doAddCardSetButtonListener);
	}

	private void setupCardSetNameField(Dialog dialog) {
		cardSetNameField = (EditText) dialog.findViewById(R.id.cardSetName);
	}

	private Dialog createAddCarSetDialog() {
		Dialog dialog = new Dialog(this);
		dialog.setTitle(R.string.addCardSetDialogLabel);
		dialog.setContentView(R.layout.add_card_set_dialog);
		return dialog;
	}
}