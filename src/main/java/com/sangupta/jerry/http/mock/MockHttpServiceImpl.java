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

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.helper.HttpHelper;
import com.sangupta.jerry.http.service.HttpService;

/**
 * A mock {@link HttpService} implementation that allows us to send
 * pre-configured {@link WebResponse} objects as part of various calls
 * to {@link HttpService}.
 * 
 * @author sangupta
 *
 */
public class MockHttpServiceImpl implements HttpService {
	
	private static final WebResponse SENTINEL_RESPONSE = new WebResponse(null);
	
	protected final ThreadLocal<AtomicReference<WebResponse>> nextReturnValue = new ThreadLocal<>();
	
	protected final AtomicReference<WebResponse> anyThreadReturnValue = new AtomicReference<>();
	
	/**
	 * Public constructor
	 */
	public MockHttpServiceImpl() {
		AtomicReference<WebResponse> reference = new AtomicReference<WebResponse>();
		reference.set(SENTINEL_RESPONSE);
		this.nextReturnValue.set(reference);
		
		this.anyThreadReturnValue.set(SENTINEL_RESPONSE);
	}
	
	// METHODS FOR MOCKING
	
	protected WebResponse getThreadLocalResponse() {
		AtomicReference<WebResponse> reference = this.nextReturnValue.get();
		return reference.getAndSet(SENTINEL_RESPONSE);
	}
	
	protected WebResponse getAnyThreadResponse() {
		return this.anyThreadReturnValue.getAndSet(SENTINEL_RESPONSE);
	}
	
	protected WebResponse getResponse() {
		WebResponse response = this.getThreadLocalResponse();
		if(SENTINEL_RESPONSE != response) { // yes, this is instance comparison
			return response;
		}
		
		response = this.getAnyThreadResponse();
		if(SENTINEL_RESPONSE != response) {
			return response;
		}
		
		return null;
	}
	
	public void setNextResponse(WebResponse response) {
		AtomicReference<WebResponse> reference = this.nextReturnValue.get();
		reference.set(response);
	}
	
	public void setNextResponseAnyThread(WebResponse response) {
		this.anyThreadReturnValue.set(response);
	}
	
	// METHOD FROM HTTPSERVICE

	@Override
	public String getTextResponse(String url) {
		WebResponse response = this.getResponse();
		if(response == null) {
			return null;
		}
		
		return response.getContent();
	}

	@Override
	public WebResponse getResponse(String url) {
		return this.getResponse();
	}

	@Override
	public Map<String, String> getResponseHeaders(String url) {
		WebResponse response = this.getResponse();
		if(response == null) {
			return null;
		}
		
		return response.getHeaders();
	}

	@Override
	public WebResponse doHEAD(String url) {
		return this.getResponse();
	}

	@Override
	public WebResponse doGET(String url) {
		return this.getResponse();
	}

	@Override
	public WebResponse doPOST(String url, String requestBody, String mimeType) {
		return this.getResponse();
	}

	@Override
	public WebResponse doPUT(String url, String requestBody, String mimeType) {
		return this.getResponse();
	}
	
	@Override
	public WebResponse doPATCH(String url, String requestBody, String mimeType) {
		return this.getResponse();
	}

	@Override
	public WebResponse doDELETE(String url) {
		return this.getResponse();
	}

	@Override
	public WebResponse doOPTIONS(String url) {
		return this.getResponse();
	}

	@Override
	public WebResponse doTRACE(String url) {
		return this.getResponse();
	}

	@Override
	public WebRequest getWebRequest(WebRequestMethod method, String url) {
		return HttpHelper.getWebRequest(method, url);
	}

	@Override
	public WebResponse postXML(String uri, Object object) {
		return this.getResponse();
	}

	@Override
	public WebResponse postJSON(String uri, Object object) {
		return this.getResponse();
	}

	@Override
	public WebResponse executeSilently(WebRequest request) {
		return this.getResponse();
	}

	@Override
	public WebResponse plainExecuteSilently(WebRequest request) {
		return this.getResponse();
	}

	@Override
	public void setConnectionTimeout(int millis) {
		// ignore - nothing to do
	}

	@Override
	public void setSocketTimeout(int millis) {
		// ignore - nothing to do
	}

	@Override
	public File downloadToTempFile(String url) throws IOException {
		return HttpHelper.downloadToTempFile(url, this);
	}

	@Override
	public boolean downloadToFile(String url, File fileToDownloadIn) throws IOException {
		return HttpHelper.downloadToFile(url, fileToDownloadIn, this);
	}

}
