package com.sangupta.jerry.http;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.http.mock.MockHttpServiceImpl;
import com.sangupta.jerry.http.mock.MockWebResponse;
import com.sangupta.jerry.util.StringUtils;

/**
 * This class tests the behaviour of various methods of the
 * {@link MockHttpServiceImpl}.
 * 
 * @author sangupta
 *
 */
public class TestMockHttpServiceImpl {
	
	private static final int MAX_LENGTH = 1024;
	
	@Test
	public void testGetTextResponse() {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		String originalBody = StringUtils.getRandomString(MAX_LENGTH);
		MockWebResponse response = new MockWebResponse(originalBody);
		service.setNextResponse(response);
		String body = service.getTextResponse("http://localhost/someUrl");
		
		Assert.assertNotNull(body);
		Assert.assertEquals(originalBody, body);
	}
	
	@Test
	public void testGetResponse() {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		String originalBody = StringUtils.getRandomString(MAX_LENGTH);
		MockWebResponse response = new MockWebResponse(originalBody);
		service.setNextResponse(response);
		WebResponse result = service.getResponse("http://localhost/someUrl");
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}

	@Test
	public void testGetResponseHeaders() {
		MockHttpServiceImpl service = new MockHttpServiceImpl();
		String originalBody = StringUtils.getRandomString(MAX_LENGTH);
		MockWebResponse response = new MockWebResponse(originalBody);
		response.addHeader(originalBody, originalBody);
		service.setNextResponse(response);
		WebResponse result = service.getResponse("http://localhost/someUrl");
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
		Assert.assertNotNull(response.getHeaders());
		Assert.assertEquals(1, response.getHeaders().size());
		
		Assert.assertEquals(originalBody, response.getHeaders().get(originalBody));
	}
}
