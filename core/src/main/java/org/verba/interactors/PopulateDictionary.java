package org.verba.interactors;

import static org.apache.commons.io.IOUtils.closeQuietly;

import org.verba.DictionaryDataObject;
import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryEntryRepository.Operation;
import org.verba.DictionaryRepository;
import org.verba.stardict.PhraseDefinitionCoordinates;
import org.verba.stardict.index.DictionaryIndex;
import org.verba.stardict.index.DictionaryIndexGateway;
import org.verba.stardict.metadata.DictionaryMetadata;
import org.verba.stardict.metadata.DictionaryMetadataGateway;

public class PopulateDictionary {
	private static final int DEFAULT_PROGRESS_NOTIFICATION_DELTA = 100;
	private static final int ENTRIES_BATCH_SIZE = 5000;
	private DictionaryMetadataGateway metadataGateway;
	private DictionaryIndexGateway indexGateway;
	private DictionaryRepository dictionaryRepository;
	private DictionaryEntryRepository dictionaryEntryRepository;
	private int insertedEntries;
	private DictionaryPopulationProgressListener progressListener;
	private int progressNotificationDelta;

	public PopulateDictionary(DictionaryMetadataGateway metadataGateway,
										DictionaryIndexGateway indexGateway,
										DictionaryRepository dictionaryRepository,
										DictionaryEntryRepository dictionaryEntryRepository) {
		this.dictionaryRepository = dictionaryRepository;
		this.dictionaryEntryRepository = dictionaryEntryRepository;
		this.metadataGateway = metadataGateway;
		this.indexGateway = indexGateway;
		this.progressNotificationDelta = DEFAULT_PROGRESS_NOTIFICATION_DELTA;
	}

	public void with(String dictionaryName) {
		int dictionaryId = createNewDictinary(dictionaryName);
		populateDictionaryEntriesFor(dictionaryId, dictionaryName);
	}

	private int createNewDictinary(String dictionaryName) {
		DictionaryMetadata dictionaryMetadata = metadataGateway.getMetadataFor(dictionaryName);

		return dictionaryRepository.addDictionary(createDictionaryDataObject(dictionaryName, dictionaryMetadata));
	}

	private DictionaryDataObject createDictionaryDataObject(String dictionaryName, DictionaryMetadata dictionaryMetadata) {
		DictionaryDataObject dictionaryDataObject = new DictionaryDataObject();
		dictionaryDataObject.setName(dictionaryName);
		dictionaryDataObject.setDescription(dictionaryMetadata.getName());

		return dictionaryDataObject;
	}

	private void populateDictionaryEntriesFor(int dictionaryId, String dictionaryName) {
		DictionaryIndex indexReader = indexGateway.getDictionaryIndexFor(dictionaryName);

		try {
			populateDictionaryEntriesFrom(indexReader, dictionaryId);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while iterating over index entries", e);
		} finally {
			closeQuietly(indexReader);
		}
	}

	private void populateDictionaryEntriesFrom(final DictionaryIndex indexReader, final int dictionaryId)
			throws Exception {
		while (indexReader.hasNextPhraseDefinitionCoordinates()) {
			dictionaryEntryRepository.doInOneGo(new Operation() {
				@Override
				public void doInOneGo() throws Exception {
					int entriesInBatch = 0;
					while (entriesInBatch < ENTRIES_BATCH_SIZE && indexReader.hasNextPhraseDefinitionCoordinates()) {
						PhraseDefinitionCoordinates phraseDefinitionCoordinates = indexReader.readPhraseDefinitionCoordinates();
						DictionaryEntryDataObject dictionaryEntry = new DictionaryEntryDataObject(dictionaryId,
								phraseDefinitionCoordinates);
						dictionaryEntryRepository.addDictionaryEntry(dictionaryEntry);

						insertedEntries++;
						entriesInBatch++;
						publishProgressIfNecessary();
					}
				}
			});
		}
	}

	private void publishProgressIfNecessary() {
		if (progressListener != null && insertedEntries % progressNotificationDelta == 0) {
			progressListener.onChunkPopulated();
		}
	}

	public void registerProgressListener(DictionaryPopulationProgressListener aProgressListener) {
		this.progressListener = aProgressListener;
	}

	public void setProgressNotificationDelta(int aProgressNotificationDelta) {
		progressNotificationDelta = aProgressNotificationDelta;
	}
}
