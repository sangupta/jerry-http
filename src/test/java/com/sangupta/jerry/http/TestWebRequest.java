/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2017, Sandeep Gupta
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

package com.sangupta.jerry.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpHeaderName;
import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.util.ByteArrayUtils;

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
	
	@SuppressWarnings("deprecation")
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
		
		request = WebRequest.post("http://localhost:8080/hit");
		Assert.assertEquals(0, request.getHttpRequest().getAllHeaders().length);
		
		request.setHeaders(new Header[] { new BasicHeader("name", "value") });
		Assert.assertEquals(1, request.getHttpRequest().getAllHeaders().length);
		request.setHeaders(null);
		Assert.assertEquals(0, request.getHttpRequest().getAllHeaders().length);
		
		// date header
		request.setDate(date);
		dte = request.getHttpRequest().getFirstHeader(HttpHeaderName.DATE).getValue();
		Assert.assertEquals(date.getTime() / 10000l, Date.parse(dte) / 10000l);
	}
	
	@Test
	public void testConfig() {
		WebRequest request = WebRequest.post("http://localhost:8080/hit");
		request.prepareForExecute();
		Assert.assertEquals(false, request.getHttpRequest().getConfig().isExpectContinueEnabled());
		
		request.useExpectContinue();
		request.prepareForExecute();
		Assert.assertEquals(true, request.getHttpRequest().getConfig().isExpectContinueEnabled());
		
		Assert.assertEquals(true, request.getHttpRequest().getConfig().isRedirectsEnabled());
		request.noRedirects();
		request.prepareForExecute();
		Assert.assertEquals(false, request.getHttpRequest().getConfig().isRedirectsEnabled());
		
		Assert.assertNull(request.getHttpRequest().getConfig().getProxy());
		request.viaProxy("localhost");
		request.prepareForExecute();
		Assert.assertNotNull(request.getHttpRequest().getConfig().getProxy());
	}
	
	@Test
	public void testToString() {
		WebRequest request = WebRequest.get("http://localhost");
		Assert.assertNotNull(request.toString());
	}

	@Test
	public void testBody() throws UnsupportedOperationException, IOException {
		// exception on get request
		WebRequest request = WebRequest.get("http://localhost");
		try {
			request.body(new StringEntity("hello"));
			Assert.assertTrue(false);
		} catch(IllegalStateException e) {
			Assert.assertTrue(true);
		}
		
		// check for post
		request = WebRequest.post("http://localhost");
		
		// string
		request.body(new StringEntity("hello"));
		Assert.assertEquals("hello", bodyAsString(request));
		
		request.bodyString("hello123", HttpMimeType.TEXT_PLAIN);
		Assert.assertEquals("hello123", bodyAsString(request));
		
		request.bodyString("hello234", Charset.defaultCharset());
		Assert.assertEquals("hello234", bodyAsString(request));
		
		request.bodyString("hello123", ContentType.TEXT_PLAIN);
		Assert.assertEquals("hello123", bodyAsString(request));
		
		request.bodyString("hello1231", HttpMimeType.TEXT_PLAIN, Charset.defaultCharset().name());
		Assert.assertEquals("hello1231", bodyAsString(request));
		
		// bytes
		
		byte[] bytes = ByteArrayUtils.getRandomBytes(1024);
		request.bodyByteArray(bytes);
		Assert.assertArrayEquals(bytes, bodyAsBytes(request));
		
		request.bodyByteArray(bytes, 0, bytes.length);
		Assert.assertArrayEquals(bytes, bodyAsBytes(request));
		
		// stream
		
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		request.bodyStream(stream);
		Assert.assertArrayEquals(bytes, bodyAsBytes(request));
		
		stream = new ByteArrayInputStream(bytes);
		request.bodyStream(stream, ContentType.APPLICATION_OCTET_STREAM);
		Assert.assertArrayEquals(bytes, bodyAsBytes(request));
	
		// form
		request.bodyForm(new BasicNameValuePair("one", "two"), new BasicNameValuePair("three", "four"));
		Assert.assertEquals("one=two&three=four", bodyAsString(request));
		
		request.body(new StringEntity("hello"));
		request.bodyForm(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("one", "two"), new BasicNameValuePair("three", "four") }));
		Assert.assertEquals("one=two&three=four", bodyAsString(request));
		
		request.body(new StringEntity("hello"));
		request.bodyForm(Arrays.asList(new BasicNameValuePair[] { new BasicNameValuePair("one", "two"), new BasicNameValuePair("three", "four") }), Charset.defaultCharset());
		Assert.assertEquals("one=two&three=four", bodyAsString(request));
	}

	private String bodyAsString(WebRequest request) throws UnsupportedOperationException, IOException {
		InputStream stream = ((HttpEntityEnclosingRequest) request.getHttpRequest()).getEntity().getContent();
		return IOUtils.toString(stream);
	}

	private byte[] bodyAsBytes(WebRequest request) throws UnsupportedOperationException, IOException {
		InputStream stream = ((HttpEntityEnclosingRequest) request.getHttpRequest()).getEntity().getContent();
		return IOUtils.toByteArray(stream);
	}

}
