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
import java.text.DateFormat;
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
 * 
 * @since 0.3
 */
public class WebRequest {

	/**
	 * The date format to use
	 */
	public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    
	/**
	 * The date local for US timezone
	 */
	public static final Locale DATE_LOCALE = Locale.US;
    
	/**
	 * The GMT timezone
	 */
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");

	/**
	 * Create a HTTP GET based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
	 */
    public static WebRequest get(final URI uri) {
        return new WebRequest(new HttpGet(uri));
    }

    /**
	 * Create a HTTP GET based {@link WebRequest} for the given string uri
	 * 
	 * @param uri
	 *            the string uri for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest get(final String uri) {
        return new WebRequest(new HttpGet(uri));
    }

    /**
	 * Create a HTTP HEAD based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest head(final URI uri) {
        return new WebRequest(new HttpHead(uri));
    }

    /**
	 * Create a HTTP HEAD based {@link WebRequest} for the given string uri.
	 * 
	 * @param uri
	 *            the string uri for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest head(final String uri) {
        return new WebRequest(new HttpHead(uri));
    }

    /**
	 * Create a HTTP POST based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest post(final URI uri) {
        return new WebRequest(new HttpPost(uri));
    }

    /**
	 * Create a HTTP POST based {@link WebRequest} for the given string uri.
	 * 
	 * @param uri
	 *            the string uri for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest post(final String uri) {
        return new WebRequest(new HttpPost(uri));
    }

    /**
	 * Create a HTTP PUT based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest put(final URI uri) {
        return new WebRequest(new HttpPut(uri));
    }

    /**
	 * Create a HTTP PUT based {@link WebRequest} for the given string URI.
	 * 
	 * @param uri
	 *            the string uri for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest put(final String uri) {
        return new WebRequest(new HttpPut(uri));
    }

    /**
	 * Create a HTTP TRACE based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest trace(final URI uri) {
        return new WebRequest(new HttpTrace(uri));
    }

    /**
	 * Create a HTTP TRACE based {@link WebRequest} for the given string URI
	 * 
	 * @param uri
	 *            the string URI for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest trace(final String uri) {
        return new WebRequest(new HttpTrace(uri));
    }

    /**
	 * Create a HTTP DELETE based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest delete(final URI uri) {
        return new WebRequest(new HttpDelete(uri));
    }

    /**
	 * Create a HTTP DELETE based {@link WebRequest} for the given string URI
	 * 
	 * @param uri
	 *            the string URI for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest delete(final String uri) {
        return new WebRequest(new HttpDelete(uri));
    }

    /**
	 * Create a HTTP OPTIONS based {@link WebRequest} for the given {@link URI}
	 * 
	 * @param uri
	 *            the {@link URI} for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest options(final URI uri) {
        return new WebRequest(new HttpOptions(uri));
    }

    /**
     * Create a HTTP OPTIONS based {@link WebRequest} for the given string URI.
	 * 
	 * @param uri
	 *            the string URI for which to create web request
	 * 
	 * @return the {@link WebRequest} object thus created
     */
    public static WebRequest options(final String uri) {
        return new WebRequest(new HttpOptions(uri));
    }
    
    // Instance methods start here
    
    /**
     * The associated {@link HttpRequestBase}
     */
    private final HttpRequestBase request;

    /**
     * The associated {@link HttpParams}
     */
    private final HttpParams localParams;

    /**
     * The associated {@link DateFormat} formatter
     */
    private SimpleDateFormat dateFormatter;

    /**
	 * Create the {@link WebRequest} object using the given
	 * {@link HttpRequestBase} object.
	 * 
	 * @param request
	 *            the base http request
	 * 
	 */
    WebRequest(final HttpRequestBase request) {
        super();
        this.request = request;
        this.localParams = request.getParams();
    }

    /**
     * Get the underlying {@link HttpRequestBase} object.
     * 
     * @return the base http request
     */
    HttpRequestBase getHttpRequest() {
        return this.request;
    }
    
    /**
	 * Display the debug information for this request
	 * 
	 * @return this very {@link WebRequest}
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
     * @return string representation of HTTP verb such as GET, POST in uppercase
     */
    public String getVerb() {
    	return this.request.getMethod().toUpperCase();
    }
    
