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
package com.github.r351574nc3.packtpub.junit.chapter7;

import java.io.File;
import java.io.FileOutputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *
 * @author Leo Przybylski 
 */
public class TakeScreenshotRule implements TestRule {
    protected static final String SCREENSHOT_PATH_FMT = "target/failsafe-reports/screenshot-%s.png";
    protected WebDriver driver;

    public TakeScreenshotRule(final WebDriver driver) {
        this.driver = driver;
    }
    
    public void captureScreenshot(String fileName) {
        if (!(driver instanceof TakesScreenshot)) { // Check if the driver even support screenshots
            return;
        }

        FileOutputStream out = null;
        try {
            new File("target/failsafe-reports/").mkdirs(); // Insure directory is there
            out = new FileOutputStream(String.format(SCREENSHOT_PATH_FMT, fileName));
            out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        } catch (Exception e) {
            // No need to crash the tests if the screenshot fails
        }
        finally {
            try {
                out.close();
            }
            catch (Exception e) {}
        }
    }

    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate(); // Run the original statement
                } catch (Throwable t) { // Captures any errors, exceptions, or test failures
                    captureScreenshot(description.getDisplayName());
                    throw t; // Rethrow it back to JUnit for handling
                }
            }
        };
    }
}