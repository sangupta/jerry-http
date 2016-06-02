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
