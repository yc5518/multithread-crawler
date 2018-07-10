package com.chao.crawl.util;

import com.chao.crawl.contant.Constant;

//get optimum number of threads
public class OptThreadNumUtil {
	
	public static int getOptimalThreadNum() {
		return new Double(Constant.CPU_CORE_NUM / (1 - Constant.BLOCK_COEFFICIENT)).intValue() + 1;
	}
}
