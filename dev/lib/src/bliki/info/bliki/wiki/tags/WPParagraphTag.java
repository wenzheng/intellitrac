package info.bliki.wiki.tags;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

public class WPParagraphTag extends WPTag {
	public WPParagraphTag() {
		super("p");
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}

	@Override
	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		List children = getChildren();
		if (children.size() != 0) {
			converter.nodesToText(children, _out, model);
			_out.append(PropertyManager.get("Paragraph.End"));
		}
	}

}