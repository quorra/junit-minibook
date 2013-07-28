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
package com.github.r351574nc3.packtpub.junit.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.r351574nc3.packtpub.junit.R;

import com.github.r351574nc3.packtpub.junit.MainActivity;
import com.github.r351574nc3.packtpub.junit.JUnitExample;

import static org.hamcrest.CoreMatchers.equalTo;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.robolectric.Robolectric.clickOn;
import static org.robolectric.Robolectric.shadowOf;

import static org.junit.Assert.*;

import static android.widget.TextView.BufferType.NORMAL;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    /**
     * Setup the Intent and Activity for other tests
     */
    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().get();
    }

    protected Activity getActivity() {
        return activity;
    }

    @Test
    public void testPreconditions() {
        final Button listen = (Button) getActivity().findViewById(R.id.button_listen);
        assertNotNull(listen);        
    }

    @Test
    public void testButtonExists() {
        final MainActivity main = (MainActivity) getActivity();

        final int buttonId = R.id.button_listen;
        assertNotNull(main.findViewById(buttonId));
        final Button view = (Button) main.findViewById(buttonId);
        assertEquals("Incorrect label of the button", "Listen", view.getText());
    }

    @Test
    public void testStartJunitExampleActivity() {
        final MainActivity main = (MainActivity) getActivity();

        // Get access to the button
        final int buttonId = R.id.button_listen;
        final Button view = (Button) main.findViewById(buttonId);
        assertNotNull("Button not allowed to be null", view);

        // Get access to the edit text
        final int textId = R.id.edit_message;
        final EditText text = (EditText) main.findViewById(textId);
        assertNotNull("Button not allowed to be null", text);
        text.setText("Do tgat tging", NORMAL);

        view.performClick();

        // Use API's rather than simulating user interaction

        // Trigger the API caused by a click
        final ShadowActivity shadowActivity = shadowOf(activity);
        final Intent startedIntent = shadowActivity.getNextStartedActivity();
        final ShadowIntent shadowIntent = shadowOf(startedIntent);

        assertThat(shadowIntent.getComponent().getClassName(), equalTo(JUnitExample.class.getName()));
    }
}
