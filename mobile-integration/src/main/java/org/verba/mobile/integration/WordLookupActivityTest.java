package org.verba.mobile.integration;

import android.test.ActivityInstrumentationTestCase2;
import org.verba.mobile.WordLookupActivity;

public class WordLookupActivityTest extends ActivityInstrumentationTestCase2<WordLookupActivity> {

    public WordLookupActivityTest() {
        super(WordLookupActivity.class); 
    }

    public void testActivity() {
        WordLookupActivity activity = getActivity();
        assertNotNull(activity);
    }
}

