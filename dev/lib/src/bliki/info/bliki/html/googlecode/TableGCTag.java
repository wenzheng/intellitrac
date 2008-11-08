package info.bliki.html.googlecode;

import info.bliki.html.wikipedia.AbstractHTMLTag;

import org.htmlcleaner.TagNode;


public class TableGCTag extends AbstractHTMLTag {

	@Override
	public void close(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n");
	}
}
