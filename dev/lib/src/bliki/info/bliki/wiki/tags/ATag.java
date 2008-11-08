package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

import org.htmlcleaner.TagNode;

public class ATag extends HTMLTag {
	public ATag() {
		super("a");
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}

	// public boolean isAllowedAttribute(String attributeName) {
	// if (attributeName.equals("href") || attributeName.equals("title") ||
	// attributeName.equals("rel")) {
	// return true;
	// }
	// return false;
	// }

	public String getCloseTag() {
		return "</a>";
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		if (!converter.noLinks()) {
			super.renderHTML(converter, buf, model);
		} else {
			List children = getChildren();
			if (children.size() != 0) {
				converter.nodesToText(children, buf, model);
			}
		}
	}

	@Override
	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		TagNode node = this;
		List children = node.getChildren();
		if (children.size() != 0) {
			converter.nodesToText(children, _out, model);
		}
	}

	@Override
	public boolean addAttribute(String attName, String attValue, boolean checkXSS) {
		super.addAttribute(attName, attValue, checkXSS);
		if (attName != null && attValue != null && attName.equalsIgnoreCase("href")) {
			String valueLowerCased = attValue.trim().toLowerCase();
			if (valueLowerCased.startsWith("http:") || valueLowerCased.startsWith("https:") || valueLowerCased.startsWith("ftp:")
					|| valueLowerCased.startsWith("ftps:") || valueLowerCased.startsWith("mailto:")) {
				addAttribute("rel", "nofollow", true);
				return true;
			}
		}
		return false;
	}

}