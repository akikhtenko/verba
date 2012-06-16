package org.verba.mobile;

import static android.widget.Toast.LENGTH_LONG;
import static org.verba.mobile.CardSetViewerActivity.CARD_ID_PARAMETER;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.card.Card;
import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardDao.NoCardFoundException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCardActivity extends Activity implements ServiceConnection {
	private CardDao cardDao;
	private TextView cardPhraseField;
	private TextView cardDefinitionField;
	private int cardId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_card);

		cardId = getIntent().getIntExtra(CARD_ID_PARAMETER, -1);
		setupCardPhraseField();
		setupCardDefinitionField();
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

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = new Intent(this, DictionaryDataService.class);
		bindService(intent, this, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (cardDao != null) {
			unbindService(this);
			cardDao = null;
		}
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		DictionaryBinder binder = (DictionaryBinder) service;
		cardDao = binder.getCardDao();
		populateCardFields();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		cardDao = null;
	}
}