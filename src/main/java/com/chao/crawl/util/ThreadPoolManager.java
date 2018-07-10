package com.chao.crawl.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Singleton threadpool
public class ThreadPoolManager {
	private ThreadPoolManager() {}
	
	private static class ThreadPoolManagerHolder{
		
		static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(OptThreadNumUtil.getOptimalThreadNum());
		
	}
	
	public static ExecutorService getInstance() {
		
		return ThreadPoolManagerHolder.EXECUTOR;
	}
}	
