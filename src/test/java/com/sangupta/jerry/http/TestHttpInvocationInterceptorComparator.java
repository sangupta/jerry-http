package com.sangupta.jerry.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link HttpInvocationInterceptorComparator}.
 * 
 * @author sangupta
 *
 */
public class TestHttpInvocationInterceptorComparator {

	@Test
	public void testComparator() {
		HttpInvocationInterceptorComparator comparator = new HttpInvocationInterceptorComparator();
		
		Assert.assertEquals(0, comparator.compare(new Hii(3), new Hii(3)));
		Assert.assertEquals(1, comparator.compare(new Hii(0), new Hii(3)));
		Assert.assertEquals(-1, comparator.compare(new Hii(3), new Hii(0)));
	}

	private static class Hii implements HttpInvocationInterceptor {
		
		public Hii(int priority) {
			this.priority = priority;
		}
		
		private int priority;

		@Override
		public int getPriority() {
			return this.priority;
		}

		@Override
		public WebResponse beforeInvocation(WebRequest request) {
			return null;
		}

		@Override
		public WebResponse afterInvocation(WebResponse response) {
			return null;
		}
		
	}
	
}
