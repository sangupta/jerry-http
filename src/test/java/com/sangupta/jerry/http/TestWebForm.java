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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link WebForm} class
 * 
 * @author sangupta
 *
 */
public class TestWebForm {

	@Test
	public void testEmptyForm() {
		WebForm form = WebForm.newForm();
		
		Assert.assertNotNull(form);
		Assert.assertTrue(form.isEmpty());
	}
	
	@Test 
	public void testParams() {
		WebForm form = WebForm.newForm();
		
		Assert.assertNotNull(form);
		form.addParam("nodup", "v1");
		form.addParam("nodup", "v2");
		form.addParam("dup", "v1", true);
		form.addParam("dup", "v2", true);
		
		Assert.assertFalse(form.isEmpty());
		Assert.assertEquals(Arrays.asList(new String[] { "v2" }), form.getParam("nodup"));
		Assert.assertEquals(Arrays.asList(new String[] { "v1", "v2" }), form.getParam("dup"));
		
		// test size
		form.clear();
		Assert.assertEquals(1, form.addParam("1", "value").build().size());
		form.addParams(null, true);
		Assert.assertEquals(1, form.addParam("1", "value").build().size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddParamException() {
		WebForm.newForm().addParam(null, "value");
	}
	
	@Test
	public void testParamsMap() {
		WebForm form = WebForm.newForm();
		
		Assert.assertNotNull(form);
		
		Map<String, String> map = new HashMap<>();
		map.put("nodup", "v1");
		map.put("nodup", "v2");
		form.addParams(map);
		
		map = new HashMap<>();
		form.addParam("dup", "v1", true);
		map.put("dup", "v2");
		form.addParams(map, true);
		
		Assert.assertFalse(form.isEmpty());
		Assert.assertEquals(Arrays.asList(new String[] { "v2" }), form.getParam("nodup"));
		Assert.assertEquals(Arrays.asList(new String[] { "v1", "v2" }), form.getParam("dup"));
	}
	
	@Test
	public void testClear() {
		WebForm form = WebForm.newForm();
		
		Assert.assertNotNull(form);
		form.addParam("nodup", "v1");
		form.addParam("nodup", "v2");
		form.addParam("dup", "v1", true);
		form.addParam("dup", "v2", true);
		
		Assert.assertFalse(form.isEmpty());
		Assert.assertEquals(Arrays.asList(new String[] { "v2" }), form.getParam("nodup"));
		Assert.assertEquals(Arrays.asList(new String[] { "v1", "v2" }), form.getParam("dup"));

		form.clear();
		Assert.assertNotNull(form);
		Assert.assertTrue(form.isEmpty());
		Assert.assertEquals(Arrays.asList(new String[] { }), form.getParam("nodup"));
		Assert.assertEquals(Arrays.asList(new String[] { }), form.getParam("dup"));
	}
}
