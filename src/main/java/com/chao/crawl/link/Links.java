package com.chao.crawl.link;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Links {

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
        if (url != null && !url.trim().equals("")  && !visitedUrlSet.contains(url)  && !unVisitedUrlQueue.contains(url)){
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
