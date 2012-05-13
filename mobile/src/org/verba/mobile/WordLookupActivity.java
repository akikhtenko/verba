package org.verba.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WordLookupActivity extends Activity implements OnClickListener {
    public void onClick(View v) {
    	Intent commandToOpenWordDefinitionDetails = new Intent(this, WordDefinitionDetailsActivity.class);
    	commandToOpenWordDefinitionDetails.putExtra("wordToLookup", getWordToLookup());
    	startActivity(commandToOpenWordDefinitionDetails);
    }


	private String getWordToLookup() {
		EditText wordToLookupField = (EditText) findViewById(R.id.wordToFindField);
    	return wordToLookupField.getText().toString();
	}

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.lookupButton);
        button.setOnClickListener(this);
    }
}