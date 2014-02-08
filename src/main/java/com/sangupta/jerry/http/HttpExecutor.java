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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * @author sangupta
 * @since 0.3
 * @added 20 October 2012
 */
public class HttpExecutor {

    /**
     *  Create an HttpClient with the PoolingClientConnectionManager.
     *  This connection manager must be used if more than one thread will
     *  be using the HttpClient.
     */
	private static final PoolingClientConnectionManager HTTP_CONNECTION_MANAGER;
	
	/**
	 * The singleton instance of HttpClient
	 */
	public static final HttpClient HTTP_CLIENT;
	
	/**
	 * Build up the default instance
	 */
	static {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        SchemeSocketFactory plain = PlainSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("http", 80, plain));
        SchemeSocketFactory ssl = null;
        
        try {
            ssl = SSLSocketFactory.getSystemSocketFactory();
        } catch (SSLInitializationException ex) {
            SSLContext sslcontext;
            try {
                sslcontext = SSLContext.getInstance(SSLSocketFactory.TLS);
                sslcontext.init(null, new TrustManager[] {
                		
                		// make sure that we accept all SSL certificates
                		new X509TrustManager() {
							
							@Override
							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}
							
							@Override
							public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
								
							}
							
							@Override
							public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
								
							}
						}
                		
                }, null);
                ssl = new SSLSocketFactory(sslcontext);
            } catch (SecurityException ignore) {
            	// do nothing
            } catch (KeyManagementException ignore) {
            	// do nothing
            } catch (NoSuchAlgorithmException ignore) {
            	// do nothing
            }
        }
        
        if (ssl != null) {
            schemeRegistry.register(new Scheme("https", 443, ssl));
        }
        
        HTTP_CONNECTION_MANAGER = new PoolingClientConnectionManager(schemeRegistry);
        HTTP_CONNECTION_MANAGER.setDefaultMaxPerRoute(100);
        HTTP_CONNECTION_MANAGER.setMaxTotal(200);
        HTTP_CLIENT = new HttpRateLimitingClient(new DefaultHttpClient(HTTP_CONNECTION_MANAGER));
	}
	
	/**
	 * Default {@link HttpExecutor} instance that can be used across application
	 */
	public static final HttpExecutor DEFAULT = new HttpExecutor(HTTP_CLIENT);
	
	/**
	 * Return the underlying {@link HttpClient} instance that can be used
	 * to make web requests. All requests shot using this client honor
	 * rate-limiting.
	 * 
	 * @return
	 */
	public static final HttpClient getHttpClient() {
		return HTTP_CLIENT;
	}
	
	/**
	 * Get a new {@link HttpExecutor} instance based on default client
	 * 
	 * @return
	 */
	public static final HttpExecutor newInstance() {
		return new HttpExecutor(HTTP_CLIENT);
	}
	
	/**
	 * Get a new {@link HttpExecutor} instance based on given client
	 * 
	 * @param client
	 * @return
	 */
	public static final HttpExecutor newInstance(HttpClient client) {
		return new HttpExecutor(client);
	}
	
	/**
	 * Set overall maximum connections that can be handled by the underlying connection manager.
	 * 
	 * @param numConnections
	 */
	public static void setMaxConnections(int numConnections) {
		HTTP_CONNECTION_MANAGER.setMaxTotal(numConnections);
	}
	
	/**
	 * Set overall maximum connections per route (over all hosts) that can be handled by the underlying connection
	 * manager.
	 * 
	 * @param numConnections
	 */
	public static void setMaxConnectionsPerRoute(int numConnections) {
		HTTP_CONNECTION_MANAGER.setDefaultMaxPerRoute(numConnections);
	}
	
	/**
	 * Set maximum connections that will be operated over the given route, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param route
	 * @param numConnections
	 */
	public static void setMaxConnectionsOnHost(HttpRoute route, int numConnections) {
		HTTP_CONNECTION_MANAGER.setMaxPerRoute(route, numConnections);
	}
	
	/**
	 * Set maximum connections that will be operated over the given host on port 80, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param hostName
	 * @param numConnections
	 */
	public static void setMaxConnectionsOnHost(String hostName, int numConnections) {
		HttpRoute route = new HttpRoute(new HttpHost(hostName));
		setMaxConnectionsOnHost(route, numConnections);
	}
	
	/**
	 * Set maximum connections that will be operated over the given host on given port, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param hostName
	 * @param port
	 * @param numConnections
	 */
	public static void setMaxConnectionsOnHost(String hostName, int port, int numConnections) {
		HttpRoute route = new HttpRoute(new HttpHost(hostName, port));
		setMaxConnectionsOnHost(route, numConnections);
	}
	
	// Instance class starts from here
	
	/**
	 * The underlying {@link HttpClient} that will be used by the executor
	 * 
	 */
	private final HttpClient client;
	
	/**
	 * The authentication caching instance that will be used by the executor
	 * 
	 */
	private final AuthCache authCache;
	
	/**
	 * Not declared final - for an instance may not be required throught the application life-cycle
	 */
	private CredentialsProvider credentialsProvider;
	
	/**
	 * Not declared final - for an instance may not be required throught the application life-cycle
	 */
	private CookieStore cookieStore;
	
	private HttpExecutor(final HttpClient client) {
		if(client == null) {
			throw new IllegalArgumentException("Cannot create executor over null client instance");
		}

		this.client = client;
		this.authCache = new BasicAuthCache();
	}
	
	/**
	 * @param webRequest
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public WebRawResponse execute(WebRequest webRequest) throws ClientProtocolException, IOException {
		// sharing the context may lead to circular redirects in case
		// of redirections from two request objects towards a single
		// URI - like hitting http://google.com twice leads to circular
		// redirects in the second request
		HttpContext localHttpContext = new BasicHttpContext();
		
        localHttpContext.setAttribute(ClientContext.CREDS_PROVIDER, this.credentialsProvider);
        localHttpContext.setAttribute(ClientContext.AUTH_CACHE, this.authCache);
        localHttpContext.setAttribute(ClientContext.COOKIE_STORE, this.cookieStore);
        
        // localHttpContext.removeAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS);
        
        HttpRequestBase httpRequest = webRequest.getHttpRequest();
        httpRequest.reset();
        return new WebRawResponse(this.client.execute(httpRequest, localHttpContext));
	}
    
	// Methods related to rate limiting
	
	/**
	 * Add new rate limiting for the given host.
	 * 
	 * @param hostName
	 * @param limit
	 * @param timeUnit
	 */
	public HttpExecutor addRateLimiting(String hostName, int limit, TimeUnit timeUnit) {
		if(this.client instanceof HttpRateLimitingClient) {
			((HttpRateLimitingClient) this.client).addRateLimiting(hostName, limit, timeUnit);
			return this;
		}
		
		throw new IllegalStateException("Current client does not support rate-limiting");
	}
	
	/**
	 * Remove any previous rate limiting that has been set for the host.
	 * 
	 * @param hostName
	 */
	public HttpExecutor removeRateLimiting(String hostName) {
		if(this.client instanceof HttpRateLimitingClient) {
			((HttpRateLimitingClient) this.client).removeRateLimiting(hostName);
			return this;
		}
		
		throw new IllegalStateException("Current client does not support rate-limiting");
	}
	
	/**
	 * Remove all previously set rate limiting.
	 * 
	 * @return
	 */
	public HttpExecutor removeAllRateLimiting() {
		if(this.client instanceof HttpRateLimitingClient) {
			((HttpRateLimitingClient) this.client).removeAllRateLimiting();
			return this;
		}
		
		throw new IllegalStateException("Current client does not support rate-limiting");
	}
	
	// Methods related to authentication
	
	/**
	 * Add provided authentication
	 * 
	 * @param authScope
	 * @param credentials
	 * @return
	 */
	public HttpExecutor addAuthentication(AuthScope authScope, Credentials credentials) {
		if (this.credentialsProvider == null) {
            this.credentialsProvider = new BasicCredentialsProvider();
        }
        this.credentialsProvider.setCredentials(authScope, credentials);
        return this;
	}
	
	/**
	 * Clear all authentication that may have been set.
	 * 
	 * @return
	 */
	public HttpExecutor clearAllAuthentication() {
        if (this.credentialsProvider != null) {
            this.credentialsProvider.clear();
        }
        
        return this;
    }
	
	/**
	 * Add authentication for a given host
	 * 
	 * @param host
	 * @param userName
	 * @param password
	 */
	public HttpExecutor addAuthentication(String host, String userName, String password) {
		AuthScope authScope = new AuthScope(host, 80);
		Credentials credentials = new UsernamePasswordCredentials(userName, password);
		return this.addAuthentication(authScope, credentials);
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 * @return
	 */
	public HttpExecutor addAuthentication(String host, int port, String userName, String password) {
		AuthScope authScope = new AuthScope(host, port);
		Credentials credentials = new UsernamePasswordCredentials(userName, password);
		return this.addAuthentication(authScope, credentials);
	}

	/**
	 * 
	 * @param host
	 * @return
	 */
	public HttpExecutor removeAuthentication(String host) {
		AuthScope authScope = new AuthScope(host, 80);
		return this.addAuthentication(authScope, null);
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public HttpExecutor removeAuthentication(String host, int port) {
		AuthScope authScope = new AuthScope(host, port);
		return this.addAuthentication(authScope, null);
	}

	// Methods related to cookie store
	
	/**
	 * Replace or set the given cookie store as the cookie store instance to be used.
	 * 
	 * @param cookieStore
	 * @return
	 */
    public HttpExecutor setCookieStore(final CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    /**
     * Clear all cookies in the cookie store if any present.
     * 
     * @return
     */
    public HttpExecutor clearCookies() {
        if (this.cookieStore != null) {
            this.cookieStore.clear();
        }
        return this;
    }

    // Utility methods start here
    
	/**
	 * Set overall maximum connections that can be handled by the underlying connection manager.
	 * 
	 * @param numConnections
	 */
	public HttpExecutor maxConnections(int numConnections) {
		setMaxConnections(numConnections);
		return this;
	}
	
	/**
	 * Set overall maximum connections per route (over all hosts) that can be handled by the underlying connection
	 * manager.
	 * 
	 * @param numConnections
	 */
	public HttpExecutor maxConnectionsPerRoute(int numConnections) {
		setMaxConnectionsPerRoute(numConnections);
		return this;
	}
	
	/**
	 * Set maximum connections that will be operated over the given route, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param route
	 * @param numConnections
	 */
	public HttpExecutor maxConnectionsOnHost(HttpRoute route, int numConnections) {
		setMaxConnectionsOnHost(route, numConnections);
		return this;
	}
	
	/**
	 * Set maximum connections that will be operated over the given host on port 80, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param hostName
	 * @param numConnections
	 */
	public HttpExecutor maxConnectionsOnHost(String hostName, int numConnections) {
		HttpRoute route = new HttpRoute(new HttpHost(hostName));
		setMaxConnectionsOnHost(route, numConnections);
		return this;
	}
	
	/**
	 * Set maximum connections that will be operated over the given host on given port, that will be handled by the underlying
	 * connection manager.
	 * 
	 * @param hostName
	 * @param port
	 * @param numConnections
	 */
	public HttpExecutor maxConnectionsOnHost(String hostName, int port, int numConnections) {
		HttpRoute route = new HttpRoute(new HttpHost(hostName, port));
		setMaxConnectionsOnHost(route, numConnections);
		return this;
	}
	
	// Finalization methods
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		try {
			HTTP_CONNECTION_MANAGER.shutdown();
		} catch(Throwable t) {
			// eat up
		}
	}

}
