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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.sangupta.jerry.util.AssertUtils;

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
	private final List<NameValuePair> params;
	
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
	 * Return the values of the parameter with the given name.
	 * 
	 * @param name
	 *            the param name
	 * 
	 * @return the list containing all values of the parameter, or
	 *         <code>empty</code> list if the parameter is not present
	 */
	public List<String> getParam(final String name) {
		List<String> values = new ArrayList<String>();
		
		for(int index = 0; index < this.params.size(); index++) {
			NameValuePair pair = this.params.get(index);
			if(name.equals(pair.getName())) {
				values.add(pair.getValue());
			}
		}
		
		return values;
	}

	/**
	 * Add a new parameter and value to this form. Any existing duplicates will
	 * be replaced.
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
		return this.addParam(name, value, false);
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
	 * @param keepDuplicates
	 * 			  whether to preserve duplicates or not
	 * 
	 * @return this very {@link WebForm}
	 */
	public WebForm addParam(final String name, final String value, final boolean keepDuplicates) {
		if(AssertUtils.isEmpty(name)) {
			throw new IllegalArgumentException("Parameter name cannot be null/empty");
		}
		
		NameValuePair newPair = new BasicNameValuePair(name, value);
		
		if(!keepDuplicates) {
			if(this.params.isEmpty()) {
				this.params.add(newPair);
				return this;
			}
			
			for(int index = 0; index < this.params.size(); index++) {
				NameValuePair pair = this.params.get(index);
				if(name.equals(pair.getName())) {
					// we need to replace this
					this.params.remove(index);
					this.params.add(index, newPair);
					return this;
				}
			}
		}
		
		this.params.add(newPair);
		return this;
	}
	
	/**
	 * Add all map entries to this form. This method is safe against
	 * <code>null</code> being passed as input parameter object. The
	 * parameters will replace any existing parameter.
	 * 
	 * @param params
	 *            the params to add
	 * 
	 * @return this very {@link WebForm}
	 */
	public WebForm addParams(Map<String, String> params) {
		return this.addParams(params, false);
	}
	
	/**
	 * Add all map entries to this form. This method is safe against
	 * <code>null</code> being passed as input parameter object.
	 * 
	 * @param params
	 *            the params to add
	 * 
	 * @param keepDuplicates
	 * 			  whether to preserve duplicates or replace them
	 * 
	 * @return this very {@link WebForm}
	 */
	public WebForm addParams(Map<String, String> params, final boolean keepDuplicates) {
		if(params == null) {
			return this;
		}
		
		for(Entry<String, String> entry : params.entrySet()) {
			this.addParam(entry.getKey(), entry.getValue(), keepDuplicates);
		}
		
		return this;
	}
	
	/**
	 * Check if the current web form contains any parameters or not.
	 * 
	 * @return <code>true</code> if the form has no params added,
	 *         <code>false</code> otherwise.
	 * 
	 */
	public boolean isEmpty() {
		return this.params.isEmpty();
	}
	
	/**
	 * Removes all of the elements from this operation. The form will be empty
	 * after this call returns.
	 * 
	 */
	public void clear() {
		this.params.clear();
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