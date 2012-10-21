package org.verba.mobile.utils;

import static org.verba.mobile.Verba.getVerbaDirectory;

import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.boundary.CardAddition;
import org.verba.boundary.CardRetrieval;
import org.verba.boundary.CardSetAddition;
import org.verba.boundary.CardSetRetrieval;
import org.verba.boundary.NewDictionariesScanner;
import org.verba.boundary.PhraseLookup;
import org.verba.boundary.SuggestionAdviser;
import org.verba.card.CardRepository;
import org.verba.cardset.CardSetRepository;
import org.verba.interactors.NewStardictDictionariesScanner;
import org.verba.interactors.SimpleCardAddition;
import org.verba.interactors.SimpleCardRetrieval;
import org.verba.interactors.SimpleCardSetAddition;
import org.verba.interactors.SimpleCardSetRetrieval;
import org.verba.interactors.SimpleSuggestionAdviser;
import org.verba.interactors.StardictPhraseLookup;
import org.verba.mobile.Verba;
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

		bind(DictionaryMetadataGateway.class).toInstance(new StardictDictionaryMetadataGateway(dictionaryFileFinder));
		bind(DictionaryIndexGateway.class).toInstance(new StardictDictionaryIndexGateway(dictionaryFileFinder));

		bind(DictionaryRepository.class).toInstance(dictionaryRepository);
		bind(DictionaryEntryRepository.class).toInstance(dictionaryEntryRepository);
		bind(CardSetRepository.class).toInstance(cardSetRepository);
		bind(CardRepository.class).toInstance(cardRepository);

		bind(NewDictionariesScanner.class).toInstance(
				new NewStardictDictionariesScanner(getVerbaDirectory(), dictionaryRepository));
		bind(SuggestionAdviser.class).toInstance(new SimpleSuggestionAdviser(dictionaryEntryRepository));
		bind(PhraseLookup.class).toInstance(new StardictPhraseLookup(
				dictionaryRepository, dictionaryEntryRepository, new StardictDictionaryDefinitionsGateway(dictionaryFileFinder)));
		bind(CardSetRetrieval.class).toInstance(new SimpleCardSetRetrieval(cardSetRepository));
		bind(CardSetAddition.class).toInstance(new SimpleCardSetAddition(cardSetRepository));
		bind(CardRetrieval.class).toInstance(new SimpleCardRetrieval(cardRepository));
		bind(CardAddition.class).toInstance(new SimpleCardAddition(cardRepository));
	}

}