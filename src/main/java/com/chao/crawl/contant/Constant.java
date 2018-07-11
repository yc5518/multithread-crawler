package com.chao.crawl.contant;

public interface Constant {
	public static final int TIME_OUT = 30000;//30s
	
	public static final int MAX_URL_NUM = 1000;
	
	public static final int CPU_CORE_NUM = Runtime.getRuntime().availableProcessors();
	
	public static final double BLOCK_COEFFICIENT = 0.5;
	
	public static final String FEED_URL = "http://www.sli-demo.com/slim-fit-dobby-oxford-shirt-536.html";
	
	public static final int FIRST_SLASH_OF_DOMAIN = 7;
}
