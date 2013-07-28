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
package com.github.r351574nc3.packtpub.junit.test.it;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.test.suitebuilder.annotation.SmallTest;

import com.github.r351574nc3.packtpub.junit.R;

import com.github.r351574nc3.packtpub.junit.MainActivity;
import com.github.r351574nc3.packtpub.junit.JUnitExample;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity main;

    public MainActivityTest() {
        super("com.github.r351574nc3.packtpub.junit", MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        main = getActivity();
    }

    public void testStartJunitExampleActivity() throws Exception {

        // Create a monitor to use for timeout later
        final ActivityMonitor monitor = getInstrumentation()
            .addMonitor(JUnitExample.class.getName(), null, false);

        // Set text field value
        try {
            runTestOnUiThread(new Runnable() {
                    public void run() {
                        final EditText text = (EditText) main.findViewById(R.id.edit_message);
                        text.setText("Test Me");
                    }
                });
        }
        catch (Throwable t) {
            // Deciding to let this slide
        }

        // Click on the button from the main activity
        final Button view = (Button) main.findViewById(R.id.button_listen);
        TouchUtils.clickView(this, view);

        // Use the monitor to timeout after 2 seconds if the activity doesn't start
        final JUnitExample exampleActivity = (JUnitExample) monitor
            .waitForActivityWithTimeout(2000);

        assertNotNull(exampleActivity);
        
        assertEquals("Wrong message", "Test Me", exampleActivity.getMessage());
    }

} 