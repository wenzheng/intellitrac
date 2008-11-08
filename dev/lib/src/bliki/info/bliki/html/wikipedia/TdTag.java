package info.bliki.html.wikipedia;

import org.htmlcleaner.TagNode;


public class TdTag extends AbstractHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n|");
	}

}
