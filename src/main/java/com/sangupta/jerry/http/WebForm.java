/**
 *
 * jerry - Common Java Functionality
 * Copyright (c) 2012, Sandeep Gupta
 * 
 * http://www.sangupta/projects/jerry
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
	 * @return
	 */
	public static WebForm newForm() {
		return new WebForm();
	}

	/**
	 * Add a new parameter and value to this form.
	 * 
	 * @param name
	 * @param value
	 * @return
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
	 * @return
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
	 * Return all the params back
	 * 
	 * @return
	 */
	public List<NameValuePair> build() {
		return Collections.unmodifiableList(this.params);
	}
}
