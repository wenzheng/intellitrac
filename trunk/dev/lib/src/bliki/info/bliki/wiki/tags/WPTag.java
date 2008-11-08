package info.bliki.wiki.tags;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

/**
 * A special Wikipedia tag (i.e. ==, ===, ''', '', ...)
 * 
 */
public class WPTag extends HTMLTag {

	public WPTag(String htmlName) {
		super(htmlName);
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WPTag) {
			// distinguish wikipedia tags from other tags
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		if (getChildren().size() != 0) {
			super.renderHTML(converter, buf, model);
		}
	}

	@Override
	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		if (name.equals("i")) {
			_out.append(PropertyManager.get("Font.Italic.On") + "{}");
		} else if (name.equals("em")) {
			_out.append(PropertyManager.get("Font.Italic.On") + "{}");
		} else if (name.equals("b")) {
			_out.append(PropertyManager.get("Font.Bold.On") + "{}");
		} else if (name.equals("strong")) {
			_out.append(PropertyManager.get("Font.Bold.On") + "{}");
		} else if (name.equals("h1")) {
			_out.append(PropertyManager.get("Heading.3"));
		} else if (name.equals("h2")) {
			_out.append(PropertyManager.get("Heading.3"));
		} else if (name.equals("h3")) {
			_out.append(PropertyManager.get("Heading.2"));
		} else if (name.equals("h4")) {
			_out.append(PropertyManager.get("Heading.1"));
		} else if (name.equals("h5")) {
			_out.append(PropertyManager.get("Heading.0"));
		} else if (name.equals("h6")) {
			_out.append(PropertyManager.get("Heading.0"));
		}

		List children = getChildren();
		if (children.size() != 0) {
			converter.nodesToText(children, _out, model);
		}
		if (name.equals("i")) {
			_out.append(PropertyManager.get("Font.Italic.Off") + "{}");
		} else if (name.equals("em")) {
			_out.append(PropertyManager.get("Font.Italic.Off") + "{}");
		} else if (name.equals("b")) {
			_out.append(PropertyManager.get("Font.Bold.Off") + "{}");
		} else if (name.equals("strong")) {
			_out.append(PropertyManager.get("Font.Bold.Off") + "{}");
		} else if ((name.equals("h1")) || (name.equals("h2")) || (name.equals("h3")) || (name.equals("h4"))) {
			_out.append(PropertyManager.get("Heading.End"));
		} else if (name.equals("h5")) {
			_out.append(PropertyManager.get("Paragraph.End"));
		} else if (name.equals("h6")) {
			_out.append(PropertyManager.get("Paragraph.End"));
		}
	}
}