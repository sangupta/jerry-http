/**
 *
 * jerry-http - Common Java Functionality
 * Copyright (c) 2012-2014, Sandeep Gupta
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Builder class to help add form parameters to a {@link WebRequest} object.
 * 
 * @author sangupta
 *
 */
public class WebForm {
	
	/**
	 * Internal list of all params that have been added
	 */
	private List<NameValuePair> params;
	
	/**
	 * Private constructor
	 * 
	 */
	private WebForm() {
		this.params = new ArrayList<NameValuePair>();
	}
	
	/**
	 * Utility function to create a new instance.
	 * 
	 * @return a new instance of {@link WebForm}
	 */
	public static WebForm newForm() {
		return new WebForm();
	}

	/**
	 * Add a new parameter and value to this form.
	 * 
	 * @param name
	 *            the param name
	 * 
	 * @param value
	 *            the param value
	 * 
	 * @return this very {@link WebForm}
	 */
	public WebForm addParam(final String name, final String value) {
		this.params.add(new BasicNameValuePair(name, value));
		return this;
	}
	
	/**
	 * Add all map entries to this form. This method is safe against
	 * <code>null</code> being passed as input parameter object.
	 * 
	 * @param params
	 *            the params to add
	 * 
	 * @return this very {@link WebForm}
	 */
	public WebForm addParams(Map<String, String> params) {
		if(params == null) {
			return this;
		}
		
		for(Entry<String, String> entry : params.entrySet()) {
			this.params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		return this;
	}
	
	/**
	 * Return all the params back.
	 * 
	 * @return a list of all params as {@link NameValuePair}
	 */
	public List<NameValuePair> build() {
		return Collections.unmodifiableList(this.params);
	}
}