    /**
     * Return the URI associated with this request.
     * 
     * @return the {@link URI} for this request.
     * 
     */
    public URI getURI() {
    	return this.request.getURI();
    }
    
    /**
     * Return the type of request that we encapsulate.
     * 
     * @return the {@link WebRequestMethod} identifying request type.
     * 
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
	 * @return the obtained {@link WebRawResponse}.
	 * 
	 * @throws ClientProtocolException
	 *             if client protocol fails
	 * 
	 * @throws IOException
	 *             if something else fails
	 */
    public WebRawResponse execute() throws ClientProtocolException, IOException {
        return HttpExecutor.DEFAULT.execute(this);
    }

    /**
     * Abort this request now.
     * 
     * @throws UnsupportedOperationException if cannot abort
     */
    public void abort() throws UnsupportedOperationException {
        this.request.abort();
    }

    //// HTTP header operations

    /**
	 * Add the given header to the request
	 * 
	 * @param header
	 *            the {@link Header} object to use
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest addHeader(final Header header) {
        this.request.addHeader(header);
        return this;
    }

    /**
	 * Add a new header with the given name and value
	 * 
	 * @param name
	 *            the header name
	 * 
	 * @param value
	 *            the header value
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest addHeader(final String name, final String value) {
        this.request.addHeader(name, value);
        return this;
    }

    /**
	 * Remove the given header
	 * 
	 * @param header
	 *            the header name to remove
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest removeHeader(final Header header) {
        this.request.removeHeader(header);
        return this;
    }

    /**
	 * Remove all headers with the given name.
	 * 
	 * @param name
	 *            the header name to remove
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest removeHeaders(final String name) {
        this.request.removeHeaders(name);
        return this;
    }

    /**
	 * Set the request headers to the given list of headers.
	 * 
	 * @param headers
	 *            headers to set
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest setHeaders(final Header[] headers) {
        this.request.setHeaders(headers);
        return this;
    }

    /**
	 * Set cache-control as the given one.
	 * 
	 * @param cacheControl
	 *            the value to set
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest setCacheControl(String cacheControl) {
        this.request.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
        return this;
    }

    /**
	 * The associated {@link DateFormat} instance. If nothing is set, returns
	 * {@link SimpleDateFormat} for {@link #DATE_FORMAT} in {@link #DATE_LOCALE}
	 * and {@link #TIME_ZONE}.
	 * 
	 * @return the date format
	 */
    private DateFormat getDateFormat() {
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
	 *            the {@link Date} value for header
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest setDate(final Date date) {
        this.request.setHeader(HttpHeaders.DATE, getDateFormat().format(date));
        return this;
    }

    /**
	 * Set the If-Modified-Since header to the given date value.
	 * 
	 * @param date
	 *            the {@link Date} value for header
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest setIfModifiedSince(final Date date) {
        this.request.setHeader(HttpHeaders.IF_MODIFIED_SINCE, getDateFormat().format(date));
        return this;
    }

    /**
	 * Set the If-Unmodified-Since header to the given date value
	 * 
	 * @param date
	 *            the {@link Date} value for header
	 * 
	 * @return this very {@link WebRequest}
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
	 *            the param name
	 * 
	 * @param object
	 *            the value of the param
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest config(final String param, final Object object) {
        this.localParams.setParameter(param, object);
        return this;
    }

    /**
	 * Remove the local config param from this request with the given name
	 * 
	 * @param param
	 *            the param name to remove
	 * 
	 * @return this very {@link WebRequest}
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
	 *            {@link HttpVersion} to use
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest version(final HttpVersion version) {
        return config(CoreProtocolPNames.PROTOCOL_VERSION, version);
    }

    /**
	 * Set the {@link Charset} for the given request
	 * 
	 * @param charset
	 *            the {@link Charset} to use
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest elementCharset(final String charset) {
        return config(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, charset);
    }

    /**
     * Set the request to use expect continue.
     * 
     * @return this very {@link WebRequest}
     */
    public WebRequest useExpectContinue() {
        return config(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
    }

    /**
	 * Set the request User-Agent string to the given one.
	 * 
	 * @param agent
	 *            the user-agent string
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest userAgent(final String agent) {
        return config(CoreProtocolPNames.USER_AGENT, agent);
    }

    //// HTTP connection parameter operations
    
    /**
	 * Follow redirects for the request
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest followRedirects() {
    	HttpParams params = this.request.getParams();
    	params.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
    	return this;
    }

    /**
     * Do not follow any redirects
     * 
     * @return this very {@link WebRequest}
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
	 *            the timeout value
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest socketTimeout(int timeout) {
        return config(CoreConnectionPNames.SO_TIMEOUT, timeout);
    }
    
    /**
	 * Specify the connection time out to the given value.
	 * 
	 * @param timeout
	 *            the timeout value
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest connectTimeout(int timeout) {
        return config(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    /**
	 * Specifies if stale connection check needs to be performed before making a
	 * connection.
	 * 
	 * @param perform
	 *            <code>true</code>if stale connection checks needs to be
	 *            performed, <code>false</code> otherwise
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest staleConnectionCheck(boolean perform) {
        return config(CoreConnectionPNames.STALE_CONNECTION_CHECK, perform);
    }

    //// HTTP connection route operations

    /**
	 * Set the proxy via given http host.
	 * 
	 * @param proxy
	 *            the {@link HttpHost} to use for proxy
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest viaProxy(final HttpHost proxy) {
        return config(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    //// HTTP entity operations

    /**
	 * Set the body from given {@link HttpEntity}.
	 * 
	 * @param entity
	 *            the {@link HttpEntity} to set body from
	 * 
	 * @return this very {@link WebRequest}
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
	 * Set the body using FORM variables in given {@link Charset}
	 * 
	 * @param formParams
	 *            the form params
	 * 
	 * @param charset
	 *            the {@link Charset} for params
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyForm(final Iterable <? extends NameValuePair> formParams, final Charset charset) {
        return body(new UrlEncodedFormEntity(formParams, charset));
    }

    /**
	 * Set the body using FORM variables
	 * 
	 * @param formParams
	 *            the form params
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyForm(final Iterable <? extends NameValuePair> formParams) {
        return bodyForm(formParams, HTTP.DEF_CONTENT_CHARSET);
    }

    /**
	 * Set the body using FORM variables
	 * 
	 * @param formParams
	 *            the form parameters
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyForm(final NameValuePair... formParams) {
        return bodyForm(Arrays.asList(formParams), HTTP.DEF_CONTENT_CHARSET);
    }

    /**
	 * Set the body from the string for given content type.
	 * 
	 * @param string
	 *            the string to set body from
	 * 
	 * @param contentType
	 *            the {@link ContentType} of the string
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyString(final String string, final ContentType contentType) {
        return body(new StringEntity(string, contentType));
    }

    /**
	 * Set the body from given file for the given content type.
	 * 
	 * @param file
	 *            the {@link File} to read from
	 * 
	 * @param contentType
	 *            the {@link ContentType} of the file
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyFile(final File file, final ContentType contentType) {
        return body(new FileEntity(file, contentType));
    }

    /**
	 * Set the body from given byte array.
	 * 
	 * @param bytes
	 *            the byte array
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyByteArray(final byte[] bytes) {
        return body(new ByteArrayEntity(bytes));
    }

    /**
	 * Set the body from given byte array.
	 * 
	 * @param bytes
	 *            the byte array
	 * 
	 * @param offset
	 *            the offset to read from
	 * 
	 * @param length
	 *            the number of bytes to read
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyByteArray(final byte[] bytes, int offset, int length) {
        return body(new ByteArrayEntity(bytes, offset, length));
    }

    /**
	 * Set the body stream from given input stream.
	 * 
	 * @param instream
	 *            the {@link InputStream} to read from
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyStream(final InputStream instream) {
        return body(new InputStreamEntity(instream, -1));
    }

    /**
	 * Set the body stream from given input stream of given content type.
	 * 
	 * @param instream
	 *            the {@link InputStream} from which to read
	 * 
	 * @param contentType
	 *            the {@link ContentType} of the stream
	 * 
	 * @return this very {@link WebRequest}
	 */
    public WebRequest bodyStream(final InputStream instream, final ContentType contentType) {
        return body(new InputStreamEntity(instream, -1, contentType));
    }

	/**
	 * Change the cookie policy to given cookie policy name
	 * 
	 * @param cookiePolicy
	 *            the cookie policy name
	 * 
	 * @return this very {@link WebRequest}
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
