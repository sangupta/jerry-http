package com.sangupta.jerry.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHeader;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpHeaderName;
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
		Assert.assertEquals("PATCH", WebRequest.patch(uri).getVerb());
		
		Assert.assertEquals(uri, WebRequest.get(uri).getURI());
		
		Assert.assertEquals(WebRequestMethod.HEAD, WebRequest.head(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.GET, WebRequest.get(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.POST, WebRequest.post(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.PUT, WebRequest.put(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.OPTIONS, WebRequest.options(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.TRACE, WebRequest.trace(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.DELETE, WebRequest.delete(uri).getWebRequestMethod());
		Assert.assertEquals(WebRequestMethod.PATCH, WebRequest.patch(uri).getWebRequestMethod());
	}
	
	@Test
	public void testTrace() {
		WebRequest request = WebRequest.post("http://localhost:8080/hit");
		request.addHeader("test", "value");
		request.addHeader(new BasicHeader("test2", "value2"));
		request.bodyString("hello", HttpMimeType.TEXT_PLAIN);
		Assert.assertNotNull(request.trace());
	}
	
	@Test
	public void testHeaders() {
		WebRequest request = WebRequest.post("http://localhost:8080/hit");
		
		Header header = new BasicHeader("name", "value");
		Assert.assertEquals(0, request.getHttpRequest().getAllHeaders().length);
		request.addHeader(header).removeHeader(header).addHeader("name", "value").removeHeaders("name");
		Assert.assertEquals(0, request.getHttpRequest().getAllHeaders().length);
		
		request.setCacheControl("temp");
		Assert.assertEquals("temp", request.getHttpRequest().getFirstHeader(HttpHeaderName.CACHE_CONTROL).getValue());
		
		Date date = new Date();
		request.setIfModifiedSince(date);
		String dte = request.getHttpRequest().getFirstHeader(HttpHeaderName.IF_MODIFIED_SINCE).getValue();
		Assert.assertEquals(date.getTime() / 10000l, Date.parse(dte) / 10000l);
		
		request.setIfUnmodifiedSince(date);
		dte = request.getHttpRequest().getFirstHeader(HttpHeaderName.IF_UNMODIFIED_SINCE).getValue();
		Assert.assertEquals(date.getTime() / 10000l, Date.parse(dte) / 10000l);
		
		request.version(HttpVersion.HTTP_1_1);
		Assert.assertEquals(HttpVersion.HTTP_1_1, request.getHttpRequest().getProtocolVersion());

		request.userAgent("temp123");
		Assert.assertEquals("temp123", request.getHttpRequest().getFirstHeader(HttpHeaderName.USER_AGENT).getValue());
	}
	
	@Test
	public void testToString() {
		WebRequest request = WebRequest.get("http://localhost");
		Assert.assertNotNull(request.toString());
	}
}
