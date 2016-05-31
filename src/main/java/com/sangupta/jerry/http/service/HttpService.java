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
	
	public String getTextResponse(String url);
	
	public WebResponse getWebResponse(String url);
	
	public Map<String, String> getResponseHeaders(String url);

	public WebResponse doHEAD(String url);
	
	public WebResponse doGET(String url);
	
	public WebResponse doPOST(String url, String requestBody, String mimeType);
	
	public WebResponse doPUT(String url, String requestBody, String mimeType);
	
	public WebResponse doDELETE(String url);
	
	public WebResponse doOPTIONS(String url);
	
	public WebResponse doTRACE(String url);

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
