/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2016, Sandeep Gupta
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.util.ByteArrayUtils;
import com.sangupta.jerry.util.HashUtils;

/**
 * Unit tests for {@link WebResponse} class.
 * 
 * @author sangupta
 *
 */
public class TestWebResponse {

	@Test
	public void test() throws IOException, URISyntaxException {
		String data = "0" + HashUtils.getMD5Hex(ByteArrayUtils.getRandomBytes(1024));
		WebResponse response = new WebResponse(data);
		
		// content
		byte[] bytes = data.getBytes();
		Assert.assertArrayEquals(bytes, response.asBytes());
		Assert.assertArrayEquals(bytes, response.asClonedBytes());
		Assert.assertEquals(data, response.getContent());

		InputStream stream = response.asStream();
		Assert.assertNotNull(stream);

		Assert.assertArrayEquals(bytes, IOUtils.toByteArray(stream));
		
		Assert.assertEquals(data, response.asContent(Charset.defaultCharset()));
		
		// response code
		response.responseCode = 200;
		Assert.assertTrue(response.isSuccess());
		response.responseCode = 299;
		Assert.assertTrue(response.isSuccess());
		Assert.assertFalse(response.isRedirect());
		Assert.assertFalse(response.isServerError());
		Assert.assertFalse(response.isClientError());
		
		response.responseCode = 300;
		Assert.assertTrue(response.isRedirect());
		response.responseCode = 399;
		Assert.assertTrue(response.isRedirect());
		Assert.assertFalse(response.isSuccess());
		Assert.assertFalse(response.isServerError());
		Assert.assertFalse(response.isClientError());

		response.responseCode = 500;
		Assert.assertTrue(response.isServerError());
		response.responseCode = 599;
		Assert.assertTrue(response.isServerError());
		Assert.assertFalse(response.isSuccess());
		Assert.assertFalse(response.isRedirect());
		Assert.assertFalse(response.isClientError());

		response.responseCode = 400;
		Assert.assertTrue(response.isClientError());
		response.responseCode = 499;
		Assert.assertTrue(response.isClientError());
		Assert.assertFalse(response.isSuccess());
		Assert.assertFalse(response.isRedirect());
		Assert.assertFalse(response.isServerError());

		// tostring and trace
		Assert.assertNotNull(response.toString());
		Assert.assertNotNull(response.trace());
		
		// size
		Assert.assertEquals(33, response.getSize());
		
		// redirects
		Assert.assertNull(response.getRedirectChain());
		Assert.assertFalse(response.hasRedirects());
		Assert.assertNull(response.getFinalURI());
		
		// set redirects
		response.redirectChain = new ArrayList<>();
		URI uri = new URI("http://localhost");
		response.redirectChain.add(uri);

		Assert.assertNotNull(response.getRedirectChain());
		Assert.assertTrue(response.hasRedirects());
		Assert.assertNotNull(response.getFinalURI());
		Assert.assertEquals(uri, response.getFinalURI());
		
		// empty chain
		response.redirectChain = new ArrayList<>();
		Assert.assertFalse(response.hasRedirects());
		Assert.assertNull(response.getFinalURI());
	}
	
	@Test
	public void testEmpty() {
		WebResponse response = new WebResponse((byte[]) null);
		Assert.assertNull(response.asStream());
		Assert.assertNull(response.asClonedBytes());
		Assert.assertNull(response.getHeaders().get("temp"));
	}
}
