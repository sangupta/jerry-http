/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2015, Sandeep Gupta
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

package com.sangupta.jerry.http.intercept;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;

/**
 * Contract for an interceptor that can be added to the {@link WebInvoker} utility
 * classes to add hooks before and after invocation.
 * 
 * @author sangupta
 * @since 0.2.0
 */
public interface WebInvocationInterceptor {

	/**
	 * Intercepting method that is invoked before each request of
	 * {@link WebInvoker} is made thus providing a way to add hooks to modify
	 * the request originating from somewhere else.
	 * 
	 * The URL provided here contains all query parameters that might have been
	 * added. In case of POST requests the query parameters are not added.
	 * 
	 * @param url
	 *            the URL whose invocation has been requested
	 * 
	 * @param method
	 *            the HTTP verb for the invocation
	 * 
	 * @return a {@link WebResponse} object that may be returned back to the
	 *         calling code if {@link #continueInvocation()} returns
	 *         <code>false</code>. If {@link #continueInvocation()} returns
	 *         <code>true</code>, the return value of this method is ignored.
	 * 
	 */
	public WebResponse beforeInvocation(String url, WebRequestMethod method);
	
	/**
	 * Indicates whether invocation should continue as per the original plan.
	 * 
	 * @return <code>true<code> if invocation needs to be continues, <code>false</code>
	 *         otherwise.
	 */
	public boolean continueInvocation();
	
	/**
	 * Intercepting method that is invoked after each request of
	 * {@link WebInvoker} thus providing a way to add hooks to update the
	 * response received from the server.
	 * 
	 * This may be needed in scenarios like mock testing.
	 * 
	 * @param response
	 *            the response as received from the server
	 * 
	 * @return response that needs to be sent back to the calling code
	 */
	public WebResponse afterInvocation(WebResponse response);
	
}