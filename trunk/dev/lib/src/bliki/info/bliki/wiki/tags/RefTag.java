package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.IBodyTag;

import java.io.IOException;
import java.util.Map;

/**
 * Wiki tag for references &lt;ref&gt;reference text...&lt;/ref&gt;
 * 
 */
public class RefTag extends HTMLTag implements IBodyTag {

	public RefTag() {
		super("ref");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		StringBuilder buf = new StringBuilder();
		renderHTMLWithoutTag(converter, buf, model);
		Map<String, String> map = getAttributes();
		String value = (String) map.get("name");
		String[] offset = model.addToReferences(buf.toString(), value);// getBodyString());
		writer.append("<sup id=\"_ref-");
		if (offset[1] == null) {
			writer.append(offset[0]);
		} else {
			writer.append(offset[1]);
		}
		writer.append("\" class=\"reference\"><a href=\"#_note-");
		if (value == null) {
			writer.append(offset[0]);
		} else {
			writer.append(value);
		}
		writer.append("\" title=\"\">[");
		writer.append(offset[0]);
		writer.append("]</a></sup>");
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}
}