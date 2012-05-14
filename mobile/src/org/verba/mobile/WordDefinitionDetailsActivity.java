package org.verba.mobile;

import static org.verba.xdxf.node.XdxfNodeType.PLAIN_TEXT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.verba.stardict.Dictionary;
import org.verba.stardict.DictionaryIndexReader;
import org.verba.stardict.WordDefinition;
import org.verba.stardict.WordDefinitionCoordinatesRepository;
import org.verba.stardict.WordDefinitionCoordinatesRepository.WordDefinitionCoordinatesNotFoundException;
import org.verba.stardict.WordDefinitionRepository;
import org.verba.xdxf.XdxfWordDefinitionPart;
import org.verba.xdxf.node.XdxfElement;
import org.verba.xdxf.node.XdxfNode;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class WordDefinitionDetailsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_definition_details);
        
        doDosplay("Looking for definition...");
        
        new LookupWordDefinitionTask().execute(getIntent().getStringExtra("wordToLookup"));
    }

	private void desplayWordDefinition(WordDefinition wordDefinition) {
		if (wordDefinition == null) {
			doDosplay("Nothing found in the dictionary");
		} else {
			XdxfWordDefinitionPart wordDefinitionPart = (XdxfWordDefinitionPart) wordDefinition.iterator().next();
			doDosplay(asSpannableString(wordDefinitionPart.asXdxfArticle()));
		}
	}

	private void doDosplay(CharSequence toDisplay) {
		TextView wordDefinitionDetailsView = (TextView) findViewById(R.id.wordDefinitionView);
		wordDefinitionDetailsView.setMovementMethod(new ScrollingMovementMethod());
        wordDefinitionDetailsView.setText(toDisplay, BufferType.SPANNABLE);
	}
	
	private CharSequence asSpannableString(XdxfElement xdxfArticle) {
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		
		processChild(xdxfArticle, spannable);
		
		return spannable;
	}
	
	private int processChild(XdxfNode xdxfNode, SpannableStringBuilder spannable) {
		if (xdxfNode.getType() != PLAIN_TEXT) {
			int totalChildrenLength = 0;
			for(Iterator<XdxfNode> i = ((XdxfElement) xdxfNode).iterator(); i.hasNext(); ) {
				XdxfNode nextNode = i.next();
				
				totalChildrenLength += processChild(nextNode, spannable);
			}
			
			int lengthBefore = spannable.length();
			switch (xdxfNode.getType()) {
				case KEY_PHRASE:
					spannable.setSpan(new TextAppearanceSpan(this, R.style.KeyPhrase), spannable.length() - totalChildrenLength, spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					break;
				case BOLD_PHRASE:
					spannable.setSpan(new TextAppearanceSpan(this, R.style.BoldPhrase), spannable.length() - totalChildrenLength, spannable.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					break;
				default:
					break;
			}
			
			return totalChildrenLength;
		} else {
			String plainTextNodeValue = xdxfNode.asPlainText();
			spannable.append(plainTextNodeValue);
			
			return plainTextNodeValue.length();
		}
	}
	
	private class LookupWordDefinitionTask extends AsyncTask<String, Void, WordDefinition> {
	    protected WordDefinition doInBackground(String... wordsToLookup) {
	        try {
				return lookupWordDefinitionFromDeictionary(wordsToLookup[0]);
			} catch (IOException e) {
				throw new RuntimeException(String.format("Unexpected error while looking up [%s]", wordsToLookup[0]), e);
			}
	    }
	    
	    private WordDefinition lookupWordDefinitionFromDeictionary(String wordToLookup) throws IOException {
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
				return null;
			} finally {
				indexReader.close();
				dictionaryStream.close();
			}
	    	
			return wordDefinition;
		}
		
		protected void onPostExecute(WordDefinition wordDefinitionFound) {
	    	desplayWordDefinition(wordDefinitionFound);
	    }
	}
}