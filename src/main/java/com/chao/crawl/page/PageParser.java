package com.chao.crawl.page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PageParser {
	/* select by selector */
	public static Elements select(Page page, String cssSelector) {
		return page.getDoc().select(cssSelector);
	}

	/**
	 * Get specific element by css selector
	 *
	 */
	public static Element select(Page page, String cssSelector, int index) {
		Elements eles = select(page, cssSelector);
		int realIndex = index;
		if (index < 0) {
			realIndex = eles.size() + index;
		}
		return eles.get(realIndex);
	}

	/**
	 * get links from css selector
	 * 
	 * @param cssSelector
	 * @return
	 */
	public static Set<String> getLinks(Page page, String cssSelector) {
		Set<String> links = new HashSet<String>();
		Elements es = select(page, cssSelector);
		Iterator iterator = es.iterator();
		while(iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if ( element.hasAttr("href") && isStaticPage(element)) {
                links.add(element.attr("abs:href"));
            }else if( element.hasAttr("src") && isStaticPage(element)){
                links.add(element.attr("abs:src"));
            }
        }
		return links;
	}
	
    private static boolean isStaticPage(Element element) {
    	if(element.attr("abs:href").startsWith("http:") && element.attr("abs:href").endsWith(".html")) {
    		return true;
    	}
    	
    	return false;
    }

	/**
	 * get attributes from css selector
	 * 
	 * @param cssSelector
	 * @param attrName
	 * @return
	 */
	public static ArrayList<String> getAttrs(Page page, String cssSelector, String attrName) {
		ArrayList<String> result = new ArrayList<String>();
		Elements eles = select(page, cssSelector);
		for (Element ele : eles) {
			if (ele.hasAttr(attrName)) {
				result.add(ele.attr(attrName));
			}
		}
		return result;
	}
}
