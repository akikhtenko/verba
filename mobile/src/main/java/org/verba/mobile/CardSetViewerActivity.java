package org.verba.mobile;

import static org.verba.mobile.CardSetPickerActivity.CARD_SET_ID_PARAMETER;

import java.util.List;

import org.verba.mobile.card.Card;
import org.verba.mobile.card.CardDao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;

public class CardSetViewerActivity extends VerbaActivity implements OnItemClickListener {
	public static final String CARD_ID_PARAMETER = "cardId";
	@Inject private CardDao cardDao;
	private ListView cardsList;
	private int cardSetId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cardSetId = getIntent().getIntExtra(CARD_SET_ID_PARAMETER, -1);
		setupCardsList();
		populateCardsList();
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.card_set_viewer;
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
}