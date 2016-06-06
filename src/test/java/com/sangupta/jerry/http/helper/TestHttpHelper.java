package com.sangupta.jerry.http.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpStatusCode;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.mock.MockHttpServiceImpl;
import com.sangupta.jerry.http.mock.MockWebResponse;
import com.sangupta.jerry.unsafe.UnsafeMemory;

/**
 * Unit tests for {@link HttpHelper}.
 * 
 * @author sangupta
 *
 */
public class TestHttpHelper {
	
	@Test
	public void testGetWebRequest() {
		try {
			HttpHelper.getWebRequest(null, "http://localhost");
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testWriteToFile() throws IOException {
		File file = File.createTempFile("test-jerry-http-", ".dat");
		
		MockWebResponse response = new MockWebResponse("hello");
		response.setResponseCode(304);
		try {
			HttpHelper.writeToFile(response, file);
			Assert.assertTrue(false);
		} catch(HttpResponseException e) {
			Assert.assertTrue(true);
		}
		
		response.setResponseCode(200);
		HttpHelper.writeToFile(response, file);
		
		Assert.assertEquals("hello", FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
	}
	
	@Test
	public void testDownloadToTempFile() throws IOException {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		service.setNextResponse(new MockWebResponse("helllo world"));
		File file = HttpHelper.downloadToTempFile("http://localhost:8080/url", service);
		
		Assert.assertNotNull(file);
		Assert.assertEquals("helllo world", FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
		
		// null response case
		service.setNextResponse(null);
		Assert.assertNull(HttpHelper.downloadToTempFile("http://localhost:8080/url", service));
		
		// exception
		service = new MockHttpServiceImpl() {
			
			@SuppressWarnings("restriction")
			public WebResponse doGET(String url) {
				UnsafeMemory.getUnsafe().throwException(new HttpResponseException(HttpStatusCode.REQUEST_TIMEOUT, "test-timeout"));
				return null;
			}
			
		};
		Assert.assertNull(HttpHelper.downloadToTempFile("http://localhost:8080/url", service));

		// exception
		service = new MockHttpServiceImpl() {
			
			@SuppressWarnings("restriction")
			public WebResponse doGET(String url) {
				UnsafeMemory.getUnsafe().throwException(new IOException());
				return null;
			}
			
		};
		Assert.assertNull(HttpHelper.downloadToTempFile("http://localhost:8080/url", service));
	}
	
	@Test 
	public void testDownloadToFile() throws IOException {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		File file = File.createTempFile("test-jerry-http-", ".dat");
		
		service.setNextResponse(new MockWebResponse("helllo world"));
		Assert.assertTrue(HttpHelper.downloadToFile("http://localhost:8080/url", file, service));
		
		Assert.assertNotNull(file);
		Assert.assertEquals("helllo world", FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
		
		// null response case
		service.setNextResponse(null);
		Assert.assertFalse(HttpHelper.downloadToFile("http://localhost:8080/url", file, service));
		
		// exception
		service = new MockHttpServiceImpl() {
			
			@SuppressWarnings("restriction")
			public WebResponse doGET(String url) {
				UnsafeMemory.getUnsafe().throwException(new HttpResponseException(HttpStatusCode.REQUEST_TIMEOUT, "test-timeout"));
				return null;
			}
			
		};
		Assert.assertFalse(HttpHelper.downloadToFile("http://localhost:8080/url", file, service));

		// exception
		service = new MockHttpServiceImpl() {
			
			@SuppressWarnings("restriction")
			public WebResponse doGET(String url) {
				UnsafeMemory.getUnsafe().throwException(new IOException());
				return null;
			}
			
		};
		Assert.assertFalse(HttpHelper.downloadToFile("http://localhost:8080/url", file, service));
	}
}
