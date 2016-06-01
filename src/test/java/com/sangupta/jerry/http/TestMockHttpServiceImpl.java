package com.sangupta.jerry.http;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.http.mock.MockHttpServiceImpl;
import com.sangupta.jerry.http.mock.MockWebResponse;
import com.sangupta.jerry.util.ByteArrayUtils;
import com.sangupta.jerry.util.HashUtils;

/**
 * This class tests the behaviour of various methods of the
 * {@link MockHttpServiceImpl}.
 * 
 * @author sangupta
 *
 */
public class TestMockHttpServiceImpl {
	
	private static final String SOME_TEST_URL = "http://localhost/someUrl";

	private static final int MAX_LENGTH = 1024;
	
	private static final String RANDOM_STRING = HashUtils.getMD5Hex(ByteArrayUtils.getRandomBytes(MAX_LENGTH));
	
	private final MockHttpServiceImpl service = new MockHttpServiceImpl();
	
	@Test
	public void testGetTextResponse() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		service.setNextResponse(response);
		String body = service.getTextResponse(SOME_TEST_URL);
		
		Assert.assertNotNull(body);
		Assert.assertEquals(RANDOM_STRING, body);
	}
	
	@Test
	public void testGetResponse() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.getResponse(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}

	@Test
	public void testGetResponseHeaders() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		Map<String, String> result = service.getResponseHeaders(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		
		Assert.assertEquals(RANDOM_STRING, result.get(RANDOM_STRING));
	}
	
	@Test
	public void testDoHEAD() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doHEAD(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoGET() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doGET(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoPOST() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doPOST(SOME_TEST_URL, RANDOM_STRING, HttpMimeType.BINARY);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoPUT() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doPUT(SOME_TEST_URL, RANDOM_STRING, HttpMimeType.BINARY);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoOPTIONS() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doOPTIONS(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoTRACE() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doTRACE(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testDoDELETE() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.doDELETE(SOME_TEST_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testPostXML() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.postXML(SOME_TEST_URL, RANDOM_STRING);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testPostJSONL() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.postJSON(SOME_TEST_URL, RANDOM_STRING);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testExecuteSilently() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.executeSilently(service.getWebRequest(WebRequestMethod.GET, SOME_TEST_URL));
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
	@Test
	public void testPlainExecuteSilently() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		response.addHeader(RANDOM_STRING, RANDOM_STRING);
		service.setNextResponse(response);
		WebResponse result = service.plainExecuteSilently(service.getWebRequest(WebRequestMethod.GET, SOME_TEST_URL));
		
		Assert.assertNotNull(result);
		Assert.assertEquals(response, result);
	}
	
}
