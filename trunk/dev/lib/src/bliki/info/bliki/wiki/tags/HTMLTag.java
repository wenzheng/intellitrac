package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;

public class HTMLTag extends TagNode {
	public final static boolean NEW_LINES = true;

	public HTMLTag(String name) {
		super(name);
	}

	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		TagNode node = this;
		String name = node.getName();
		if (NEW_LINES) {
			if (name.equals("div") || name.equals("p") || name.equals("table") || name.equals("ul") || name.equals("ol")
					|| name.equals("li") || name.equals("th") || name.equals("tr") || name.equals("td") || name.equals("pre")) {
				buf.append('\n');
			}
		}
		buf.append('<');
		buf.append(name);

		Map<String, String> tagAtttributes = node.getAttributes();

		appendAttributes(buf, tagAtttributes);
		List children = node.getChildren();
		if (children.size() == 0) {
			buf.append(" />");
		} else {
			buf.append('>');

			converter.nodesToText(children, buf, model);

			buf.append("</");
			buf.append(node.getName());
			buf.append('>');
		}
	}

	public void renderHTMLWithoutTag(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		TagNode node = this;
		List children = node.getChildren();
		if (children.size() != 0) {
			converter.nodesToText(children, buf, model);
		}
	}

	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		TagNode node = this;
		String name = node.getName();
		if (NEW_LINES) {
			if (name.equals("div") || name.equals("p") || name.equals("table") || name.equals("ul") || name.equals("ol")
					|| name.equals("li") || name.equals("th") || name.equals("tr") || name.equals("td") || name.equals("pre")) {
				_out.append('\n');
			}
		}
		// Map tagAtttributes = node.getAttributes();
		//
		// appendAttributes(buf, tagAtttributes);
		List children = node.getChildren();
		if (children.size() != 0) {
			converter.nodesToText(children, _out, model);
		}
	}

	public void setTemplate(boolean isTemplate) {
	}

	public void appendAttributes(Appendable buf, Map<String, String> tagAtttributes) throws IOException {
		appendEscapedAttributes(buf, tagAtttributes);
	}

	public static void appendEscapedAttributes(Appendable buf, Map<String, String> tagAtttributes) throws IOException {
		if (tagAtttributes != null) {
			for (Map.Entry<String, String> currEntry : tagAtttributes.entrySet()) {
				String attName = currEntry.getKey();
				if (attName.length() >= 1 && Character.isLetter(attName.charAt(0))) {
					String attValue = currEntry.getValue();
					buf.append(" " + attName + "=\"" + Utils.escapeXml(attValue, false, false, false) + "\"");
				}
			}
		}
	}

	public static void appendUnescapedAttributes(Appendable buf, Map<String, String> tagAtttributes) throws IOException {
		if (tagAtttributes != null) {
			for (Map.Entry<String, String> currEntry : tagAtttributes.entrySet()) {
				String attName = currEntry.getKey();
				if (attName.length() >= 1 && Character.isLetter(attName.charAt(0))) {
					String attValue = currEntry.getValue();

					buf.append(" " + attName + "=\"" + attValue + "\"");
				}
			}
		}
	}
}