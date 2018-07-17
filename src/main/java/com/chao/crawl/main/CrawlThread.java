package com.chao.crawl.main;

import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chao.crawl.link.Links;
import com.chao.crawl.page.Page;
import com.chao.crawl.page.PageParser;
import com.chao.crawl.util.FileUtil;
import com.chao.crawl.util.HttpUtil;
import com.chao.crawl.util.ParserUtil;

public class CrawlThread implements Runnable {

	public void run() {

		// set loop limits
		while (!Links.unVisitedUrlQueueIsEmpty() && Links.getVisitedUrlNum() <= 1000) {

			// pop first from link list
			String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
			if (visitUrl == null) {
				continue;
			}
			
			Page page = null;
			try {
				/*
				 * for category structure of page
				 * http://www.sli-demo.com/accessories/eyewear/jackie-o-round-sunglasses.html,
				 * always having different html source between getting source by http request
				 * and by view source in browser, '<li class="category6"> <a
				 * href="http://www.sli-demo.com/accessories.html" title="">Accessories</a>
				 * <span>/ </span> </li> <li class="category18"> <a
				 * href="http://www.sli-demo.com/accessories/eyewear.html" title="">Eyewear</a>
				 * <span>/ </span>'
				 * 
				 * are omitted. trying to find out why
				 */
				page = HttpUtil.doRequest(visitUrl);

			} catch (Exception e) {
				// System.out.println("exception begin----" + visitUrl);
				// add failed url to unvisited
				Links.addUnvisitedUrlQueue(visitUrl);
				// System.out.println("exception ends");
				continue;

			}

			ParserUtil.parseHtml(page);
			
			// save page to local
			FileUtil.saveToLocal(page);

			// add visited url to visited set
			Links.addVisitedUrlSet(visitUrl);

			/*
			 * get new links only crawl products shown on the page
			 */
			Set<String> links = PageParser.getLinks(page, "a.product-image");

			// crawl other category and products shown on the page
			// Set<String> links = PageParser.getLinks(page, "a");

			for (String link : links) {
				Links.addUnvisitedUrlQueue(link);
			}

//			 int count = ((ThreadPoolExecutor) MyCrawler.executor).getActiveCount();
//			 System.out.println("unvisited: " + Links.unVisitedUrlQueue.size() +
//			 "-----size: " + Links.getVisitedUrlNum()
//			 + "---active:" + count);
		}
	}

}
