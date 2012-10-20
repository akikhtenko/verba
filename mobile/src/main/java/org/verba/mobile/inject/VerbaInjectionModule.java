package org.verba.mobile.inject;

import static org.verba.mobile.Verba.getVerbaDirectory;

import org.verba.mobile.Verba;
import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSetDao;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryEntryDao;
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

		bind(DictionaryDao.class).toInstance(verbaApplication.getDictionaryDao());
		bind(DictionaryEntryDao.class).toInstance(verbaApplication.getDictionaryEntryDao());
		bind(CardSetDao.class).toInstance(verbaApplication.getCardSetDao());
		bind(CardDao.class).toInstance(verbaApplication.getCardDao());
		bind(DictionaryMetadataGateway.class).toInstance(new StardictDictionaryMetadataGateway(dictionaryFileFinder));
		bind(DictionaryIndexGateway.class).toInstance(new StardictDictionaryIndexGateway(dictionaryFileFinder));
	}

}
