package org.verba.mobile;

import java.util.List;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.card.CardSet;
import org.verba.mobile.card.CardSetDao;

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

public class CardSetPickerActivity extends Activity implements OnItemClickListener, ServiceConnection {
	public static final String CARD_SET_ID_PARAMETER = "cardSetId";
	private CardSetDao cardSetDao;
	private ListView cardSetsList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_sets);

		setupCardSetsList();
	}

	private void setupCardSetsList() {
		cardSetsList = (ListView) findViewById(R.id.cardSets);
		cardSetsList.setOnItemClickListener(this);
	}

	private void populateCardSetsList() {
		List<CardSet> cardSets = cardSetDao.getAllCardSets();
		ArrayAdapter<CardSet> cardSetsDatasource = new ArrayAdapter<CardSet>(this, R.layout.list_item,
				R.id.listItemTitle, cardSets);
		cardSetsList.setAdapter(cardSetsDatasource);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CardSet selectedCardSet = (CardSet) cardSetsList.getItemAtPosition(position);
		openCardSetViewer(selectedCardSet.getId());
	}

	private void openCardSetViewer(int cardSetId) {
		Intent commandToOpenCardSetViewer = new Intent(this, CardSetViewerActivity.class);
		commandToOpenCardSetViewer.putExtra(CARD_SET_ID_PARAMETER, cardSetId);
		startActivity(commandToOpenCardSetViewer);
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

		if (cardSetDao != null) {
			unbindService(this);
			cardSetDao = null;
		}
	}

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		DictionaryBinder binder = (DictionaryBinder) service;
		cardSetDao = binder.getCardSetDao();
		populateCardSetsList();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		cardSetDao = null;
	}
}