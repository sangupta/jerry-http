/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2015, Sandeep Gupta
 * 
 * http://sangupta.com/projects/jerry-http
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.jerry.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * A wrapper over the {@link HttpResponse} object that is returned as 
 * part of {@link HttpRequest} execution. This helps in extracting the
 * final {@link WebResponse} object.
 * 
 * @author sangupta
 * 
 * @since 0.3
 */
public class WebRawResponse {

	/**
	 * Internal {@link HttpResponse} handle
	 */
	private final HttpResponse response;
	
	/**
	 * Internal {@link HttpContext} handle
	 */
	private final HttpContext localHttpContext;
	
	/**
	 * Flag that signifies if the response stream has been consumed
	 * or not.
	 */
    private boolean consumed;

    /**
	 * Constructor that takes a {@link HttpResponse} object and stores it
	 * internally for processing.
	 * 
	 * @param response
	 *            the actual {@link HttpResponse} object that was returned from
	 *            the server
	 * 
	 * @param localHttpContext
	 *            the local {@link HttpContext} as applicable to this request
	 * 
	 */
    WebRawResponse(final HttpResponse response, HttpContext localHttpContext) {
        super();
        this.response = response;
        this.localHttpContext = localHttpContext;
    }

    /**
     * Check if the response stream has not already been consumed.
     * 
     */
    private void assertNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException("Response content has been already consumed");
        }
    }

    /**
     * Dispose off the response, after consuming.
     * 
     */
    private void dispose() {
        if (this.consumed) {
            return;
        }
        try {
            EntityUtils.consume(this.response.getEntity());
        } catch (Exception ignore) {
        } finally {
            this.consumed = true;
        }
    }

    /**
     * Discard any content off the response stream, if pending.
     * 
     */
    public void discardContent() {
        dispose();
    }

    /**
	 * Handle the response using the given response handler.
	 * 
	 * @param handler
	 *            the handler to use
	 * 
	 * @return the handled response return
	 * 
	 * @throws ClientProtocolException
	 *             if something fails
	 * 
	 * @throws IOException
	 *             if something fails
	 * 
	 */
    public <T> T handleResponse(final HttpResponseHandler<T> handler) throws ClientProtocolException, IOException {
        assertNotConsumed();
        try {
            return handler.handleResponse(this.response, this.localHttpContext);
        } finally {
            dispose();
        }
    }

    /**
	 * Convert the response to a {@link WebResponse} object. The method will
	 * never return a <code>null</code>.
	 * 
	 * @return the {@link WebResponse} object
	 * 
	 * @throws ClientProtocolException
	 *             if something fails
	 * 
	 * @throws IOException
	 *             if something fails
	 */
    public WebResponse webResponse() throws ClientProtocolException, IOException {
        return handleResponse(new WebResponseHandler());
    }

    /**
	 * Return the {@link HttpResponse} object by reading the entire response
	 * stream as byte-array.
	 * 
	 * @return the {@link HttpResponse} object
	 * 
	 * @throws IOException
	 *             if something fails
	 */
    public HttpResponse httpResponse() throws IOException {
        assertNotConsumed();
        try {
            HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                this.response.setEntity(new ByteArrayEntity(EntityUtils.toByteArray(entity), ContentType.getOrDefault(entity)));
            }
            return this.response;
        } finally {
            this.consumed = true;
        }
    }
    
    /**
	 * Write the response stream to the given file. If the HTTP status code is
	 * greater than or equal to HTTP 300, an {@link HttpResponseException} is
	 * thrown.
	 * 
	 * @param file
	 *            the file to write the response to.
	 * 
	 * @throws IOException
	 *             if something fails during HTTP connection
	 * 
	 * @throws HttpResponseException
	 *             if the HTTP status code is greater than or equal to HTTP 300
	 * 
	 * @throws NullPointerException
	 *             if the file to which the response needs to be written is
	 *             <code>null</code>
	 */
    public void writeToFile(final File file) throws IOException {
        assertNotConsumed();
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(file));
        try {
            HttpEntity entity = this.response.getEntity();
            if (entity != null) {
                entity.writeTo(bout);
            }
        } finally {
            this.consumed = true;
            bout.close();
        }
    }
}