package com.sangupta.jerry.http.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.http.mock.MockHttpServiceImpl;
import com.sangupta.jerry.http.mock.MockWebResponse;

/**
 * Unit tests for {@link HttpHelper}.
 * 
 * @author sangupta
 *
 */
class TestHttpHelepr {

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
	}
	
	@Test 
	public void testDownloadToFile() throws IOException {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		File file = File.createTempFile("test-jerry-http-", ".dat");
		
		service.setNextResponse(new MockWebResponse("helllo world"));
		HttpHelper.downloadToFile("http://localhost:8080/url", file, service);
		
		Assert.assertNotNull(file);
		Assert.assertEquals("helllo world", FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
	}
}
