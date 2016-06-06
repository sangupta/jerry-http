package com.sangupta.jerry.http;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.http.mock.MockWebResponse;

/**
 * Unit tests for {@link HandledWebRawResponse}.
 * 
 * @author sangupta
 *
 */
public class TestHandledWebRawResponse {

	@Test
	public void test() throws ClientProtocolException, IOException {
		HandledWebRawResponse hwrr = new HandledWebRawResponse(new MockWebResponse("hello"));
		try {
			hwrr.webResponse(null);
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
		
		File file = File.createTempFile("test-jerry-http-", ".dat");
		hwrr.writeToFile(file);
		Assert.assertEquals("hello", FileUtils.readFileToString(file));
		
		hwrr = new HandledWebRawResponse(new MockWebResponse(null));
		hwrr.writeToFile(file);
		Assert.assertEquals("", FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
	}
	
}
