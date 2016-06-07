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

import org.junit.Assert;
import org.junit.Test;

import com.sangupta.jerry.http.mock.MockWebResponse;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.http.service.impl.DefaultHttpServiceImpl;

/**
 * Unit tests involving {@link HttpInvocationInterceptor}.
 * 
 * @author sangupta
 *
 */
public class TestInterceptor {
	
	@Test
	public void testExceptions() {
		Assert.assertFalse(HttpExecutor.DEFAULT.removeInvocationInterceptor(null));
		
		try {
			HttpExecutor.DEFAULT.addInvocationInterception(null);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}
	
	@Test
	public void testFinalize() {
		try {
			HttpExecutor.DEFAULT.finalize();
			Assert.assertTrue(true);
		} catch(Throwable t) {
			Assert.assertTrue(false);
		}
	}
	
	@Test
	public void testInterception() {
		HttpService service = new DefaultHttpServiceImpl();
		service.setConnectionTimeout(10);
		service.setSocketTimeout(10);
		
		MockWebResponse response = new MockWebResponse("hello world");
		
		HttpInvocationInterceptor interceptor = new MyInterceptor(0, null, null);
		HttpExecutor.DEFAULT.addInvocationInterception(interceptor);
		Assert.assertNull(service.getResponse("http://localhost/hit"));
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor);
		
		interceptor = new MyInterceptor(0, response, null);
		HttpExecutor.DEFAULT.addInvocationInterception(interceptor);
		Assert.assertEquals("hello world", service.getTextResponse("http://localhost/hit"));
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor);
		
		interceptor = new MyInterceptor(0, null, response);
		HttpExecutor.DEFAULT.addInvocationInterception(interceptor);
		Assert.assertEquals("hello world", service.getTextResponse("http://localhost/hit"));
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor);
		
		interceptor = new MyInterceptor(0, new MockWebResponse("hello1"), new MockWebResponse("hello2"));
		HttpExecutor.DEFAULT.addInvocationInterception(interceptor);
		Assert.assertEquals("hello1", service.getTextResponse("http://localhost/hit"));
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor);

		interceptor = new MyInterceptor(10, new MockWebResponse("hello1"), new MockWebResponse("hello2"));
		HttpInvocationInterceptor interceptor2 = new MyInterceptor(0, new MockWebResponse("hello3"), new MockWebResponse("hello4"));

		HttpExecutor.DEFAULT.addInvocationInterception(interceptor);
		HttpExecutor.DEFAULT.addInvocationInterception(interceptor2);
		Assert.assertEquals("hello1", service.getTextResponse("http://localhost/hit"));
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor);
		HttpExecutor.DEFAULT.removeInvocationInterceptor(interceptor2);
		
		// clear all interceptors
		HttpExecutor.DEFAULT.removeAllInterceptors();
	}

	private class MyInterceptor implements HttpInvocationInterceptor {
		
		private int priority = 0;
		
		private WebResponse before = null;
		
		private WebResponse after = null;
		
		public MyInterceptor(int priority, WebResponse before, WebResponse after) {
			this.priority = priority;
			this.before = before;
			this.after = after;
		}
		
		@Override
		public int getPriority() {
			return this.priority;
		}

		@Override
		public WebResponse beforeInvocation(WebRequest request) {
			return this.before;
		}

		@Override
		public WebResponse afterInvocation(WebResponse response, IOException e) {
			return this.after;
		}
		
	}
}
