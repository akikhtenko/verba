package org.verba.mobile.task;

import static org.verba.mobile.Verba.getVerbaDirectory;

import java.io.IOException;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.task.LookupPhraseDefinitionTask.Request;
import org.verba.mobile.task.LookupPhraseDefinitionTask.Response;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;

import android.os.AsyncTask;

public class LookupPhraseDefinitionTask extends AsyncTask<Request, Void, Response> {
	private PhraseDefinitionDetailsActivity activity;

	public LookupPhraseDefinitionTask(PhraseDefinitionDetailsActivity activity) {
		this.activity = activity;
	}

	@Override
	protected Response doInBackground(Request... requests) {
		try {
			PhraseDefinitionLookup phraseDefinitionLookup =
									new PhraseDefinitionLookup(getVerbaDirectory(), requests[0].getDictionaryName());
			PhraseDefinition phraseDefinition =
									phraseDefinitionLookup.lookupPhraseDefinition(requests[0].getPhraseDefinitionCoordinates());

			return new Response(requests[0].getDictionaryName(), phraseDefinition);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Unexpected error while looking up [%s]",
					requests[0].getPhraseDefinitionCoordinates().getTargetPhrase()), e);
		}
	}

	@Override
	protected void onPostExecute(Response response) {
		activity.displayPhraseDefinition(response);
	}

	public static class Request {
		private String dictionaryName;
		private PhraseDefinitionCoordinates phraseDefinitionCoordinates;

		public Request(String dictionaryName, PhraseDefinitionCoordinates phraseDefinitionCoordinates) {
			this.dictionaryName = dictionaryName;
			this.phraseDefinitionCoordinates = phraseDefinitionCoordinates;
		}

		public String getDictionaryName() {
			return dictionaryName;
		}
		public PhraseDefinitionCoordinates getPhraseDefinitionCoordinates() {
			return phraseDefinitionCoordinates;
		}
	}

	public static class Response {
		private String dictionaryName;
		private PhraseDefinition phraseDefinition;

		public Response(String dictionaryName, PhraseDefinition phraseDefinition) {
			this.dictionaryName = dictionaryName;
			this.phraseDefinition = phraseDefinition;
		}

		public String getDictionaryName() {
			return dictionaryName;
		}
		public PhraseDefinition getPhraseDefinition() {
			return phraseDefinition;
		}
	}
}
