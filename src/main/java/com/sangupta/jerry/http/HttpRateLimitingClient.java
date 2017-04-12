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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * An implementation of the {@link HttpClient} that supports rate limiting
 * checks over given hosts/routes. The implementation needs an actual {@link HttpClient}
 * implementation which does the magic of all executing all calls.
 * 
 * @author sangupta
 * 
 * @since 0.3
 */
@SuppressWarnings("deprecation")
public class HttpRateLimitingClient implements HttpClient {
	
	/**
	 * Rate-limit mappings are their associated meta-data is kept here
	 */
	private static final Map<String, RateLimitValues> RATE_LIMITED_HOSTS = new HashMap<String, RateLimitValues>();
	
	/**
	 * The actual client instance that does all the magic
	 */
	private HttpClient actualClient;

	/**
	 * Boolean flag to skip checks if there are no limited hosts defined
	 */
	private volatile boolean hasHosts = false;
	
	/**
	 * Constructor - takes an actual implementation of an {@link HttpClient}.
	 * 
	 * @param actualClient
	 *            the {@link HttpClient} to use
	 */
	public HttpRateLimitingClient(HttpClient actualClient) {
		this.actualClient = actualClient;
	}
	
	/**
	 * Return the actual client being used by this wrapper.
	 * 
	 * @return the actual {@link HttpClient} being used
	 */
	public HttpClient getActualClient() {
		return this.actualClient;
	}
	
	/**
	 * Add rate limiting around a given host name
	 * 
	 * @param hostName
	 *            the host name
	 * 
	 * @param limit
	 *            the limit
	 * 
	 * @param timeUnit
	 *            the time unit against which the limit is set
	 */
	public void addRateLimiting(String hostName, int limit, TimeUnit timeUnit) {
		if(limit <= 0) {
			throw new IllegalArgumentException("Rate limit cannot be zero/negative");
		}
		
		long divisor;
		switch(timeUnit) {
			case SECONDS:
				divisor = 1000;
				break;
				
			case MINUTES:
				divisor = 60 * 1000;
				break;
				
			case HOURS:
				divisor = 60 * 60 * 1000;
				break;
				
			case DAYS:
				divisor = 24 * 60 * 60 * 1000;
				break;
				
			default:
				throw new IllegalArgumentException("Rate limiting not supported at this level.");
		}
		
		if(RATE_LIMITED_HOSTS.containsKey(hostName)) {
			throw new IllegalStateException("To change the rate limit, remove any current limit and then reset");
		}
		
		RATE_LIMITED_HOSTS.put(hostName.toLowerCase(), new RateLimitValues(divisor, limit));
		this.hasHosts = true;
	}
	
	/**
	 * Remove rate limiting around this hostname.
	 * 
	 * @param hostName
	 *            the host name to remove rate limiting from
	 */
	public void removeRateLimiting(String hostName) {
		RATE_LIMITED_HOSTS.remove(hostName);
		this.hasHosts = !RATE_LIMITED_HOSTS.isEmpty();
	}
	
	/**
	 * Clear all previously set rate limited hosting
	 * 
	 */
	public void removeAllRateLimiting() {
		RATE_LIMITED_HOSTS.clear();
		this.hasHosts = false;
	}
	
	/**
	 * The method checks if the current rate execution rate is within the prescribed limits or not
	 */
	private void assertRateInLimit(HttpUriRequest request) {
		if(!this.hasHosts) {
			return;
		}
		
		assertRateInLimit(request.getURI().getHost().toLowerCase());
	}
	
	/**
	 * The method checks if the current rate execution rate is within the prescribed limits or not
	 */
	private void assertRateInLimit(HttpHost target) {
		if(!this.hasHosts) {
			return;
		}
		
		assertRateInLimit(target.getHostName());
	}

	/**
	 * The method checks if the current rate execution rate is within the prescribed limits or not
	 */
	private void assertRateInLimit(String host) {
		RateLimitValues values = RATE_LIMITED_HOSTS.get(host);
		if(values == null) {
			return;
		}
		
		long block = System.currentTimeMillis() / values.interval;
		if(block == values.block) {
			int currentValue = values.current.intValue();
			if(currentValue == values.limit) {
				throw new HttpRateLimitException("Host is at its limit: " + host);
			}
			
			// we are in limits
			boolean updated = values.current.compareAndSet(currentValue, 1);
			if(!updated) {
				values.current.incrementAndGet();
			}
		} else {
			// reset the block
			values.block = block;
			values.current.set(1);
		}
	}

	/**
	 * @see org.apache.http.client.HttpClient#getParams()
	 */
	@Deprecated
	@Override
	public HttpParams getParams() {
		return this.actualClient.getParams();
	}

	/**
	 * @see org.apache.http.client.HttpClient#getConnectionManager()
	 */
	@Deprecated
	@Override
	public ClientConnectionManager getConnectionManager() {
		return this.actualClient.getConnectionManager();
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest)
	 */
	@Override
	public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
		assertRateInLimit(request);
		return this.actualClient.execute(request);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
		assertRateInLimit(request);
		return this.actualClient.execute(request, context);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, org.apache.http.HttpRequest)
	 */
	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
		assertRateInLimit(target);
		return this.actualClient.execute(target, request);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
		assertRateInLimit(target);
		return this.actualClient.execute(target, request, context);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		assertRateInLimit(request);
		return this.actualClient.execute(request, responseHandler);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest, org.apache.http.client.ResponseHandler, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
		assertRateInLimit(request);
		return this.actualClient.execute(request, responseHandler, context);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		assertRateInLimit(target);
		return this.actualClient.execute(target, request, responseHandler);
	}

	/**
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost, org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
		assertRateInLimit(target);
		return this.actualClient.execute(target, request, responseHandler, context);
	}

	/**
	 * Stores rate-limiting values
	 * 
	 * @author sangupta
	 *
	 */
	private static class RateLimitValues {
		
		final long interval;
		
		final int limit;
		
		AtomicInteger current = new AtomicInteger();
		
		long block;
		
		public RateLimitValues(long interval, int limit) {
			this.interval = interval;
			this.limit = limit;
		}
	}
	
}
