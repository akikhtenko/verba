package org.verba.mobile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.verba.stardict.Dictionary;
import org.verba.stardict.DictionaryIndexReader;
import org.verba.stardict.WordDefinition;
import org.verba.stardict.WordDefinitionCoordinatesRepository;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;
import org.verba.stardict.WordDefinitionRepository;
import org.verba.xdxf.XdxfParser;
import org.verba.xdxf.XdxfParser.XdxfArticleParseException;
import org.verba.xdxf.node.XdxfElement;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class WordDefinitionDetailsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_definition_details);
        
        desplayWordDefinition("Looking for definition...");
        
        new LookupWordDefinitionTask().execute(getIntent().getStringExtra("wordToLookup"));
    }

	private void desplayWordDefinition(String wordDefinition) {
		TextView wordDefinitionDetailsView = (TextView) findViewById(R.id.wordDefinitionView);
		wordDefinitionDetailsView.setMovementMethod(new ScrollingMovementMethod());
        wordDefinitionDetailsView.setText(wordDefinition);
	}
	
	private class LookupWordDefinitionTask extends AsyncTask<String, Void, String> {
	    protected String doInBackground(String... wordsToLookup) {
	        try {
				return lookupWordDefinitionFromDeictionary(wordsToLookup[0]);
			} catch (IOException e) {
				throw new RuntimeException(String.format("Unexpected error while looking up [%s]", wordsToLookup[0]), e);
			}
	    }
	    
	    private String lookupWordDefinitionFromDeictionary(String wordToLookup) throws IOException {
	    	File path = Environment.getExternalStoragePublicDirectory("verba");
	    	File indexFile = new File(path, "dictionary.idx");
	    	File dictionaryFile = new File(path, "dictionary.dict");
	    	InputStream indexStream = new FileInputStream(indexFile);
	    	InputStream dictionaryStream = new FileInputStream(dictionaryFile);
	    	
			DictionaryIndexReader indexReader = new DictionaryIndexReader(indexStream);
			WordDefinitionCoordinatesRepository coordinatesRepository = new WordDefinitionCoordinatesRepository(indexReader);
			
			WordDefinitionRepository definitionsRepository = new WordDefinitionRepository(dictionaryStream);
			
			Dictionary dictionary = new Dictionary(coordinatesRepository, definitionsRepository);
			
			WordDefinition wordDefinition = null;
			try {
				wordDefinition = dictionary.lookup(wordToLookup);
			} catch (WordDefinitionCoordinatesNotFoundException e) {
				return String.format("%s was not found in the dictionary", wordToLookup);
			} finally {
				indexReader.close();
				dictionaryStream.close();
			}
	    	
			return asPlainText(wordDefinition);
		}

		private String asPlainText(WordDefinition wordDefinition) throws IOException {
			XdxfParser xdxfParser = new XdxfParser();
			
			byte[] pureWordDefinitionData = wordDefinition.bytes();
					
			InputStream xdxfStream = new ByteArrayInputStream(adjustWordDefinitionData(pureWordDefinitionData));
			
			XdxfElement xdxfArticle = null;
			try {
				xdxfArticle = xdxfParser.parse(xdxfStream);
			} catch (XdxfArticleParseException e) {
				throw new RuntimeException("Unexpected error while parsing the word definition", e);
			} finally {
				xdxfStream.close();
			}
			
			return xdxfArticle.asPlainText();
		}

		private byte[] adjustWordDefinitionData(byte[] pureWordDefinitionData) {
			ByteBuffer adjustedWordDefinitionData = ByteBuffer.allocate(pureWordDefinitionData.length + 9);
			
			adjustedWordDefinitionData.put("<ar>".getBytes());
			adjustedWordDefinitionData.put(pureWordDefinitionData);
			adjustedWordDefinitionData.put("</ar>".getBytes());
			
			return adjustedWordDefinitionData.array();
		}

		protected void onPostExecute(String wordDefinitionFound) {
	    	desplayWordDefinition(wordDefinitionFound);
	    }
	}
}