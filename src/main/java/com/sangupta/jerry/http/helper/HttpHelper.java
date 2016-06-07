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

package com.sangupta.jerry.http.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;

/**
 * Helper functions that can be used across
 * 
 * @author sangupta
 *
 */
public abstract class HttpHelper {
	
	/**
	 * My logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);
	
	public static void writeToFile(WebResponse response, File file) throws IOException {
		if (response.getResponseCode() >= 300) {
            throw new HttpResponseException(response.getResponseCode(), response.getMessage());
        }

        FileUtils.writeByteArrayToFile(file, response.getBytes());
	}
	
	public static File downloadToTempFile(String url, HttpService service) throws IOException {
		String extension = UriUtils.extractExtension(url);
		File tempFile = File.createTempFile("download", extension);
		tempFile.deleteOnExit();
		
		LOGGER.debug("Downloading {} to {}", url, tempFile.getAbsolutePath());
		
		try {
			WebResponse response = service.doGET(url);
			if(response == null) {
				return null;
			}
			
			response.writeToFile(tempFile);
			return tempFile;
		} catch(HttpResponseException e) {
			LOGGER.error("HTTP response did not yield an OK status", e);
		} catch(IOException e) {
			LOGGER.error("Unable to download url to temp file", e);
		}
		
		return null;
	}
	
	public static boolean downloadToFile(String url, File fileToDownloadIn, HttpService service) throws IOException {
		LOGGER.debug("Downloading {} to {}", url, fileToDownloadIn.getAbsolutePath());
		
		try {
			WebResponse response = service.doGET(url);
			if(response == null) {
				return false;
			}
			
			response.writeToFile(fileToDownloadIn);
			return true;
		} catch(HttpResponseException e) {
			LOGGER.error("HTTP response did not yield an OK status", e);
		} catch(IOException e) {
			LOGGER.error("Unable to download url to temp file", e);
		}
		
		return false;
	}

	/**
	 * Create a {@link WebRequest} object for the given {@link WebRequestMethod}
	 * and the given url. This method will never return a <code>null</code>.
	 * 
	 * @param method
	 *            the HTTP VERB to use
	 * 
	 * @param url
	 *            the URL for which to create the request
	 * 
	 * @return the constructed {@link WebRequest} object
	 * 
	 * @throws IllegalArgumentException
	 *             if {@link WebRequestMethod} or url is <code>null</code>.
	 */
	public static WebRequest getWebRequest(WebRequestMethod method, String url) {
		if(method == null) {
			throw new IllegalArgumentException("WebRequestMethod cannot be null");
		}
		
		if(AssertUtils.isEmpty(url)) {
			throw new IllegalArgumentException("URL cannot be null/empty");
		}
		
		WebRequest request = null;
		switch(method) {
			case DELETE:
				request = WebRequest.delete(url);
				break;
				
			case GET:
				request = WebRequest.get(url);
				break;
				
			case HEAD:
				request = WebRequest.head(url);
				break;
				
			case OPTIONS:
				request = WebRequest.options(url);
				break;
				
			case POST:
				request = WebRequest.post(url);
				break;
				
			case PUT:
				request = WebRequest.put(url);
				break;
				
			case TRACE:
				request = WebRequest.trace(url);
				break;
				
			default:
				throw new IllegalStateException("All options of enumeration have a check above, reaching this is impossible. This is a coding horror.");
		}
		
		return request;
	}
	
}
