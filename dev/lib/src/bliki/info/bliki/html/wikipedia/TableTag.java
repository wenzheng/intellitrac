package info.bliki.html.wikipedia;

import org.htmlcleaner.TagNode;


public class TableTag extends AbstractHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n{| border=\"1\"");
	}

	@Override
	public void close(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("\n|}");
	}
}
