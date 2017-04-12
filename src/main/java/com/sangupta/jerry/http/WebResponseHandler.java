/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2017, Sandeep Gupta
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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.sangupta.jerry.util.AssertUtils;

/**
 * {@link ResponseHandler} class that handles the extract the {@link WebResponse} object
 * from the provided {@link HttpResponse} object.
 * 
 * @author sangupta
 * 
 * @since 0.3
 */
public class WebResponseHandler implements HttpResponseHandler {

	/**
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	@Override
	public WebResponse handleResponse(URI originalURI, HttpResponse response, HttpContext localHttpContext) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        
        byte[] bytes = null;
        if(entity != null) {
        	bytes = EntityUtils.toByteArray(entity);
        }
		final WebResponse webResponse = new WebResponse(originalURI, bytes);
		
		// decipher from status line
		webResponse.responseCode = statusLine.getStatusCode();
		webResponse.message = statusLine.getReasonPhrase();
		
		// set size
		if(entity != null) {
			webResponse.size = entity.getContentLength();
        } else {
    		long value = 0;
        	Header header = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
        	if(header != null) {
        		String headerValue = header.getValue();
            	if(AssertUtils.isNotEmpty(headerValue)) {
            		try {
            			value = Long.parseLong(headerValue);
            		} catch(Exception e) {
            			// eat the exception
            		}
            	}
        	}
        	
        	webResponse.size = value;
        }
		
		// content type
		if(entity != null && entity.getContentType() != null) {
        	webResponse.contentType = entity.getContentType().getValue();
        }
		
		// response headers
		final Header[] responseHeaders = response.getAllHeaders();
        if(AssertUtils.isNotEmpty(responseHeaders)) {
        	for(Header header : responseHeaders) {
        		webResponse.headers.put(header.getName(), header.getValue());
        	}
        }
		
		// charset
        try {
			ContentType type = ContentType.get(entity);
			if(type != null) {
				webResponse.charSet = type.getCharset();
			}
        } catch(UnsupportedCharsetException e) {
        	// we are unable to find the charset for the content
        	// let's leave it to be considered binary
        }
        
        // fill in the redirect uri chain
        RedirectLocations locations = (RedirectLocations) localHttpContext.getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
        if(AssertUtils.isNotEmpty(locations)) {
        	webResponse.setRedirectChain(locations.getAll());
        }
        
		// return the object finally
        return webResponse;
	}

}
