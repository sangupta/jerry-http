package com.sangupta.jerry.http.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.http.service.impl.DefaultHttpServiceImpl;
import com.sangupta.jerry.util.ByteArrayUtils;
import com.sangupta.jerry.util.HashUtils;
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
	
	private final String RANDOM_STRING = HashUtils.getMD5Hex(ByteArrayUtils.getRandomBytes(1024));
	
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
	
	@Test
	public void testGetTextResponse() {
		handler.clear();
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		
		String response = service.getTextResponse(LOCAL_URL);
		Assert.assertEquals(RANDOM_STRING, response);
	}
	
	@Test
	public void testGetResponse() {
		handler.clear();
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		WebResponse result = service.getResponse(LOCAL_URL);
		
		Assert.assertEquals(RESPONSE_CODE, result.getResponseCode());
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getContent());
		Assert.assertEquals(RANDOM_STRING, result.getContent());
	}

	@Test
	public void testGetResponseHeaders() {
		handler.clear();
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);
		
		Map<String, String> result = service.getResponseHeaders(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		
		Assert.assertEquals(RANDOM_STRING, result.get(RANDOM_STRING));
	}
	
	@Test
	public void testDoHEAD() {
		handler.clear();
		handler.setResponse(RESPONSE_CODE, RANDOM_STRING);
		handler.setHeader(RANDOM_STRING, RANDOM_STRING);

		WebResponse result = service.doHEAD(LOCAL_URL);
		
		Assert.assertNotNull(result);
		Assert.assertNull(result.getContent());
		Assert.assertNotNull(result.getHeaders());
		Assert.assertEquals(2, result.getHeaders().size());
		Assert.assertEquals(RANDOM_STRING, result.getHeaders().get(RANDOM_STRING));
	}
//	
//	@Test
//	public void testDoGET() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doGET(LOCAL_URL);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testDoPOST() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doPOST(LOCAL_URL, RANDOM_STRING, HttpMimeType.BINARY);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testDoPUT() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doPUT(LOCAL_URL, RANDOM_STRING, HttpMimeType.BINARY);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testDoOPTIONS() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doOPTIONS(LOCAL_URL);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testDoTRACE() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doTRACE(LOCAL_URL);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testDoDELETE() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.doDELETE(LOCAL_URL);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testPostXML() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.postXML(LOCAL_URL, RANDOM_STRING);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testPostJSONL() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.postJSON(LOCAL_URL, RANDOM_STRING);
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testExecuteSilently() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.executeSilently(service.getWebRequest(WebRequestMethod.GET, LOCAL_URL));
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
//	
//	@Test
//	public void testPlainExecuteSilently() {
//		MockWebResponse response = new MockWebResponse(RANDOM_STRING);
//		response.addHeader(RANDOM_STRING, RANDOM_STRING);
//		service.setNextResponse(response);
//		WebResponse result = service.plainExecuteSilently(service.getWebRequest(WebRequestMethod.GET, LOCAL_URL));
//		
//		Assert.assertNotNull(result);
//		Assert.assertEquals(response, result);
//	}
	
	static class MyHandler implements HttpHandler {
		
		private int responseCode = -1;
		
		private String body = null;
		
		private final Map<String, String> headers = new HashMap<>();
        
		@Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if(!this.headers.isEmpty()) {
            	for(Entry<String, String> entry : this.headers.entrySet()) {
            		List<String> list = new ArrayList<>();
            		list.add(entry.getValue());
            		httpExchange.getResponseHeaders().put(entry.getKey(), list);
            	}
            }
            
            httpExchange.sendResponseHeaders(this.responseCode, this.body.length());
            
            OutputStream os = httpExchange.getResponseBody();
            os.write(this.body.getBytes());
            os.close();
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
		}
    }
	
}
