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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

/**
 * A wrapper that provides a builder based way to constructing a web request,
 * executing it via the default {@link HttpExecutor} and then returning the raw
 * {@link WebRawResponse}.
 * 
 * @author sangupta
 * @since 0.3
 * @added 20 October 2012
 */
public class WebRequest {

	public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    
	public static final Locale DATE_LOCALE = Locale.US;
    
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");

	/**
	 * 
	 * @param uri
	 * @return
	 */
    public static WebRequest get(final URI uri) {
        return new WebRequest(new HttpGet(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest get(final String uri) {
        return new WebRequest(new HttpGet(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest head(final URI uri) {
        return new WebRequest(new HttpHead(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest head(final String uri) {
        return new WebRequest(new HttpHead(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest post(final URI uri) {
        return new WebRequest(new HttpPost(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest post(final String uri) {
        return new WebRequest(new HttpPost(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest put(final URI uri) {
        return new WebRequest(new HttpPut(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest put(final String uri) {
        return new WebRequest(new HttpPut(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest trace(final URI uri) {
        return new WebRequest(new HttpTrace(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest trace(final String uri) {
        return new WebRequest(new HttpTrace(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest delete(final URI uri) {
        return new WebRequest(new HttpDelete(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest delete(final String uri) {
        return new WebRequest(new HttpDelete(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest options(final URI uri) {
        return new WebRequest(new HttpOptions(uri));
    }

    /**
     * 
     * @param uri
     * @return
     */
    public static WebRequest options(final String uri) {
        return new WebRequest(new HttpOptions(uri));
    }
    
    // Instance methods start here
    
    /**
     * 
     */
    private final HttpRequestBase request;

    /**
     * 
     */
    private final HttpParams localParams;

    /**
     * 
     */
    private SimpleDateFormat dateFormatter;

    /**
     * Create the {@link WebRequest} object using the given {@link HttpRequestBase} object.
     * 
     * @param request
     */
    WebRequest(final HttpRequestBase request) {
        super();
        this.request = request;
        this.localParams = request.getParams();
    }

    /**
     * Get the underlying {@link HttpRequestBase} object.
     * 
     * @return
     */
    HttpRequestBase getHttpRequest() {
        return this.request;
    }
    
    /**
     * Display the debug information for this request
     * 
     * @return
     */
    public WebRequest trace() {
    	System.out.println(this.request.getRequestLine());
    	Header[] headers = this.request.getAllHeaders();
    	if(headers != null) {
    		for(Header header : headers) {
    			System.out.println(header.toString());
    		}
    	}
    	
    	if (this.request instanceof HttpEntityEnclosingRequest) {
    		HttpEntityEnclosingRequest hecr = (HttpEntityEnclosingRequest) request;
    		HttpEntity entity = hecr.getEntity();
    		if(entity != null) {
    			if(entity.getContentType() != null) {
    				System.out.println(entity.getContentType().toString());
    			}
    			
    			if(entity.getContentEncoding() != null) {
    				System.out.println(entity.getContentEncoding().toString());
    			}
    			
    			System.out.println("Content-Length: " + entity.getContentLength());
    		}
    	}
    	
    	return this;
    }
    
    /**
     * Return the HTTP VERB associated with this request.
     * 
     * @return
     */
    public String getVerb() {
    	return this.request.getMethod().toUpperCase();
    }
    
    /**
     * Return the URI associated with this request.
     * 
     * @return
     */
    public URI getURI() {
    	return this.request.getURI();
    }
    
    /**
     * Return the type of request that we encapsulate.
     * 
     * @return
     */
    public WebRequestMethod getWebRequestMethod() {
    	if(this.request instanceof HttpGet) {
    		return WebRequestMethod.GET;
    	}
    	
    	if(this.request instanceof HttpPost) {
    		return WebRequestMethod.POST;
    	}
    	
    	if(this.request instanceof HttpPut) {
    		return WebRequestMethod.PUT;
    	}
    	
    	if(this.request instanceof HttpDelete) {
    		return WebRequestMethod.DELETE;
    	}
    	
    	if(this.request instanceof HttpHead) {
    		return WebRequestMethod.HEAD;
    	}
    	
    	if(this.request instanceof HttpTrace) {
    		return WebRequestMethod.TRACE;
    	}
    	
    	if(this.request instanceof HttpOptions) {
    		return WebRequestMethod.OPTIONS;
    	}
    	
    	throw new IllegalStateException("Unknown request type");
    }

    /**
     * Execute this web request now.
     * 
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public WebRawResponse execute() throws ClientProtocolException, IOException {
        return HttpExecutor.DEFAULT.execute(this);
    }

    /**
     * Abort this request now.
     * 
     * @throws UnsupportedOperationException
     */
    public void abort() throws UnsupportedOperationException {
        this.request.abort();
    }

    //// HTTP header operations

    /**
     * Add the given header to the request
     * 
     * @param header
     * @return
     */
    public WebRequest addHeader(final Header header) {
        this.request.addHeader(header);
        return this;
    }

    /**
     * Add a new header with the given name and value
     * 
     * @param name
     * @param value
     * @return
     */
    public WebRequest addHeader(final String name, final String value) {
        this.request.addHeader(name, value);
        return this;
    }

    /**
     * Remove the given header
     * 
     * @param header
     * @return
     */
    public WebRequest removeHeader(final Header header) {
        this.request.removeHeader(header);
        return this;
    }

    /**
     * Remove all headers with the given name.
     * 
     * @param name
     * @return
     */
    public WebRequest removeHeaders(final String name) {
        this.request.removeHeaders(name);
        return this;
    }

    /**
     * Set the request headers to the given list of headers.
     * 
     * @param headers
     * @return
     */
    public WebRequest setHeaders(final Header[] headers) {
        this.request.setHeaders(headers);
        return this;
    }

    /**
     * Set cache-control as the given one.
     * 
     * @param cacheControl
     * @return
     */
    public WebRequest setCacheControl(String cacheControl) {
        this.request.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
        return this;
    }

    /**
     * 
     * @return
     */
    private SimpleDateFormat getDateFormat() {
        if (this.dateFormatter == null) {
            this.dateFormatter = new SimpleDateFormat(DATE_FORMAT, DATE_LOCALE);
            this.dateFormatter.setTimeZone(TIME_ZONE);
        }
        return this.dateFormatter;
    }

    /**
     * Set the request date header to the given date value.
     * 
     * @param date
     * @return
     */
    public WebRequest setDate(final Date date) {
        this.request.setHeader(HttpHeaders.DATE, getDateFormat().format(date));
        return this;
    }

    /**
     * Set the If-Modified-Since header to the given date value.
     * 
     * @param date
     * @return
     */
    public WebRequest setIfModifiedSince(final Date date) {
        this.request.setHeader(HttpHeaders.IF_MODIFIED_SINCE, getDateFormat().format(date));
        return this;
    }

    /**
     * Set the If-Unmodified-Since header to the given date value
     *  
     * @param date
     * @return
     */
    public WebRequest setIfUnmodifiedSince(final Date date) {
        this.request.setHeader(HttpHeaders.IF_UNMODIFIED_SINCE, getDateFormat().format(date));
        return this;
    }

    //// HTTP config parameter operations

    /**
     * Set the local param for this request to the value.
     * 
     * @param param
     * @param object
     * @return
     */
    public WebRequest config(final String param, final Object object) {
        this.localParams.setParameter(param, object);
        return this;
    }

    /**
     * Remove the local config param from this request with the given name
     * 
     * @param param
     * @return
     */
    public WebRequest removeConfig(final String param) {
        this.localParams.removeParameter(param);
        return this;
    }

    //// HTTP protocol parameter operations

    /**
     * Set the request HTTP Protocol version
     * 
     * @param version
     * @return
     */
    public WebRequest version(final HttpVersion version) {
        return config(CoreProtocolPNames.PROTOCOL_VERSION, version);
    }

    /**
     * 
     * @param charset
     * @return
     */
    public WebRequest elementCharset(final String charset) {
        return config(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, charset);
    }

    /**
     * 
     * @return
     */
    public WebRequest useExpectContinue() {
        return config(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
    }

    /**
     * Set the request User-Agent string to the given one.
     * 
     * @param agent
     * @return
     */
    public WebRequest userAgent(final String agent) {
        return config(CoreProtocolPNames.USER_AGENT, agent);
    }

    //// HTTP connection parameter operations
    
    /**
     * Follow redirects for the request
     * 
     * @return
     */
    public WebRequest followRedirects() {
    	HttpParams params = this.request.getParams();
    	params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
    	return this;
    }

    /**
     * Do not follow any redirects
     * 
     * @return
     */
    public WebRequest noRedirects() {
    	HttpParams params = this.request.getParams();
    	params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
    	return this;
    }
    
    /**
     * Specify the socket time out to the given value.
     * 
     * @param timeout
     * @return
     */
    public WebRequest socketTimeout(int timeout) {
        return config(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }
    
    /**
     * Specify the connection time out to the given value.
     * 
     * @param timeout
     * @return
     */
    public WebRequest connectTimeout(int timeout) {
        return config(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    /**
     * Specifies if stale connection check needs to be performed before
     * making a connection.
     * 
     * @param perform
     * @return
     */
    public WebRequest staleConnectionCheck(boolean perform) {
        return config(CoreConnectionPNames.STALE_CONNECTION_CHECK, perform);
    }

    //// HTTP connection route operations

    /**
     * 
     * @param proxy
     * @return
     */
    public WebRequest viaProxy(final HttpHost proxy) {
        return config(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    //// HTTP entity operations

    /**
     * 
     * @param entity
     * @return
     */
    public WebRequest body(final HttpEntity entity) {
        if (this.request instanceof HttpEntityEnclosingRequest) {
            ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
        } else {
            throw new IllegalStateException(this.request.getMethod() + " request cannot enclose an entity");
        }
        
        return this;
    }

    /**
     * 
     * @param formParams
     * @param charset
     * @return
     */
    public WebRequest bodyForm(final Iterable <? extends NameValuePair> formParams, final Charset charset) {
        return body(new UrlEncodedFormEntity(formParams, charset));
    }

    /**
     * 
     * @param formParams
     * @return
     */
    public WebRequest bodyForm(final Iterable <? extends NameValuePair> formParams) {
        return bodyForm(formParams, HTTP.DEF_CONTENT_CHARSET);
    }

    /**
     * 
     * @param formParams
     * @return
     */
    public WebRequest bodyForm(final NameValuePair... formParams) {
        return bodyForm(Arrays.asList(formParams), HTTP.DEF_CONTENT_CHARSET);
    }

    /**
     * 
     * @param s
     * @param contentType
     * @return
     */
    public WebRequest bodyString(final String s, final ContentType contentType) {
        return body(new StringEntity(s, contentType));
    }

    /**
     * 
     * @param file
     * @param contentType
     * @return
     */
    public WebRequest bodyFile(final File file, final ContentType contentType) {
        return body(new FileEntity(file, contentType));
    }

    /**
     * 
     * @param b
     * @return
     */
    public WebRequest bodyByteArray(final byte[] b) {
        return body(new ByteArrayEntity(b));
    }

    /**
     * 
     * @param b
     * @param off
     * @param len
     * @return
     */
    public WebRequest bodyByteArray(final byte[] b, int off, int len) {
        return body(new ByteArrayEntity(b, off, len));
    }

    /**
     * 
     * @param instream
     * @return
     */
    public WebRequest bodyStream(final InputStream instream) {
        return body(new InputStreamEntity(instream, -1));
    }

    /**
     * 
     * @param instream
     * @param contentType
     * @return
     */
    public WebRequest bodyStream(final InputStream instream, final ContentType contentType) {
        return body(new InputStreamEntity(instream, -1, contentType));
    }

	/**
	 * @param cookiePolicy
	 * @return
	 */
	public WebRequest cookiePolicy(String cookiePolicy) {
		this.request.getParams().setParameter(ClientPNames.COOKIE_POLICY, cookiePolicy);
		return this;
	}

    /**
     * Convert this request to {@link String} format. This is basically a 
     * representation of the request line that will be sent over the wire.
     * 
     */
    @Override
    public String toString() {
        return this.request.getRequestLine().toString();
    }

}
