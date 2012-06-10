package org.verba.mobile.integration;

import android.test.ActivityInstrumentationTestCase2;
import org.verba.mobile.PhraseLookupActivity;

public class WordLookupActivityTest extends ActivityInstrumentationTestCase2<PhraseLookupActivity> {

    public WordLookupActivityTest() {
        super(PhraseLookupActivity.class); 
    }

    public void testActivity() {
        PhraseLookupActivity activity = getActivity();
        assertNotNull(activity);
    }
}

