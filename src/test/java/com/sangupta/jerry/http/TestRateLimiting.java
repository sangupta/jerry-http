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

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.http.service.impl.DefaultHttpServiceImpl;

/**
 * Unit tests for rate limiting with {@link HttpService}.
 * 
 * @author sangupta
 *
 */
public class TestRateLimiting {
	
	@AfterClass
	public static void cleanUp() {
		HttpExecutor.DEFAULT.removeAllRateLimiting();
	}
	
	@Before
	public void setup() {
		HttpExecutor.DEFAULT.removeAllRateLimiting();
	}

	@Test
	public void testRateLimiting() {
		HttpService service = new DefaultHttpServiceImpl();
		service.setConnectionTimeout(10);
		service.setSocketTimeout(10);
		
		// add limit
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.MINUTES);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// try clear
		HttpExecutor.DEFAULT.removeRateLimiting("localhost");
		service.getTextResponse("http://localhost:8080/hit");
		
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.MINUTES);
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// try clearing all
		HttpExecutor.DEFAULT.removeAllRateLimiting();
		service.getTextResponse("http://localhost:8080/hit");
		

		// add limit
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.MINUTES);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// clear for something else
		HttpExecutor.DEFAULT.removeRateLimiting("unknown-domain");
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// just for coverage
		// add limit
		HttpExecutor.DEFAULT.removeAllRateLimiting();
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.DAYS);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// add limit
		HttpExecutor.DEFAULT.removeAllRateLimiting();
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.HOURS);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		// clear all
		HttpExecutor.DEFAULT.removeAllRateLimiting();
	}
	
	@Test
	public void testExceptions() {
		try {
			HttpExecutor.DEFAULT.addRateLimiting("localhost", 0, TimeUnit.DAYS);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			HttpExecutor.DEFAULT.addRateLimiting("localhost", 0, TimeUnit.DAYS);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

		HttpExecutor.DEFAULT.removeAllRateLimiting();
	}
	
	@Test
	public void testRateLimitingOtherHost() {
		HttpService service = new DefaultHttpServiceImpl();
		service.setConnectionTimeout(10);
		service.setSocketTimeout(10);
		
		// add limit
		HttpExecutor.DEFAULT.addRateLimiting("unknown-domain", 1, TimeUnit.MINUTES);
		service.getTextResponse("http://localhost:8080/hit");
		service.getTextResponse("http://localhost:8080/hit");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testActualClient() {
		HttpClient client = HttpExecutor.getHttpClient();
		Assert.assertNotNull(client);
		Assert.assertTrue(client instanceof HttpRateLimitingClient);
		
		Assert.assertNotNull(client.getConnectionManager());
		
		try {
			client.getParams();
			Assert.assertTrue(false);
		} catch(UnsupportedOperationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testRateLimitingTiming() {
		HttpService service = new DefaultHttpServiceImpl();
		service.setConnectionTimeout(10);
		service.setSocketTimeout(10);
		
		HttpExecutor.DEFAULT.addRateLimiting("localhost", 1, TimeUnit.SECONDS);
		service.getTextResponse("http://localhost:8080/hit");
		
		mustWait(1);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
		
		mustWait(1);
		service.getTextResponse("http://localhost:8080/hit");
		
		try {
			service.getTextResponse("http://localhost:8080/hit");
			Assert.assertTrue(false);
		} catch(HttpRateLimitException e) {
			Assert.assertTrue(true);
		}
	}
	
	private void mustWait(int seconds) {
		// wait for 10 seconds
		long delta = 1000l * seconds;
		long millis = System.currentTimeMillis() + delta; // 10 seconds
		do {
			try {
				Thread.sleep(delta);
			} catch (InterruptedException e) {
			}
			
			long time = System.currentTimeMillis();
			if(time > millis) {
				break;
			}
			
			delta = millis - time;
		} while(true);
	}
}
