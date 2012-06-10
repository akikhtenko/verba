package org.verba.mobile;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;

@RunWith(RobolectricTestRunner.class)
public class PhraseLookupActivityTest {
	private static final String DUMMY_LOOKUP_PHRASE = "dummy";
	private PhraseLookupActivity phraseLookupActivity;
	private Button lookupButton;
	private EditText wordToLookupField;

	@Before
	public void prepareActivity() {
		phraseLookupActivity = new PhraseLookupActivity();
		phraseLookupActivity.onCreate(null);
		lookupButton = (Button) phraseLookupActivity.findViewById(R.id.lookupButton);
		wordToLookupField = (EditText) phraseLookupActivity.findViewById(R.id.wordToFindField);
	}

	@Test
	public void shouldHaveVerbaLabel() throws Exception {
		assertThat(phraseLookupActivity.getResources().getString(R.string.app_name), is("Verba"));
	}

	@Test
	@Ignore
	public void shouldRunPhraseDefinitionDetailsActivityWithTargetPhrase() throws Exception {
		wordToLookupField.setText(DUMMY_LOOKUP_PHRASE);
		lookupButton.performClick();

		ShadowActivity shadowWordLookupActivity = shadowOf(phraseLookupActivity);
		Intent startWddIntent = shadowWordLookupActivity.getNextStartedActivity();
		ShadowIntent shadowStartWddIntent = shadowOf(startWddIntent);

		assertThat(shadowStartWddIntent.getComponent().getClassName(), is(PhraseDefinitionDetailsActivity.class.getName()));
		assertThat(shadowStartWddIntent.getStringExtra("wordToLookup"), is(DUMMY_LOOKUP_PHRASE));
	}
}

