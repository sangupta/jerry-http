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

	public static WebRequest getWebRequest(WebRequestMethod method, String uri) {
		if(method == null) {
			throw new IllegalArgumentException("WebRequestMethod cannot be null");
		}
		
		WebRequest request = null;
		switch(method) {
			case DELETE:
				request = WebRequest.delete(uri);
				break;
				
			case GET:
				request = WebRequest.get(uri);
				break;
				
			case HEAD:
				request = WebRequest.head(uri);
				break;
				
			case OPTIONS:
				request = WebRequest.options(uri);
				break;
				
			case POST:
				request = WebRequest.post(uri);
				break;
				
			case PUT:
				request = WebRequest.put(uri);
				break;
				
			case TRACE:
				request = WebRequest.trace(uri);
				break;
				
			default:
				throw new IllegalStateException("All options of enumeration have a check above, reaching this is impossible. This is a coding horror.");
		}
		
		return request;
	}
	
}
