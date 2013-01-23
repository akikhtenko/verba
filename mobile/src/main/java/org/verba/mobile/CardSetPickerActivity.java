package org.verba.mobile;

import java.util.List;

import org.verba.CardSet;
import org.verba.interactors.GetCardSet;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
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

@ContentView(R.layout.card_sets)
public class CardSetPickerActivity extends VerbaActivity implements OnItemClickListener {
	public static final String CARD_SET_ID_PARAMETER = "cardSetId";
	@InjectView(R.id.cardSets) private ListView cardSetsList;
	@Inject private GetCardSet getCardSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupCardSetsList();
		populateCardSetsList();
	}

	private void setupCardSetsList() {
		cardSetsList.setOnItemClickListener(this);
//		registerForContextMenu(cardSetsList);
	}

	private void populateCardSetsList() {
		List<CardSet> cardSets = getCardSet.all();
		ArrayAdapter<CardSet> cardSetsDatasource = new ArrayAdapter<CardSet>(this, R.layout.card_sets_list_item,
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