package com.chao.crawl.main;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chao.crawl.contant.Constant;
import com.chao.crawl.link.Links;
import com.chao.crawl.page.Page;
import com.chao.crawl.page.PageParser;
import com.chao.crawl.util.FileUtil;
import com.chao.crawl.util.HttpUtil;
import com.chao.crawl.util.ParserUtil;
import com.chao.crawl.util.ThreadPoolManager;

public class MyCrawler {

	public static final ExecutorService executor = ThreadPoolManager.getInstance();

	/**
	 *
	 * @param seeds
	 *            seeds url
	 * @return
	 */
	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++) {
			Links.addUnvisitedUrlQueue(seeds[i]);
		}
	}

	/**
	 * crawling process
	 *
	 * @param seeds
	 * @return
	 * @throws Exception
	 */
	public void crawling(String[] seeds) throws Exception {

		try {
			// init url links
			initCrawlerWithSeeds(seeds);

			// run for once to add unvisited url to the queue
			extractInfo();

			// run in thread pool
			 Links.unVisitedUrlQueue.parallelStream()
			 .forEach((link) -> CompletableFuture.runAsync(new CrawlThread(), executor));
		} finally {
			// shutdown executor
			executor.shutdown();
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		}
	}

	private void extractInfo() throws Exception {

		// pop first from link list
		String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();

		if (StringUtil.isBlank(visitUrl))
			return;

		Page page = HttpUtil.doRequest(visitUrl);

		ParserUtil.parseHtml(page);

		// save page to local
		FileUtil.saveToLocal(page);

		// add visited url to visited set
		Links.addVisitedUrlSet(visitUrl);

		/*
		 * get new links only crawl products shown on the page
		 */
		// Set<String> links = PageParser.getLinks(page, "a.product-image");

		// crawl other category and products shown on the page
		Set<String> links = PageParser.getLinks(page, "a");

		for (String link : links) {
			Links.addUnvisitedUrlQueue(link);
		}

	}

	public static void main(String[] args) throws Exception {
		
		MyCrawler crawler = new MyCrawler();
		crawler.crawling(new String[] { Constant.FEED_URL });
	}
}
