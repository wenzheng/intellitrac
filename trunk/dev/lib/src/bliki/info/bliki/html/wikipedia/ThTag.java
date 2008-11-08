package info.bliki.html.wikipedia;

import org.htmlcleaner.TagNode;


public class ThTag extends AbstractHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n!");
	}

}
