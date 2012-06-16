package org.verba.mobile;

import static org.verba.mobile.CardSetPickerActivity.CARD_SET_ID_PARAMETER;

import java.util.List;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.card.Card;
import org.verba.mobile.card.CardDao;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CardSetViewerActivity extends Activity implements OnItemClickListener, ServiceConnection {
	public static final String CARD_ID_PARAMETER = "cardId";
	private CardDao cardDao;
	private ListView cardsList;
	private int cardSetId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_set_viewer);

		cardSetId = getIntent().getIntExtra(CARD_SET_ID_PARAMETER, -1);
		setupCardsList();
	}

	private void setupCardsList() {
		cardsList = (ListView) findViewById(R.id.cards);
		cardsList.setOnItemClickListener(this);
	}

	private void populateCardsList() {
		List<Card> cardSets = cardDao.getCardsInCardSet(cardSetId, Integer.MAX_VALUE);
		ArrayAdapter<Card> cardSetsDatasource = new ArrayAdapter<Card>(this, R.layout.list_item, R.id.listItemTitle,
				cardSets);
		cardsList.setAdapter(cardSetsDatasource);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Card selectedCard = (Card) cardsList.getItemAtPosition(position);
		openCardViewer(selectedCard.getId());
	}

	private void openCardViewer(int cardId) {
		Intent commandToOpenCardViewer = new Intent(this, ViewCardActivity.class);
		commandToOpenCardViewer.putExtra(CARD_ID_PARAMETER, cardId);
		startActivity(commandToOpenCardViewer);
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
		populateCardsList();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		cardDao = null;
	}
}