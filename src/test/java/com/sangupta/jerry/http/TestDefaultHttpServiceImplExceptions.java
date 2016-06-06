package com.sangupta.jerry.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sangupta.jerry.constants.HttpMimeType;
import com.sangupta.jerry.http.service.HttpService;
import com.sangupta.jerry.http.service.impl.DefaultHttpServiceImpl;
import com.sangupta.jerry.unsafe.UnsafeMemory;

/**
 * Unit tests for {@link DefaultHttpServiceImpl}.
 * 
 * @author sangupta
 *
 */
public class TestDefaultHttpServiceImplExceptions {
	
	private static final String LOCAL_URL = "http://localhost:8080/hit";
	
	public Exception exception;
	
	private final HttpService service = new DefaultHttpServiceImpl() {
		
		@SuppressWarnings("restriction")
		public WebRequest getWebRequest(WebRequestMethod method, String uri) {
			if(exception != null) {
				UnsafeMemory.getUnsafe().throwException(exception);
				return null;
			}
			
			return super.getWebRequest(method, uri);
		}
		
	};
	
	@Before
	public void setup() {
		this.exception = null;
	}
	
	@Test
	public void testExceptions() {
		this.exception = new IOException();

		Assert.assertNull(this.service.getTextResponse(LOCAL_URL));
		Assert.assertNull(this.service.getResponse(LOCAL_URL));
		Assert.assertNull(this.service.getResponseHeaders(LOCAL_URL));
		Assert.assertNull(this.service.doHEAD(LOCAL_URL));
		Assert.assertNull(this.service.doGET(LOCAL_URL));
		Assert.assertNull(this.service.doPOST(LOCAL_URL, "help", HttpMimeType.TEXT_PLAIN));
		Assert.assertNull(this.service.doPUT(LOCAL_URL, "help", HttpMimeType.TEXT_PLAIN));
		Assert.assertNull(this.service.doOPTIONS(LOCAL_URL));
		Assert.assertNull(this.service.doTRACE(LOCAL_URL));
		Assert.assertNull(this.service.doDELETE(LOCAL_URL));
		
		Assert.assertNull(this.service.postXML(LOCAL_URL, null));
		Assert.assertNull(this.service.postJSON(LOCAL_URL, null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testException1() {
		this.service.executeSilently(null);
	}
	
	@Test
	public void testTimeouts() {
		this.service.setConnectionTimeout(200);
		this.service.setSocketTimeout(300);
		WebRequest request = this.service.getWebRequest(WebRequestMethod.GET, LOCAL_URL);
		try {
			request.execute();
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		
		Assert.assertEquals(200, request.getHttpRequest().getConfig().getConnectTimeout());
		Assert.assertEquals(300, request.getHttpRequest().getConfig().getSocketTimeout());
		
	}
	
	@Test
	public void testNullRequestExecute() {
		try {
			this.service.plainExecuteSilently(null);
			Assert.assertTrue(false);
		} catch(IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
		
		WebResponse response = this.service.plainExecuteSilently(new WebRequest(new HttpGet("http://localhost/hit")) {
			
			@Override
			public WebRawResponse execute() throws ClientProtocolException, IOException {
				throw new IOException();
			}
			
		});
		Assert.assertNull(response);
	}
}
