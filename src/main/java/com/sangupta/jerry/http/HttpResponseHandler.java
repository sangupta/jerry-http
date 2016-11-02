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
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.protocol.HttpContext;

/**
 * Handler that encapsulates the process of generating a response object
 * from a {@link HttpResponse}.
 *
 * This has been modified from {@link ResponseHandler} to include the
 * passage of {@link HttpContext} (the local context) that is required to
 * fetch the redirect chain in case the original URI redirected
 * 
 * @author sangupta
 * 
 */
public interface HttpResponseHandler {

    /**
	 * Processes an {@link HttpResponse} and returns some value corresponding to
	 * that response.
	 * 
	 * @param response
	 *            The response to process
	 * 
	 * @return A value determined by the response
	 * 
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 * 
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 */
    WebResponse handleResponse(URI originalURI, HttpResponse response, HttpContext httpContext) throws ClientProtocolException, IOException;

}
