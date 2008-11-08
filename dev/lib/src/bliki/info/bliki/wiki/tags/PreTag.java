package info.bliki.wiki.tags;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.LaTeXConverter;
import info.bliki.wiki.filter.WPTable;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Wiki tag for the HTML <code>pre</code> Tag.
 * 
 * @see WPPreTag
 */
public class PreTag extends HTMLBlockTag implements INoBodyParsingTag {// implements IPreBodyParsingTag {
	private final static Pattern NOWIKI_OPEN_PATTERN = Pattern.compile("\\<nowiki\\>", Pattern.CASE_INSENSITIVE);

	private final static Pattern NOWIKI_CLOSE_PATTERN = Pattern.compile("\\<\\/nowiki\\>", Pattern.CASE_INSENSITIVE);

	public PreTag() {
		super("pre", Configuration.SPECIAL_BLOCK_TAGS);
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		String content = getBodyString();
		if (content != null && content.length() > 0) {
			writer.append("\n<pre>");
			content = NOWIKI_OPEN_PATTERN.matcher(content).replaceAll("");
			content = NOWIKI_CLOSE_PATTERN.matcher(content).replaceAll("");
			NowikiTag.copyPre(content, writer, false);
			writer.append("</pre>");
		}
	}

	@Override
	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		String content = getBodyString();
		if (content != null && content.length() > 0) {
			boolean inTable = getParent() instanceof WPTable;
			if (inTable) {
				_out.append(PropertyManager.get("CodeBlock.inTable.Begin"));
				_out.append(LaTeXConverter.verbToText(content));
				_out.append(PropertyManager.get("CodeBlock.inTable.End"));
			} else {
				_out.append(PropertyManager.get("CodeBlock.Begin"));
				_out.append(content);
				_out.append(PropertyManager.get("CodeBlock.End"));
			}
		}
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}