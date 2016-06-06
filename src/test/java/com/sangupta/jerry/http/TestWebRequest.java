package com.sangupta.jerry.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpMimeType;

/**
 * Unit tests for {@link WebRequest}.
 * 
 * @author sangupta
 *
 */
public class TestWebRequest {

	@Test
	public void testRequest() throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/hit");
		
		Assert.assertEquals("HEAD", WebRequest.head(uri).getVerb());
		Assert.assertEquals("GET", WebRequest.get(uri).getVerb());
		Assert.assertEquals("POST", WebRequest.post(uri).getVerb());
		Assert.assertEquals("PUT", WebRequest.put(uri).getVerb());
		Assert.assertEquals("OPTIONS", WebRequest.options(uri).getVerb());
		Assert.assertEquals("TRACE", WebRequest.trace(uri).getVerb());
		Assert.assertEquals("DELETE", WebRequest.delete(uri).getVerb());
		
		Assert.assertEquals(uri, WebRequest.get(uri).getURI());
		
		Assert.assertEquals(WebRequestMethod.HEAD, WebRequest.head(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.GET, WebRequest.get(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.POST, WebRequest.post(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.PUT, WebRequest.put(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.OPTIONS, WebRequest.options(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.TRACE, WebRequest.trace(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.DELETE, WebRequest.delete(uri).getWebRequestMethod());
	}
	
	@Test
	public void testTrace() {
		WebRequest request = WebRequest.post("http://localhost:8080/hit");
		request.addHeader("test", "value");
		request.addHeader(new BasicHeader("test2", "value2"));
		request.bodyString("hello", HttpMimeType.TEXT_PLAIN);
		Assert.assertNotNull(request.trace());
	}
	
}
