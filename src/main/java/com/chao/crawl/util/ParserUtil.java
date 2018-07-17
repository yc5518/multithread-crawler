package com.chao.crawl.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.chao.crawl.page.Page;
import com.chao.crawl.page.PageParser;

public class ParserUtil {
	
	public static void parseHtml(Page page) {
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
					System.out.println("--------------");
				}
	}
}
