package org.verba.mobile;

import java.util.List;

import org.verba.mobile.card.CardSet;
import org.verba.mobile.card.CardSetDao;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;

public class CardSetPickerActivity extends VerbaActivity implements OnItemClickListener {
	public static final String CARD_SET_ID_PARAMETER = "cardSetId";
	private ListView cardSetsList;
	@Inject private CardSetDao cardSetDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupCardSetsList();
		populateCardSetsList();
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.card_sets;
	}

	private void setupCardSetsList() {
		cardSetsList = (ListView) findViewById(R.id.cardSets);
		cardSetsList.setOnItemClickListener(this);
		registerForContextMenu(cardSetsList);
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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.card_set_menu, menu);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle(cardSetsList.getItemAtPosition(info.position).toString());
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}

	private void openCardSetViewer(int cardSetId) {
		Intent commandToOpenCardSetViewer = new Intent(this, CardSetViewerActivity.class);
		commandToOpenCardSetViewer.putExtra(CARD_SET_ID_PARAMETER, cardSetId);
		startActivity(commandToOpenCardSetViewer);
	}

}