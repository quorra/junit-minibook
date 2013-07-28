/*
 *  Copyright (c) 2013, Leo Przybylski
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met: 
 *
 *  1. Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer. 
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution. 
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *  The views and conclusions contained in the software and documentation are those
 *  of the authors and should not be interpreted as representing official policies, 
 *  either expressed or implied, of the FreeBSD Project.
 */
package com.github.r351574nc3.packtpub.junit.test.ut;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.r351574nc3.packtpub.junit.R;

import com.github.r351574nc3.packtpub.junit.MainActivity;
import com.github.r351574nc3.packtpub.junit.JUnitExample;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {
    private Intent mainIntent;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    /**
     * Setup the Intent and Activity for other tests
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // This is one way to get the intent
        /*
        final Intent intent = new Intent(getInstrumentation().getTargetContext(),
                                         MainActivity.class);
        */
        
        // This is another way
        mainIntent = new Intent(Intent.ACTION_MAIN);
    }

    @MediumTest
    public void testPreconditions() {
        startActivity(mainIntent, null, null);
        assertNotNull(getActivity());

        final Button listen = (Button) getActivity().findViewById(R.id.button_listen);
        assertNotNull(listen);        
    }

    @SmallTest
    public void testButtonExists() {
        startActivity(mainIntent, null, null);
        final MainActivity main = getActivity();

        final int buttonId = R.id.button_listen;
        assertNotNull(main.findViewById(buttonId));
        final Button view = (Button) main.findViewById(buttonId);
        assertEquals("Incorrect label of the button", "Listen", view.getText());
    }

    @SmallTest
    public void testStartJunitExampleActivity() {
        startActivity(mainIntent, null, null);
        final MainActivity main = getActivity();

        // Get access to the button
        final int buttonId = R.id.button_listen;
        final Button view = (Button) main.findViewById(buttonId);
        assertNotNull("Button not allowed to be null", view);

        // Use API's rather than simulating user interaction

        // Trigger the API caused by a click
        getActivity().clickExample(view);

        final JUnitExample junitExampleActivity = (JUnitExample) getActivity();
    
        /*
        // Check the intent which was started
        final Intent triggeredIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", triggeredIntent);

        // Compare extradata
        final String data = triggeredIntent.getExtras().getString("URL");
        */

        assertEquals("URL was incorrect","http://r351574nc3.github.io", junitExampleActivity.getUrl());
    }

    @Override
    protected void tearDown() throws Exception {    
        super.tearDown();
    }
}
