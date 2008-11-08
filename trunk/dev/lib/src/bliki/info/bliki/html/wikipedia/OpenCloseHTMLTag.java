package info.bliki.html.wikipedia;

import java.util.Iterator;
import java.util.Map;

import org.htmlcleaner.TagNode;

public class OpenCloseHTMLTag extends OpenCloseTag {
	public OpenCloseHTMLTag(String opener, String closer) {
		super(opener, closer);
	}

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append(openStr);

		Map<String, String> tagAtttributes = node.getAttributes();
		boolean first = true;
		for (Map.Entry<String, String> currEntry : tagAtttributes.entrySet()) {
			if (first) {
				resultBuffer.append(" ");
				first = false;
			}
			String attName = currEntry.getKey();
			if (attName.length() >= 1 && Character.isLetter(attName.charAt(0))) {
				String attValue = currEntry.getValue();

				resultBuffer.append(" " + attName + "=\"" + attValue + "\"");
			}
		}
		resultBuffer.append(">");
	}

}
