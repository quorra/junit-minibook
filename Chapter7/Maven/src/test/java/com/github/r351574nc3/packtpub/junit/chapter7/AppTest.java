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

import static org.junit.Assert.assertEquals;

import com.github.r351574nc3.packtpub.junit.IntegrationTests;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.junit.experimental.categories.Category;
import org.junit.experimental.categories.Categories;

import static org.junit.Assert.*;

/**
 * Tests for Junit minibook selenium tests.
 *
 * @author Leo Przybylski
 */
public class AppTest {
    @Rule
    public TakeScreenshotRule screenshotTestRule = new TakeScreenshotRule(driver);
    
    public static WebDriver driver = new FirefoxDriver();
    
    @Before
    public void createDriver() {
    }

    @After
    public void quitDriver() {
        driver.quit();
    }    

    @Test
    @Category(IntegrationTests.class)
    public void thisAlwaysPasses() {
        driver.get("http://localhost:9090/test-example/");        
        final RepeatPage page = new RepeatPage(driver);
        page.typeRepeatText("Coming from a test")
            .clickSubmit();
        assertTrue("Jersey say : Coming from a test".equals(page.getRepeatedText()));
    }

    @Test
    @Category(IntegrationTests.class)
    public void thisAlwaysFails() {
        driver.get("http://localhost:9090/test-example/");        
        final RepeatPage page = new RepeatPage(driver);
        page.typeRepeatText("Coming from a test")
            .clickSubmit();
        fail();
    }
}
