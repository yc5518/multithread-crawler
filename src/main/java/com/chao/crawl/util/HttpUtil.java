package com.chao.crawl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.chao.crawl.contant.Constant;
import com.chao.crawl.page.Page;

public class HttpUtil {
	public static Page doRequest(String url) throws Exception {
		Page page = null;

		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(Constant.TIME_OUT);

		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, Constant.TIME_OUT);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		// execute http request
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}

			InputStream inStream = getMethod.getResponseBodyAsStream();

			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = inStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}

			// process response
			byte[] responseBody = swapStream.toByteArray();
			String contentType = getMethod.getResponseHeader("Content-Type").getValue();

			page = new Page(responseBody, url, contentType); // encapsulate to page
		} catch (HttpException e) {
			System.out.println("Please check http address you provided!");
			throw e;
		} catch (IOException e) { // do not handle exception h
			throw e;
		} finally {
			// release connection
			getMethod.releaseConnection();
		}
		return page;
	}
}
