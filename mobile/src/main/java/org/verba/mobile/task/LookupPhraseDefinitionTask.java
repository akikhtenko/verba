package org.verba.mobile.task;

import static org.verba.mobile.Verba.getVerbaDirectory;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.task.LookupPhraseDefinitionTask.Response;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;

import roboguice.util.RoboAsyncTask;

public class LookupPhraseDefinitionTask extends RoboAsyncTask<Response> {
	private PhraseDefinitionDetailsActivity activity;
	private Request request;

	public LookupPhraseDefinitionTask(PhraseDefinitionDetailsActivity activity, Request request) {
		super(activity);
		this.activity = activity;
		this.request = request;
	}

	@Override
	public Response call() throws Exception {
		PhraseDefinitionLookup phraseDefinitionLookup =
								new PhraseDefinitionLookup(getVerbaDirectory(), request.getDictionaryName());
		PhraseDefinition phraseDefinition =
								phraseDefinitionLookup.lookupPhraseDefinition(request.getPhraseDefinitionCoordinates());

		return new Response(request.getDictionaryName(), phraseDefinition);
	}

	@Override
	protected void onSuccess(Response response) throws Exception {
		activity.displayPhraseDefinition(response);
	}

	@Override
	protected void onException(Exception e) {
		throw new RuntimeException(String.format("Unexpected error while looking up [%s]",
										request.getPhraseDefinitionCoordinates().getTargetPhrase()), e);
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
