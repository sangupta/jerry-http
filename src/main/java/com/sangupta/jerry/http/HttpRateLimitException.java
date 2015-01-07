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

package com.sangupta.jerry.http;

/**
 * Runtime exception that signifies that we are going overboard when hitting
 * the host, which has a prescribed rate limit.
 * 
 * @author sangupta
 * 
 * @since 0.3
 */
public class HttpRateLimitException extends RuntimeException {

	/**
	 * Generated via Eclipse
	 */
	private static final long serialVersionUID = -8455577300423526727L;

	/**
	 * Default construtor
	 * 
	 */
	public HttpRateLimitException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public HttpRateLimitException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public HttpRateLimitException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * @param cause
	 */
	public HttpRateLimitException(Throwable cause) {
		super(cause);
	}
	
}