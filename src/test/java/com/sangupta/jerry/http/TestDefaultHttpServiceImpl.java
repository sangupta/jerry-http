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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpHeaderName;
import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.http.service.impl.DefaultHttpServiceImpl;
import com.sangupta.jerry.util.ByteArrayUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.jerry.util.HashUtils;
import com.sangupta.jerry.util.XStreamUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Unit tests for {@link DefaultHttpServiceImpl}.
 * 
 * @author sangupta
 *
 */
@SuppressWarnings("restriction")
public class TestDefaultHttpServiceImpl {
	
	private static HttpServer server;
	
	private static MyHandler handler;
	
	private static final String LOCAL_URL = "http://localhost:8080/hit";
	
	// prefix with one to take care of case-change during header transmission
	private final String RANDOM_STRING = "1" + HashUtils.getMD5Hex(ByteArrayUtils.getRandomBytes(1024));
	
	private final HttpService service = new DefaultHttpServiceImpl();
	
	private final int RESPONSE_CODE = 290;
	
	@BeforeClass
	public static void setup() throws IOException {
		server = HttpServer.create(new InetSocketAddress(8080), 0);
		handler = new MyHandler();
		server.createContext("/hit", handler);
		server.setExecutor(null); // creates a default executor
		server.start();
	}
	
	@AfterClass
	public static void shutdown() {
		server.stop(0); // 300 seconds, 5 minutes
	}
	
	@After
	public void tearDown() {
		handler.clear();
	}
	
	@Test
	public void testGetTextResponse() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		
		String response = service.getTextResponse(LOCAL_URL);
		Assert.assertEquals(RANDOM_STRING, response);
	}
	
	@Test
	public void testGetResponse() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		WebResponse result = service.getResponse(LOCAL_URL);
		
		Assert.assertEquals(RESPONSE_CODE, result.getResponseCode());
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
	}

	@Test
	public void testGetResponseHeaders() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		
		Map<String, String> result = service.getResponseHeaders(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(RANDOM_STRING, result.get(RANDOM_STRING));
	}
	
	@Test
	public void testDoHEAD() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doHEAD(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNull(result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoGET() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doGET(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoPOST() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		handler.checkBody(RANDOM_STRING);
		handler.checkMethod(WebRequestMethod.POST);
		
		WebResponse result = service.doPOST(LOCAL_URL, RANDOM_STRING, HttpMimeType.BINARY);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoPUT() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		handler.checkBody(RANDOM_STRING);
		handler.checkMethod(WebRequestMethod.PUT);
		
		WebResponse result = service.doPUT(LOCAL_URL, RANDOM_STRING, HttpMimeType.BINARY);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoPATCH() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		handler.checkBody(RANDOM_STRING);
		handler.checkMethod(WebRequestMethod.PATCH);
		
		WebResponse result = service.doPATCH(LOCAL_URL, RANDOM_STRING, HttpMimeType.BINARY);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoOPTIONS() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doOPTIONS(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoTRACE() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doTRACE(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testDoDELETE() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doDELETE(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testPostXML() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		handler.checkBody(XStreamUtils.getXStream().toXML(RANDOM_STRING));
		handler.checkMethod(WebRequestMethod.POST);
		
		WebResponse result = service.postXML(LOCAL_URL, RANDOM_STRING);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testPostJSONL() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		handler.checkBody(GsonUtils.getGson().toJson(RANDOM_STRING));
		handler.checkMethod(WebRequestMethod.POST);
		
		WebResponse result = service.postJSON(LOCAL_URL, RANDOM_STRING);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testExecuteSilently() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebRequest request = service.getWebRequest(WebRequestMethod.GET, LOCAL_URL);
		WebResponse result = service.executeSilently(request);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));		
	}
	
	@Test
	public void testPlainExecuteSilently() {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebRequest request = service.getWebRequest(WebRequestMethod.GET, LOCAL_URL);
		WebResponse result = service.plainExecuteSilently(request);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
	
	@Test
	public void testWriteToFile() throws IOException {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		File file = service.downloadToTempFile(LOCAL_URL);
		Assert.assertEquals(RANDOM_STRING, FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
	}
	
	@Test
	public void testWriteToGivenFile() throws IOException {
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		File file = File.createTempFile("test-jerry-http-", ".dat");
		service.downloadToFile(LOCAL_URL, file);
		Assert.assertEquals(RANDOM_STRING, FileUtils.readFileToString(file));
		
		FileUtils.deleteQuietly(file);
	}
	
	static class MyHandler implements HttpHandler {
		
		private int responseCode = -1;
		
		private String body = null;
		
		private final Map<String, String> headers = new HashMap<>();
		
		private String bodyToCheck;
		
		private WebRequestMethod method;
        
		@Override
        public void handle(HttpExchange httpExchange) throws IOException {
			this.addHeader(httpExchange, HttpHeaderName.CONTENT_TYPE, HttpMimeType.BINARY);
			
            if(!this.headers.isEmpty()) {
            	for(Entry<String, String> entry : this.headers.entrySet()) {
            		this.addHeader(httpExchange, entry.getKey(), entry.getValue());
            	}
            }
            
            String method = httpExchange.getRequestMethod();
            if(this.method != null) {
            	if(!this.method.toString().equalsIgnoreCase(method)) {
            		httpExchange.sendResponseHeaders(-1, 0);
            		return;
            	}
            }
            
            if(method.equalsIgnoreCase("head")) {
            	httpExchange.sendResponseHeaders(this.responseCode, 0);
            	return;
            }
            
            if(this.bodyToCheck != null) {
            	String myBody = IOUtils.toString(httpExchange.getRequestBody());
            	if(!this.bodyToCheck.equals(myBody)) {
            		httpExchange.sendResponseHeaders(-1, 0);
            	}
            }
        	
            httpExchange.sendResponseHeaders(this.responseCode, this.body.length());
        	
            OutputStream os = httpExchange.getResponseBody();
            os.write(this.body.getBytes());
            os.close();
        }

		public void checkMethod(WebRequestMethod method) {
			this.method = method;
		}

		public void checkBody(String body) {
			this.bodyToCheck = body;
		}

		public void setResponse(int responseCode, String message) {
			this.responseCode = responseCode;
			this.body = message;
		}
		
		public void setHeader(String name, String value) {
			this.headers.put(name, value);
		}
		
		public void clear() {
			this.responseCode = -1;
			this.body = null;
			this.headers.clear();
			this.bodyToCheck = null;
			this.method = null;
		}
		
		private void addHeader(HttpExchange httpExchange, String name, String value) {
			List<String> list = new ArrayList<>();
    		list.add(value);
    		httpExchange.getResponseHeaders().put(name, list);
		}
    }
	
}
