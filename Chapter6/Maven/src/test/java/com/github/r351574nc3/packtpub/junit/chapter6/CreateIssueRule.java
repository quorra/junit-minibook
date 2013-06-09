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
package com.github.r351574nc3.packtpub.junit.chapter6;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 *
 * @author Leo Przybylski 
 */
public class CreateIssueRule implements TestRule {
    protected static final String TOKEN_KEY = "token";
    protected static final String TITLE_KEY = "title";
    protected static final String BODY_KEY  = "body";
    protected static final String TITLE_FORMAT = "Test Failed: %s";
    protected static final String BODY_FORMAT  = "Failure Cause: %s %n%s";

    protected String retrieveToken() {
        final HttpHost targetHost = new HttpHost("api.github.com", 443, "https");
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        // final HttpPost httpMethod   = new HttpPost(githubIssueUrl);
        final HttpPost httpMethod = new HttpPost("/authorizations");
        
        httpClient.getCredentialsProvider().setCredentials(
            new AuthScope(targetHost.getHostName(), targetHost.getPort()),
            new UsernamePasswordCredentials("r351574nc3", System.getProperty("test.github.password")));
        // Create AuthCache instance
        final AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        final BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);
        
        // Add AuthCache to the execution context
        final BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

        try {
            httpMethod.setEntity(new StringEntity("{\"scopes\": [\"repo\"]}"));
        }
        catch (Exception e) {
            return null;
        }
        
        try {
            final HttpResponse response = httpClient.execute(targetHost, httpMethod, localcontext);
            final JsonFactory factory = new JsonFactory(); 
            final ObjectMapper mapper = new ObjectMapper(factory);
            
            final TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>(){}; 
            
            final Map<String,Object> responseMap = mapper.readValue(response.getEntity().getContent(), typeRef); 
            return (String) responseMap.get(TOKEN_KEY);
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            httpClient.getConnectionManager().shutdown();
        }
        
        
        return null;
    }
    
    
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate(); // Run the original statement
                } catch (Throwable t) { // Captures any errors, exceptions, or test failures
                    createIssue(description, t);
                    throw t; // Rethrow it back to JUnit for handling
                }
            }


            public void createIssue(final Description description, final Throwable t) {
                final String authToken = retrieveToken();

                final String issueUri = "/repos/r351574nc3/junit-minibook/issues";
                final HttpHost targetHost = new HttpHost("api.github.com", 443, "https");
                final HttpClient httpClient = new DefaultHttpClient();
                final BasicHttpContext localcontext = new BasicHttpContext();
                final HttpPost httpMethod   = new HttpPost(issueUri);
                httpMethod.addHeader("Authorization", TOKEN_KEY + " " + authToken);


                final JsonFactory factory = new JsonFactory(); 
                final ObjectMapper mapper = new ObjectMapper(factory);
                final StringWriter bodyWriter = new StringWriter();
                t.printStackTrace(new PrintWriter(bodyWriter));

                try {
                    final String jsonData = mapper.writeValueAsString(new HashMap<String,Object>() {{
                                put(TITLE_KEY, String.format(TITLE_FORMAT, description.getDisplayName()));
                                put(BODY_KEY, String.format(BODY_FORMAT, t.getMessage(), bodyWriter.toString()));
                            }}); 
                
                    httpMethod.setEntity(new StringEntity(jsonData));
                }
                catch (Exception e) {
                }

                try {
                    final HttpResponse response = httpClient.execute(targetHost, httpMethod);
                    System.out.println("Got status code " + response.getStatusLine());
                } catch (IOException e) {
                    System.err.println("Fatal transport error: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Release the connection.
                    httpClient.getConnectionManager().shutdown();
                } 
            }
        };
    }
}