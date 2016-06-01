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

package com.sangupta.jerry.http.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.helper.HttpHelper;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.util.DateUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.jerry.util.XStreamUtils;

/**
 * Default implementation of {@link HttpService} that uses Apache {@link HttpClient}
 * to make HTTP calls.
 * 
 * @author sangupta
 * 
 * @since 2.0.0
 *
 */
public class DefaultHttpServiceImpl implements HttpService {
	
	/**
	 * My logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpServiceImpl.class);
	
	/**
	 * Value to be used for connection timeout
	 */
	private volatile int connectionTimeout = (int) DateUtils.ONE_MINUTE;
	
	/**
	 * Value to be used for socket timeout
	 */
	private volatile int socketTimeout = (int) DateUtils.ONE_MINUTE;
	
	/**
	 * The cookie policy to use
	 */
	private volatile String cookiePolicy = CookieSpecs.DEFAULT;
	
	/**
	 * Whether to follow redirects or not
	 */
	private volatile boolean followRedirects = true;
	
	@Override
	public String getTextResponse(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.GET, url).execute().webResponse().getContent();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse getResponse(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.GET, url).execute().webResponse();
		} catch(Exception e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public Map<String, String> getResponseHeaders(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.HEAD, url).execute().webResponse().getHeaders();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doHEAD(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.HEAD, url).execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doGET(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.GET, url).execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doPOST(String url, String requestBody, String mimeType) {
		WebRequest request = this.getWebRequest(WebRequestMethod.POST, url);
		request.bodyString(requestBody, ContentType.create(mimeType));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doPUT(String url, String requestBody, String mimeType) {
		WebRequest request = this.getWebRequest(WebRequestMethod.PUT, url);
		request.bodyString(requestBody, ContentType.create(mimeType));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doDELETE(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.DELETE, url).execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doOPTIONS(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.OPTIONS, url).execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse doTRACE(String url) {
		try {
			return this.getWebRequest(WebRequestMethod.TRACE, url).execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch response headers from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse postXML(String url, Object object) {
		WebRequest request = this.getWebRequest(WebRequestMethod.POST, url);
		
		String requestBody = XStreamUtils.getXStream(object.getClass()).toXML(object);
		request.bodyString(requestBody, ContentType.create(HttpMimeType.XML));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse postJSON(String url, Object object) {
		WebRequest request = this.getWebRequest(WebRequestMethod.POST, url);
		
		String requestBody = GsonUtils.getGson().toJson(object);
		request.bodyString(requestBody, ContentType.create(HttpMimeType.JSON));
		
		try {
			return request.execute().webResponse();
		} catch(IOException e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", url, e);
		}
		
		return null;
	}

	@Override
	public WebResponse executeSilently(WebRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Webrequest to be executed cannot be null");
		}
		
		request.connectTimeout(connectionTimeout).socketTimeout(socketTimeout).cookiePolicy(cookiePolicy);
		if(this.followRedirects) {
			request.followRedirects();
		}
		
		return this.executeSilently(request);
	}
	
	@Override
	public WebResponse plainExecuteSilently(WebRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Webrequest to be executed cannot be null");
		}
		
		try {
			return request.execute().webResponse();
		} catch(Exception e) {
			LOGGER.debug("Unable to fetch repsonse from url: {}", request.getURI().toString(), e);	
		}
		
		return null;
	}
	
	@Override
	public WebRequest getWebRequest(WebRequestMethod method, String uri) {
		WebRequest request = HttpHelper.getWebRequest(method, uri);
		if(request == null) {
			return null;
		}
		
		request.connectTimeout(connectionTimeout).socketTimeout(socketTimeout).cookiePolicy(cookiePolicy);
		if(this.followRedirects) {
			request.followRedirects();
		}
		
		return request;
	}

	@Override
	public void setConnectionTimeout(int millis) {
		this.connectionTimeout = millis;
	}
	
	@Override
	public void setSocketTimeout(int millis) {
		this.socketTimeout = millis;
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
