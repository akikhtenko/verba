package org.verba.mobile.utils;

import static org.verba.mobile.Verba.getVerbaDirectory;

import org.verba.Card;
import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.card.CardRepository;
import org.verba.cardset.CardSetRepository;
import org.verba.interactors.AddCard;
import org.verba.interactors.AddCardSet;
import org.verba.interactors.GetCard;
import org.verba.interactors.GetCardSet;
import org.verba.interactors.GetCards;
import org.verba.interactors.GetNewDictionaries;
import org.verba.interactors.GetSuggestions;
import org.verba.interactors.LookupPhrase;
import org.verba.mobile.Verba;
import org.verba.mobile.provider.SoughtCardProvider;
import org.verba.mobile.provider.SoughtCard;
import org.verba.stardict.definitions.StardictDictionaryDefinitionsGateway;
import org.verba.stardict.index.DictionaryIndexGateway;
import org.verba.stardict.index.StardictDictionaryIndexGateway;
import org.verba.stardict.metadata.DictionaryMetadataGateway;
import org.verba.stardict.metadata.StardictDictionaryMetadataGateway;
import org.verba.util.DictionaryFileFinder;

import android.content.Context;

import com.google.inject.AbstractModule;

public class VerbaInjectionModule extends AbstractModule {
	private Verba verbaApplication;

	public VerbaInjectionModule(Context application) {
		this.verbaApplication = (Verba) application;
	}

	@Override
	protected void configure() {
		DictionaryFileFinder dictionaryFileFinder = new DictionaryFileFinder(getVerbaDirectory());

		DictionaryRepository dictionaryRepository = verbaApplication.getDictionaryRepository();
		DictionaryEntryRepository dictionaryEntryRepository = verbaApplication.getDictionaryEntryRepository();
		CardSetRepository cardSetRepository = verbaApplication.getCardSetRepository();
		CardRepository cardRepository = verbaApplication.getCardRepository();

		DictionaryMetadataGateway dictionaryMetadataGateway = new StardictDictionaryMetadataGateway(dictionaryFileFinder);

		bind(DictionaryMetadataGateway.class).toInstance(dictionaryMetadataGateway);
		bind(DictionaryIndexGateway.class).toInstance(new StardictDictionaryIndexGateway(dictionaryFileFinder));

		bind(DictionaryRepository.class).toInstance(dictionaryRepository);
		bind(DictionaryEntryRepository.class).toInstance(dictionaryEntryRepository);
		bind(CardSetRepository.class).toInstance(cardSetRepository);
		bind(CardRepository.class).toInstance(cardRepository);

		bind(GetNewDictionaries.class).toInstance(new GetNewDictionaries(getVerbaDirectory(), dictionaryRepository));
		bind(GetSuggestions.class).toInstance(new GetSuggestions(dictionaryEntryRepository));
		bind(LookupPhrase.class).toInstance(new LookupPhrase(dictionaryRepository, dictionaryEntryRepository, dictionaryMetadataGateway,
						new StardictDictionaryDefinitionsGateway(dictionaryFileFinder)));
		bind(GetCardSet.class).toInstance(new GetCardSet(cardSetRepository));
		bind(AddCardSet.class).toInstance(new AddCardSet(cardSetRepository));
		bind(GetCard.class).toInstance(new GetCard(cardRepository));
		bind(GetCards.class).toInstance(new GetCards(cardRepository));
		bind(AddCard.class).toInstance(new AddCard(cardRepository));

		bind(Card.class).annotatedWith(SoughtCard.class).toProvider(SoughtCardProvider.class);
	}

}