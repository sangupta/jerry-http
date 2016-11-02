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

package com.sangupta.jerry.http;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;

/**
 * An already-handled version of {@link WebRawResponse} that uses a
 * {@link WebResponse} object to simulate the contract of {@link WebRawResponse}.
 * 
 * @author sangupta
 *
 */
public class HandledWebRawResponse extends WebRawResponse {
	
	/**
	 * The {@link WebResponse} object to use
	 */
	private final WebResponse webResponse;

	/**
	 * Constructor
	 * 
	 * @param response the {@link WebResponse} that will be returned
	 */
	HandledWebRawResponse(WebResponse response) {
		super(null, null, null);
		this.webResponse = response;
	}
	
	@Override
	public WebResponse webResponse() throws ClientProtocolException, IOException {
		return this.webResponse;
	}
	
	@Override
	public WebResponse webResponse(HttpResponseHandler handler) throws ClientProtocolException, IOException {
		throw new IllegalStateException("RawResponse has already been handled.");
	}

	@Override
	public void writeToFile(File file) throws IOException {
        byte[] bytes = webResponse.getBytes();
        if(bytes == null) {
        	bytes = new byte[0];
        }
        
        FileUtils.writeByteArrayToFile(file, bytes);
	}
	
}
