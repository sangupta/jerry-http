package com.sangupta.jerry.http;

import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link HttpExecutor}.
 * 
 * @author sangupta
 *
 */
public class TestHttpExecutor {

	@Test
	public void test() {
		Assert.assertNotNull(HttpExecutor.DEFAULT);
		Assert.assertNotNull(HttpExecutor.newInstance());
		Assert.assertNotNull(HttpExecutor.newInstance(HttpExecutor.getHttpClient()));
		
		try {
			HttpExecutor.newInstance(null);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		// max connections
		HttpExecutor.setMaxConnections(54);
		Assert.assertEquals(54, HttpExecutor.getConnectionManager().getMaxTotal());
		
		HttpExecutor.setMaxConnectionsPerRoute(22);
		Assert.assertEquals(22, HttpExecutor.getConnectionManager().getDefaultMaxPerRoute());
		
		HttpRoute route = new HttpRoute(new HttpHost("localhost"));
		HttpExecutor.setMaxConnectionsOnHost(route, 13);
		Assert.assertEquals(13, HttpExecutor.getConnectionManager().getMaxPerRoute(route));
		
		HttpExecutor.setMaxConnectionsOnHost("local", 9);
		route = new HttpRoute(new HttpHost("local"));
		Assert.assertEquals(9, HttpExecutor.getConnectionManager().getMaxPerRoute(route));
		
		HttpExecutor.setMaxConnectionsOnHost("local", 8080, 97);
		route = new HttpRoute(new HttpHost("local", 8080));
		Assert.assertEquals(97, HttpExecutor.getConnectionManager().getMaxPerRoute(route));
		
		// exceptions
		try {
			HttpExecutor.setMaxConnections(0);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			HttpExecutor.setMaxConnectionsPerRoute(0);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			route = new HttpRoute(new HttpHost("localhost"));
			HttpExecutor.setMaxConnectionsOnHost(route, 0);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			HttpExecutor.setMaxConnectionsOnHost("local", 0);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		try {
			HttpExecutor.setMaxConnectionsOnHost("local", 8080, 0);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}
}
