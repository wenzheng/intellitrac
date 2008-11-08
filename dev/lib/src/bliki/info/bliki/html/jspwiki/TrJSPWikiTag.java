package info.bliki.html.jspwiki;

import info.bliki.html.wikipedia.AbstractHTMLTag;

import org.htmlcleaner.TagNode;


public class TrJSPWikiTag extends AbstractHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n");
	}

}
