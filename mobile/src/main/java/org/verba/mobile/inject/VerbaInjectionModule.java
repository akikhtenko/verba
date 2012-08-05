package org.verba.mobile.inject;

import org.verba.mobile.Verba;
import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSetDao;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryEntryDao;

import android.content.Context;

import com.google.inject.AbstractModule;

public class VerbaInjectionModule extends AbstractModule {
	private Verba verbaApplication;

	public VerbaInjectionModule(Context application) {
		this.verbaApplication = (Verba) application;
	}

	@Override
	protected void configure() {
		bind(DictionaryDao.class).toInstance(verbaApplication.getDictionaryDao());
		bind(DictionaryEntryDao.class).toInstance(verbaApplication.getDictionaryEntryDao());
		bind(CardSetDao.class).toInstance(verbaApplication.getCardSetDao());
		bind(CardDao.class).toInstance(verbaApplication.getCardDao());
	}

}
