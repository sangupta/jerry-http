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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link WebRawResponse}.
 * 
 * @author sangupta
 *
 */
@SuppressWarnings("deprecation")
public class TestWebRawResponse {

	@Test
	public void test() throws ClientProtocolException, IOException {
		WebRawResponse response = new WebRawResponse(null, null, null);
		
		Assert.assertFalse(response.isConsumed());
		response.handleResponse(new HttpResponseHandler() {
			
			@Override
			public WebResponse handleResponse(URI originalURI, HttpResponse response, HttpContext httpContext) throws ClientProtocolException, IOException {
				return null;
			}
		});
		Assert.assertTrue(response.isConsumed());
		
		// discard
		response = new WebRawResponse(null, null, null);
		Assert.assertFalse(response.isConsumed());
		response.discardContent();
		Assert.assertTrue(response.isConsumed());
		response.discardContent();
		Assert.assertTrue(response.isConsumed());
		
		// webresponse
		response = new WebRawResponse(null, null, null);
		
		Assert.assertFalse(response.isConsumed());
		WebResponse webResponse = response.webResponse(new HttpResponseHandler() {
			
			@Override
			public WebResponse handleResponse(URI originalURI, HttpResponse response, HttpContext httpContext) throws ClientProtocolException, IOException {
				return null;
			}
		});
		Assert.assertNull(webResponse);
		Assert.assertTrue(response.isConsumed());
	}
	
	@Test
	public void testWriteToFile() throws IOException {
		File file = File.createTempFile("test-jerry-http-", ".dat");
		
		// empty
		MyResponse hr = new MyResponse();
		hr.responseCode = 200;
		WebRawResponse response = new WebRawResponse(null, hr, null);
		response.writeToFile(file);
		Assert.assertEquals("", FileUtils.readFileToString(file));
		FileUtils.deleteQuietly(file);
		
		// some data
		hr.responseCode = 200;
		hr.entity = new StringEntity("hello");
		response = new WebRawResponse(null, hr, null);
		response.writeToFile(file);
		Assert.assertEquals("hello", FileUtils.readFileToString(file));
		FileUtils.deleteQuietly(file);
		
		// error
		try {
			hr.responseCode = 301;
			response = new WebRawResponse(null, hr, null);
			response.writeToFile(file);
			Assert.assertTrue(false);
		} catch(HttpResponseException e) {
			Assert.assertTrue(true);
		}
		
		// write again
		hr.responseCode = 200;
		hr.entity = new StringEntity("hello");
		response = new WebRawResponse(null, hr, null);
		response.writeToFile(file);
		Assert.assertEquals("hello", FileUtils.readFileToString(file));
		FileUtils.deleteQuietly(file);
		
		try {
			response.writeToFile(file);
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
	}

	private class MyResponse implements HttpResponse {
		
		private int responseCode = -1;
		
		private HttpEntity entity;

		@Override
		public ProtocolVersion getProtocolVersion() {
			return null;
		}

		@Override
		public boolean containsHeader(String name) {
			return false;
		}

		@Override
		public Header[] getHeaders(String name) {
			return null;
		}

		@Override
		public Header getFirstHeader(String name) {
			return null;
		}

		@Override
		public Header getLastHeader(String name) {
			return null;
		}

		@Override
		public Header[] getAllHeaders() {
			return null;
		}

		@Override
		public void addHeader(Header header) {
			
		}

		@Override
		public void addHeader(String name, String value) {
			
		}

		@Override
		public void setHeader(Header header) {
			
		}

		@Override
		public void setHeader(String name, String value) {
			
		}

		@Override
		public void setHeaders(Header[] headers) {
			
		}

		@Override
		public void removeHeader(Header header) {
			
		}

		@Override
		public void removeHeaders(String name) {
			
		}

		@Override
		public HeaderIterator headerIterator() {
			return null;
		}

		@Override
		public HeaderIterator headerIterator(String name) {
			return null;
		}

		@Override
		public HttpParams getParams() {
			return null;
		}

		@Override
		public void setParams(HttpParams params) {
			
		}

		@Override
		public StatusLine getStatusLine() {
			return new StatusLine() {
				
				@Override
				public int getStatusCode() {
					return responseCode;
				}
				
				@Override
				public String getReasonPhrase() {
					return null;
				}
				
				@Override
				public ProtocolVersion getProtocolVersion() {
					return null;
				}
			};
		}

		@Override
		public void setStatusLine(StatusLine statusline) {
			
		}

		@Override
		public void setStatusLine(ProtocolVersion ver, int code) {
			
		}

		@Override
		public void setStatusLine(ProtocolVersion ver, int code, String reason) {
			
		}

		@Override
		public void setStatusCode(int code) throws IllegalStateException {
			
		}

		@Override
		public void setReasonPhrase(String reason) throws IllegalStateException {
			
		}

		@Override
		public HttpEntity getEntity() {
			return this.entity;
		}

		@Override
		public void setEntity(HttpEntity entity) {
			this.entity = entity;
		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public void setLocale(Locale loc) {
			
		}
		
	}
}
