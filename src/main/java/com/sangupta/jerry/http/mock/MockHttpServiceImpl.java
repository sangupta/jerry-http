package com.sangupta.jerry.http.mock;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.util.UriUtils;

/**
 * A mock {@link HttpService} implementation that allows us to send
 * pre-configured {@link WebResponse} objects as part of various calls
 * to {@link HttpService}.
 * 
 * @author sangupta
 *
 */
public class MockHttpServiceImpl implements HttpService {
	
	/**
	 * My logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MockHttpServiceImpl.class);
	
	private static final WebResponse SENTINEL_RESPONSE = new WebResponse(null);
	
	protected final ThreadLocal<AtomicReference<WebResponse>> nextReturnValue = new ThreadLocal<>();
	
	protected final AtomicReference<WebResponse> anyThreadReturnValue = new AtomicReference<>();
	
	private MockHttpServiceImpl() {
		this.nextReturnValue.set(new AtomicReference<WebResponse>());
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
	public WebResponse getWebResponse(String url) {
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
		if(method == null) {
			throw new IllegalArgumentException("WebRequestMethod cannot be null");
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
		String extension = UriUtils.extractExtension(url);
		File tempFile = File.createTempFile("download", extension);
		tempFile.deleteOnExit();
		
		LOGGER.debug("Downloading {} to {}", url, tempFile.getAbsolutePath());
		
		try {
			WebResponse response = this.getResponse();
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

	@Override
	public boolean downloadToFile(String url, File fileToDownloadIn) throws IOException {
		try {
			WebResponse response = this.getResponse();
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

}
