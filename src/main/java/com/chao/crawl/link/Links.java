package com.chao.crawl.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.chao.crawl.contant.Constant;

public class Links {

	//define filter, to filter urls inside the seed website
    private static LinkFilter filter = new LinkFilter() {
    	
        public boolean accept(String url) {
        	int index = Constant.FEED_URL.substring(Constant.FIRST_SLASH_OF_DOMAIN).indexOf("/");
        	String mainDomain = Constant.FEED_URL.substring(0, index + Constant.FIRST_SLASH_OF_DOMAIN);
        	
            if (url.startsWith(mainDomain))
                return true;
            else
                return false;
        }
    };
    
	 static //visited url
	Set<String> set = new HashSet<String>();
    public static Set<String> visitedUrlSet = Collections.synchronizedSet(set);

    //urls to be visited
    public static ConcurrentLinkedQueue<String> unVisitedUrlQueue = (new ConcurrentLinkedQueue<String>());

    //size of visited url
    public static int getVisitedUrlNum() {
        return visitedUrlSet.size();
    }

    //add to visited url
    public static void addVisitedUrlSet(String url) {
        visitedUrlSet.add(url);
    }

    //remove visited url
    public static void removeVisitedUrlSet(String url) {
        visitedUrlSet.remove(url);
    }



    //acquire unvisited url
    public static ConcurrentLinkedQueue<String> getUnVisitedUrlQueue() {
        return unVisitedUrlQueue;
    }

    //add to unvisited url, unique url
    public static void addUnvisitedUrlQueue(String url) {
        if (url != null && !url.trim().equals("")  && !visitedUrlSet.contains(url)  && !unVisitedUrlQueue.contains(url) && filter.accept(url)){
            unVisitedUrlQueue.add(url);
        }
    }

    //remove unvisited url
    public static String removeHeadOfUnVisitedUrlQueue() {
    	String url = unVisitedUrlQueue.peek();
    	unVisitedUrlQueue.remove();
        return url;
    }

    public static boolean unVisitedUrlQueueIsEmpty() {
        return unVisitedUrlQueue.isEmpty();
    }
    
}
