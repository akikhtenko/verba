package org.verba.mobile;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(RobolectricTestRunner.class)
public class WordLookupActivityTest {
	private static final String DUMMY_LLOKUP_WORD = "dummy";
	private WordLookupActivity wordLookupActivity;
	private Button lookupButton;
	private EditText wordToLookupField;

	@Before
	public void prepareActivity() {
		wordLookupActivity = new WordLookupActivity();
		wordLookupActivity.onCreate(null);
		lookupButton = (Button) wordLookupActivity.findViewById(R.id.lookupButton);
		wordToLookupField = (EditText) wordLookupActivity.findViewById(R.id.wordToFindField);
	}

    @Test
	public void shouldHaveVerbaLabel() throws Exception {
		assertThat(wordLookupActivity.getResources().getString(R.string.app_name), is("Verba"));
    }

	@Test
	public void shouldRunWordDefinitionDetailsActivityWithTargetWord() throws Exception {
		wordToLookupField.setText(DUMMY_LLOKUP_WORD);
		lookupButton.performClick();

		ShadowActivity shadowWordLookupActivity = shadowOf(wordLookupActivity);
		Intent startWddIntent = shadowWordLookupActivity.getNextStartedActivity();
		ShadowIntent shadowStartWddIntent = shadowOf(startWddIntent);

		assertThat(shadowStartWddIntent.getComponent().getClassName(), is(WordDefinitionDetailsActivity.class.getName()));
		assertThat(shadowStartWddIntent.getStringExtra("wordToLookup"), is(DUMMY_LLOKUP_WORD));
	}
}

