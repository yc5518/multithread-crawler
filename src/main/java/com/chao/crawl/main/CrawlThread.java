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

public class CrawlThread implements Runnable {

	public void run() {

		// set loop limits
		while (!Links.unVisitedUrlQueueIsEmpty() && Links.getVisitedUrlNum() <= 1000) {

			int count = ((ThreadPoolExecutor) MyCrawler.executor).getActiveCount();

			// pop first from link list
			String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
			if (visitUrl == null) {
				continue;
			}

			Page page = null;
			try {
				page = HttpUtil.doRequest(visitUrl);

			} catch (Exception e) {
				System.out.println("exception begin" + visitUrl);
				//add failed url to unvisited
				Links.addUnvisitedUrlQueue(visitUrl);
				System.out.println("exception ends");
				continue;
				
			}

			// find productId
			String productIdHtml = PageParser.select(page, "div.no-display").html();
			Element productIdEle = Jsoup.parseBodyFragment(productIdHtml);
			String productId = productIdEle.select("input[name=product]").attr("value");

			// find breadcrumbs
			Elements breadcrumbs = PageParser.select(page, "div.breadcrumbs ul li");

			String linkText = "";
			StringBuilder sb = new StringBuilder();
			for (Element path : breadcrumbs) {
				linkText = path.text();
				sb.append(" " + linkText);
			}

			// find title & price
			Elements title = PageParser.select(page, "title");
			Elements price = PageParser.select(page, "span[id=product-price-" + productId + "]");

			// find description
			Elements shortDesc = PageParser.select(page, "div.short-description");
			// output---------------
			if (!price.isEmpty()) {
				System.out.println("Title: " + title.text());
				System.out.println("Category path: " + sb.substring(1, sb.length()));
				System.out.println("Price: " + price.text());
				System.out.println("Short description: " + shortDesc.text());
//				System.out.println("url:-------" + visitUrl);
				System.out.println("--------------");
			}

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

//			System.out.println("unvisited: " + Links.unVisitedUrlQueue.size() + "-----size: " + Links.getVisitedUrlNum()
//					+ "---active:" + count);
		}
	}

}
