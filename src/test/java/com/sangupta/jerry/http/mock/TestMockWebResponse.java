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

package com.sangupta.jerry.http.mock;

import java.nio.charset.Charset;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpHeaderName;
import com.sangupta.jerry.util.ByteArrayUtils;
import com.sangupta.jerry.util.HashUtils;

/**
 * Unit tests for {@link MockWebResponse} class.
 * 
 * @author sangupta
 *
 */
public class TestMockWebResponse {

	private final int MAX_LENGTH = 1024;
	
	private final String RANDOM_STRING = HashUtils.getMD5Hex(ByteArrayUtils.getRandomBytes(MAX_LENGTH));
	
	@Test
	public void testLastModified() {
		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
		Assert.assertEquals(-1, response.getLastModified());
		
		response.addHeader(HttpHeaderName.LAST_MODIFIED, "Fri, 03 Jun 2016 01:54:51 GMT");
		Assert.assertEquals(1464918891000l, response.getLastModified());
	}

	@Test
	public void testMockWebResponse() {
		Charset charset = Charset.defaultCharset();
		int responseCode = new Random().nextInt(200) + 200;
		long time = System.currentTimeMillis();
		
		MockWebResponse response = new MockWebResponse(RANDOM_STRING)
												.setCharset(charset)
												.setContentType(RANDOM_STRING)
												.setMessage(RANDOM_STRING)
												.setResponseCode(responseCode)
												.setLastModified(time)
												.addHeader(RANDOM_STRING, RANDOM_STRING);
			
		Assert.assertEquals(responseCode, response.getResponseCode());
		Assert.assertEquals(charset, response.getCharSet());
		Assert.assertEquals(RANDOM_STRING, response.getContentType());
		Assert.assertEquals(RANDOM_STRING, response.getMessage());
		Assert.assertEquals(time, response.getLastModified());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertEquals(1, response.getHeaders().size());
		Assert.assertEquals(RANDOM_STRING, response.getHeaders().get(RANDOM_STRING));
		
		MockWebResponse response2 = new MockWebResponse(RANDOM_STRING)
				.setCharset(charset)
				.setContentType(RANDOM_STRING)
				.setMessage(RANDOM_STRING)
				.setResponseCode(responseCode)
				.setLastModified(time)
				.addHeader(RANDOM_STRING, RANDOM_STRING);

		Assert.assertEquals(responseCode, response.getResponseCode());
		Assert.assertEquals(charset, response.getCharSet());
		Assert.assertEquals(RANDOM_STRING, response.getContentType());
		Assert.assertEquals(RANDOM_STRING, response.getMessage());
		Assert.assertEquals(time, response.getLastModified());
		Assert.assertNotNull(response.getHeaders());
		Assert.assertEquals(1, response.getHeaders().size());
		Assert.assertEquals(RANDOM_STRING, response.getHeaders().get(RANDOM_STRING));
		
		// check hashcode and equals
		Assert.assertEquals(response.hashCode(), response2.hashCode());
		Assert.assertTrue(response.equals(response));
		Assert.assertTrue(response.equals(response2));
		
		Assert.assertFalse(response.equals(null));
		Assert.assertFalse(response.equals(new Object()));
		
		response2 = new MockWebResponse(null);
		Assert.assertNotEquals(response.hashCode(), response2.hashCode());
		Assert.assertFalse(response.equals(response2));
		
		response = new MockWebResponse(null);
		Assert.assertEquals(response.hashCode(), response2.hashCode());
		Assert.assertTrue(response.equals(response2));
		
		Assert.assertFalse(new MockWebResponse(null).equals(new MockWebResponse("123")));
	}
	
}
