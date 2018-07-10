package com.chao.crawl.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chao.crawl.page.Page;

public class FileUtil {
	 private static String dirPath;

	    /**
	     * getMethod.getResponseHeader("Content-Type").getValue()
	     * 
	     */
	    private static String getFileNameByUrl(String url, String contentType) {
	        //eliminate http://
	        url = url.substring(7);
	        //text/html 
	        if (contentType.indexOf("html") != -1) {
	            url = url.replaceAll("[\\?/:*|<>\"]", "_");
	            return url;
	        }
	        // application/pdf 
	        else {
	            return url.replaceAll("[\\?/:*|<>\"]", "_") + "." +
	                    contentType.substring(contentType.lastIndexOf("/") + 1);
	        }
	    }

	    /*
	    *  produce directory
	    * */
	    private static void mkdir() {
	        if (dirPath == null) {
	            dirPath = Class.class.getClass().getResource("/").getPath() + "temp\\";
	        }
	        File fileDir = new File(dirPath);
	        if (!fileDir.exists()) {
	            fileDir.mkdir();
	        }
	    }

	    /**
	     * save to local
	     */

	    public static void saveToLocal(Page page) {
	        mkdir();
	        String fileName =  getFileNameByUrl(page.getUrl(), page.getContentType()) ;
	        String filePath = dirPath + fileName ;
	        byte[] data = page.getContent();
	        try {
	            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
	            for (int i = 0; i < data.length; i++) {
	                out.write(data[i]);
	            }
	            out.flush();
	            out.close();
//	            System.out.println(filePath);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
