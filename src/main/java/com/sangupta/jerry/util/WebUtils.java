/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2014, Sandeep Gupta
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

package com.sangupta.jerry.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.http.WebRequest;

/**
 * Function to handle file downloads from the web via HTTP. More schemes
 * may be added in future.
 * 
 * @author sangupta
 *
 */
public class WebUtils {
	
	/**
	 * My logger instance
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	
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
	public static File downloadToTempFile(String url) throws IOException {
		String extension = UriUtils.extractExtension(url);
		File tempFile = File.createTempFile("download", extension);
		tempFile.deleteOnExit();
		
		logger.debug("Downloading {} to {}", url, tempFile.getAbsolutePath());
		
		try {
			WebRequest.get(url).execute().writeToFile(tempFile);
			return tempFile;
		} catch(HttpResponseException e) {
			logger.error("HTTP response did not yield an OK status", e);
		} catch(IOException e) {
			logger.error("Unable to download url to temp file", e);
		}
		
		return null;
	}
	
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
	public static boolean downloadToFile(String url, File fileToDownloadIn) throws IOException {
		try {
			WebRequest.get(url).execute().writeToFile(fileToDownloadIn);
			return true;
		} catch(HttpResponseException e) {
			logger.error("HTTP response did not yield an OK status", e);
		} catch(IOException e) {
			logger.error("Unable to download url to temp file", e);
		}
		
		return false;
	}

}
