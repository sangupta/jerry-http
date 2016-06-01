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

package com.sangupta.jerry.http.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.thoughtworks.xstream.XStream;

/**
 * An abstraction that can be used by clients to make HTTP calls. This contract
 * helps align with the dependency injection model and makes it easier to test
 * code that depends on external web-service or HTTP calls.
 * 
 * @author sangupta
 * 
 * @since 2.0.0
 *
 */
public interface HttpService {
	
	/**
	 * Return the HTTP response body for a GET request to the given URL. In case
	 * an {@link IOException} is thrown, it will be eaten up, logged at DEBUG
	 * level, and <code>null</code> returned.
	 * 
	 * @param url
	 *            the url to hit
	 *            
	 * @return the string response body
	 */
	public String getTextResponse(String url);
	
	/**
	 * Return the {@link WebResponse} for a GET request to the given URL. In
	 * case an {@link IOException} is thrown, it will be eaten up, logged at
	 * DEBUG level, and <code>null</code> returned.
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse getResponse(String url);
	
	/**
	 * Returns the HTTP headers etc by making a HEAD request to the given URL as
	 * a {@link Map}.
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return a {@link Map} of all header values
	 */
	public Map<String, String> getResponseHeaders(String url);

	/**
	 * Make a HEAD request to the URL and return the web response. 
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doHEAD(String url);
	
	/**
	 * Make a GET request to the URL and return the web response. 
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doGET(String url);
	
	/**
	 * Make a POST request to the URL and return the web response.
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @param requestBody
	 *            the request body to set
	 * 
	 * @param mimeType
	 *            the {@link HttpMimeType} for the request
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doPOST(String url, String requestBody, String mimeType);
	
	/**
	 * Make a PUT request to the URL and return the web response.
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @param requestBody
	 *            the request body to set
	 * 
	 * @param mimeType
	 *            the {@link HttpMimeType} for the request
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doPUT(String url, String requestBody, String mimeType);
	
	/**
	 * Make a DELETE request to the URL and return the web response. 
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doDELETE(String url);
	
	/**
	 * Make a OPTIONS request to the URL and return the web response. 
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doOPTIONS(String url);
	
	/**
	 * Make a TRACE request to the URL and return the web response. 
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse doTRACE(String url);

	/**
	 * Get the {@link WebRequest} object for the given {@link WebRequestMethod}.
	 * 
	 * @param method
	 *            the HTTP VERB to be used
	 * 
	 * @param url
	 *            the url to hit
	 * 
	 * @return the {@link WebRequest} instance for the request
	 */
	public WebRequest getWebRequest(WebRequestMethod method, String url);
	
	/**
	 * POST the XML representation of the given object, to the given URL. The
	 * object is converted to XML format using {@link XStream} project.
	 * 
	 * @param uri
	 *            the url to hit
	 * 
	 * @param object
	 *            the object to be sent in request body
	 * 
	 * @return the {@link WebResponse} obtained
	 * 
	 */
	public WebResponse postXML(final String uri, final Object object);
	
	/**
	 * POST the JSON representation of the given object to the given URL. The
	 * object is converted to JSON format usign {@link Gson} project.
	 * 
	 * @param uri
	 *            the url to hit
	 * 
	 * @param object
	 *            the object to be sent in request body
	 * 
	 * @return the {@link WebResponse} obtained
	 */
	public WebResponse postJSON(final String uri, final Object object);
	
	/**
	 * Execute the given {@link WebRequest} silently massaging the request for
	 * connection/socket timeouts, applying cookie policy and whether to follow
	 * redirects or not.
	 * 
	 * @param request
	 *            the {@link WebRequest} to be executed
	 * 
	 * @return {@link WebResponse} obtained
	 * 
	 * @throws IllegalArgumentException
	 *             if the {@link WebRequest} is <code>null</code>
	 */
	public WebResponse executeSilently(WebRequest request);

	/**
	 * Execute the given {@link WebRequest} silently.
	 * 
	 * @param request
	 *            the {@link WebRequest} to be executed
	 * 
	 * @return {@link WebResponse} obtained
	 * 
	 * @throws IllegalArgumentException
	 *             if the {@link WebRequest} is <code>null</code>
	 */
	public WebResponse plainExecuteSilently(WebRequest request);
	
	/**
	 * Change the default value of the connection timeout.
	 * 
	 * @param millis
	 *            the timeout in millis
	 */
	public void setConnectionTimeout(int millis);
	
	/**
	 * Change the default value of the socket timeout.
	 * 
	 * @param millis
	 *            the timeout in millis
	 */
	public void setSocketTimeout(int millis);
	
	/**
	 * Download the file at the given location URL and store it as a temporary
	 * file on disk. The temporary file is set to be deleted at the exit of the
	 * application.
	 * 
	 * @param url
	 *            absolute URL of the file
	 * 
	 * @return {@link File} handle of the temporary file that was written to
	 *         disk if successful, <code>null</code> otherwise.
	 * 
	 * @throws IOException
	 *             in case something fails
	 */
	public File downloadToTempFile(String url) throws IOException;

	/**
	 * Download the file at the given location URL and store it in the file
	 * mentioned on disk. If the file exists, it will be over-written.
	 * 
	 * @param url
	 *            absolute URL of the file
	 * 
	 * @param fileToDownloadIn
	 *            {@link File} in which contents are written
	 * 
	 * @return <code>true</code> if file was successfully downloaded,
	 *         <code>false</code> otherwise.
	 * 
	 * @throws IOException
	 *             in case something fails
	 */
	public boolean downloadToFile(String url, File fileToDownloadIn) throws IOException;
	
}
