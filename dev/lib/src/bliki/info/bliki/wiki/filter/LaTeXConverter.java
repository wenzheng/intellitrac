package info.bliki.wiki.filter;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.tags.BrTag;
import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.ContentToken;
import org.htmlcleaner.EndTagToken;
import org.htmlcleaner.TagNode;

/**
 * A converter which renders the internal node representation as LaTeX text
 * 
 */
public class LaTeXConverter implements ITextConverter {

	public static final String TEMP_BRACES = "TEMP-BRACES";

	/**
	 * Escaped characters which can be used inside LaTeX
	 */
	static final String[] CHAR_REPLACEMENTS = new String[] { "\\textbackslash" + TEMP_BRACES, "\\textasciitilde" + TEMP_BRACES,
			"\\textasciicircum" + TEMP_BRACES, "\\textbar" + TEMP_BRACES, "\\textless" + TEMP_BRACES, "\\textgreater" + TEMP_BRACES,
			"\\$", "$\\{$", "$\\}$", "\\%", "\\&", "\\#", "\\_{}", "''" };

	/**
	 * Original chars which have to be escaped, because they have a special
	 * meaning in LaTeX
	 */
	static final String CHARS_TO_REPLACE = "\\~^|<>${}%&#_\"";

	static final String[] STRING_REPLACEMENTS = new String[] { "\\dots{}" };

	/** replacements for whole Strings */
	static final String[] STRINGS_TO_REPLACE = new String[] { "..." };

	public LaTeXConverter() {
	}

	public void nodesToText(List<BaseToken> nodes, Appendable resultBuffer, IWikiModel model) throws IOException {
		if (nodes != null && !nodes.isEmpty()) {
			Iterator childrenIt = nodes.iterator();
			while (childrenIt.hasNext()) {
				Object item = childrenIt.next();
				if (item != null) {
					if (item instanceof List) {
						nodesToText((List) item, resultBuffer, model);
					} else if (item instanceof ContentToken) {
						ContentToken contentToken = (ContentToken) item;
						resultBuffer.append(texEscapeString(contentToken.getContent()));
					} else if (item instanceof HTMLTag) {
						((HTMLTag) item).renderLaTeX(this, resultBuffer, model);
					} else if (item instanceof TagNode) {
						TagNode node = (TagNode) item;
						Map map = node.getObjectAttributes();
						if (map != null && map.size() > 0) {
							Object attValue = map.get("wikiobject");
							if (attValue instanceof ImageFormat) {
								// imageNodeToText(node, (ImageFormat) attValue, resultBuffer,
								// model);
							}
						} else {
							nodeToLaTeX(node, resultBuffer, model);
						}
					} else if (item instanceof EndTagToken) {
						//
						if (item instanceof BrTag) {
							resultBuffer.append(PropertyManager.get("LineBreak"));
						}
					}
				}
			}
		}
	}

	protected void nodeToLaTeX(TagNode node, Appendable resultBuffer, IWikiModel model) throws IOException {
		String name = node.getName();
		if (HTMLTag.NEW_LINES) {
			if (name.equals("div") || name.equals("p") || name.equals("table") || name.equals("ul") || name.equals("ol")
					|| name.equals("li") || name.equals("th") || name.equals("tr") || name.equals("td") || name.equals("pre")) {
				resultBuffer.append('\n');
			}
		}

		List children = node.getChildren();
		if (children.size() != 0) {
			nodesToText(children, resultBuffer, model);
		}
	}

	public void imageNodeToText(TagNode imageTagNode, ImageFormat imageFormat, Appendable resultBuffer, IWikiModel model)
			throws IOException {
		Map<String, String> map = imageTagNode.getAttributes();
		String caption = imageFormat.getCaption();
		String location = imageFormat.getLocation();
		String type = imageFormat.getType();
		int pxSize = imageFormat.getSize();
		if (pxSize != -1) {
			resultBuffer.append("<div style=\"");
			resultBuffer.append("width:");
			resultBuffer.append(Integer.toString(pxSize));
			resultBuffer.append("px");
			resultBuffer.append("\">");
		}
		resultBuffer.append("<a class=\"internal\" href=\"");
		resultBuffer.append(map.get("href").toString());
		resultBuffer.append("\" title=\"");
		resultBuffer.append(caption);
		resultBuffer.append("\">");

		resultBuffer.append("<img src=\"");
		resultBuffer.append(map.get("src").toString());
		resultBuffer.append("\"");

		if (caption != null && caption.length() > 0) {
			resultBuffer.append(" alt=\"").append(caption).append("\"");
			resultBuffer.append(" title=\"").append(caption).append("\"");
		}

		resultBuffer.append(" class=\"");
		if (location != null) {
			resultBuffer.append(" location-").append(location);
		}
		if (type != null) {
			resultBuffer.append(" type-").append(type);
		}
		resultBuffer.append("\"");

		if (pxSize != -1) {
			resultBuffer.append(" width=\"").append(Integer.toString(pxSize)).append("px\"");
		}
		resultBuffer.append(" />\n");

		resultBuffer.append("</a>");
		List children = imageTagNode.getChildren();
		if (children.size() != 0) {
			nodesToText(children, resultBuffer, model);
		}

		if (pxSize != -1) {
			resultBuffer.append("</div>\n");
		}
	}

	/**
	 * @return The String 's' with all special characters escaped and linebreaks
	 *         converted to "\\"
	 */
	public static String verbToText(String s) {
		String text = texEscapeString(s);
		text = text.replace("\r", "");
		text = text.replace("\n", PropertyManager.get("LineBreak"));
		return text;
	}

	/**
	 * Escapes characters or whole strings that otherwise have a special meaning
	 * in tex.
	 */
	public static String texEscapeString(String s) {
		for (int i = 0; i < LaTeXConverter.CHARS_TO_REPLACE.length(); i++) {
			s = s.replace(LaTeXConverter.CHARS_TO_REPLACE.charAt(i) + "", LaTeXConverter.CHAR_REPLACEMENTS[i]);
		}
		for (int i = 0; i < LaTeXConverter.STRINGS_TO_REPLACE.length; i++) {
			s = s.replace(LaTeXConverter.STRINGS_TO_REPLACE[i], LaTeXConverter.STRING_REPLACEMENTS[i]);
		}
		s = s.replace(LaTeXConverter.TEMP_BRACES, "{}");
		return s;
	}

	public boolean noLinks() {
		return true;
	}
}
