/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2016, Sandeep Gupta
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

package com.sangupta.jerry.http.mock;

import java.nio.charset.Charset;
import java.util.Arrays;

import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.service.HttpService;

/**
 * A mocked {@link WebResponse} object that allows us to easily unit test
 * pieces written using the {@link HttpService} class.
 * 
 * @author sangupta
 *
 */
public class MockWebResponse extends WebResponse {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = 2283766764093044839L;
	
	/**
	 * Last modified time, if specified
	 */
	protected Long lastModified = null;

	/**
	 * Constructor
	 * 
	 * @param responseBody the {@link String} response body to use
	 */
	public MockWebResponse(String responseBody) {
		super(null, responseBody);
	}
	
	@Override
	public long getLastModified() {
		if(this.lastModified == null) {
			return super.getLastModified();
		}
		
		return this.lastModified.longValue();
	}

	/**
	 * Set the response code to return
	 * 
	 * @param responseCode
	 *            the response code to use
	 * 
	 * @return this very {@link MockWebResponse} instance
	 */
	public MockWebResponse setResponseCode(int responseCode) {
		this.responseCode = responseCode;
		return this;
	}
	
	/**
	 * Set the {@link Charset} to return
	 * 
	 * @param charSet
	 *            the {@link Charset} to use
	 * 
	 * @return this very {@link MockWebResponse} instance
	 */
	public MockWebResponse setCharset(Charset charSet) {
		this.charSet = charSet;
		return this;
	}
	
	public MockWebResponse setLastModified(long millis) {
		this.lastModified = Long.valueOf(millis);
		return this;
	}
	
	/**
	 * Set the content type to return
	 * 
	 * @param contentType
	 *            the content type to use
	 * 
	 * @return this very {@link MockWebResponse} instance
	 */
	public MockWebResponse setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Add a new response header. Any previous header with the same value will
	 * be over-written.
	 * 
	 * @param name
	 *            the header name
	 * 
	 * @param value
	 *            the header value
	 * 
	 * @return this very {@link MockWebResponse} instance
	 */
	public MockWebResponse addHeader(String name, String value) {
		this.headers.put(name, value);
		return this;
	}
	
	/**
	 * Set the response message received from server.
	 * 
	 * @param message
	 *            the message to use
	 * 
	 * @return this very {@link MockWebResponse} instance
	 */
	public MockWebResponse setMessage(String message) {
		this.message = message;
		return this;
	}
	
	@Override
	public int hashCode() {
		if(this.bytes == null) {
			return -1;
		}
		
		return this.bytes.length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof WebResponse)) {
			return false;
		}
		
		WebResponse wr = (WebResponse) obj;
		
		if(this.bytes == null) {
			if(wr.getBytes() == null) {
				return true;
			}
			
			return false;
		}
		
		return Arrays.equals(bytes, wr.getBytes());
	}
}
