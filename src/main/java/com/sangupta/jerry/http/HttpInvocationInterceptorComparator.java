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

import java.util.Comparator;

class HttpInvocationInterceptorComparator implements Comparator<HttpInvocationInterceptor> {

	@Override
	public int compare(HttpInvocationInterceptor o1, HttpInvocationInterceptor o2) {
		if(o1.getPriority() > o2.getPriority()) {
			return -1;
		}
		
		if(o1.getPriority() < o2.getPriority()) {
			return 1;
		}
		
		return 0;
	}

}
