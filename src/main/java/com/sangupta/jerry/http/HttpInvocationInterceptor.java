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

/**
 * Contract for an interceptor that can be added to the {@link WebInvoker} utility
 * classes to add hooks before and after invocation.
 * 
 * @author sangupta
 * @since 0.2.0
 */
public interface HttpInvocationInterceptor {
	
	/**
	 * Return the priority of the interceptor. The higher the priority the
	 * earlier it is executed in the interception chain.
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * Intercepting method that is invoked before each {@link WebRequest} is run
	 * using the {@link HttpExecutor} - this provides a way to modify the
	 * request prior to run, if needed.
	 * 
	 * @param request
	 *            the {@link WebRequest} object
	 * 
	 * @return <code>null</code> if the execution chain should continue as if
	 *         there was no interceptor. An instance of {@link WebResponse} will
	 *         break the execution chain and the value will be returned to the
	 *         callee.
	 * 
	 */
	public WebResponse beforeInvocation(WebRequest request);
	
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
	 * @param exception
	 *            the {@link IOException} if thrown when hitting the webservice
	 * 
	 * @return response that needs to be sent back to the calling code
	 */
	public WebResponse afterInvocation(WebResponse response, IOException exception);
	
}