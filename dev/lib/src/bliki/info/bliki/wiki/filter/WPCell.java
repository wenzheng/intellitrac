package info.bliki.wiki.filter;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.BrTag;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.util.TagStack;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.Utils;

/**
 * Represents a single cell in a wiki table (i.e. table syntax bordered by
 * <code>{| ..... |}</code> ). See: <a
 * href="http://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
 * 
 */
public class WPCell {
	public static final int ALIGN_NOT_SET = 0;

	public static final int ALIGN_LEFT = 1;

	public static final int ALIGN_RIGHT = 2;

	public static final int ALIGN_CENTER = 3;

	public static final int ALIGN_JUSTIFY = 4;

	int fStartPos;

	int fEndPos;

	/**
	 * The &gt;td&lt; tag should be used
	 */
	public final static int DEFAULT = 1;

	/**
	 * The &gt;th&lt; tag should be used
	 */
	public final static int TH = 2;

	/**
	 * The &gt;caption&lt; tag should be used
	 */
	public final static int CAPTION = 4;

	/**
	 * No table cell tag specified in the wiki table
	 */
	public final static int UNDEFINED = 8;

	private int fType;

	private int fAlign;

	private char[] fSource;

	private TagStack fStack;

	private Map<String, String> fAttributes;

	public WPCell(int start) {
		fStartPos = start;
		fType = DEFAULT;
		fAlign = ALIGN_NOT_SET;
		fStack = null;
		fAttributes = null;
		fSource = null;
	}

	/**
	 * @return Returns the endPos.
	 */
	public int getEndPos() {
		return fEndPos;
	}

	/**
	 * Create the internal TagNodes stack for a single table cell
	 * 
	 * @param endPos
	 *          The endPos to set.
	 */
	public void createTagStack(WPTable parent, String src, IWikiModel wikiModel, int endPos) {
		fEndPos = endPos;
		if (fEndPos > fStartPos) {
			fSource = src.toCharArray();
			String content;
			String params = null;
			WikipediaScanner scan = new WikipediaScanner(src, fStartPos);
			int index = scan.indexOfAttributes();
			if (index == (-1) || index >= fEndPos) {
				content = new String(fSource, fStartPos, fEndPos - fStartPos);
			} else {
				content = new String(fSource, index + 1, fEndPos - index - 1);
				params = new String(fSource, fStartPos, index - fStartPos);
			}

			fAttributes = Util.getAttributes(params);
			String rawWikiText = Utils.ltrimNewline(content);
			AbstractParser parser = wikiModel.createNewInstance(rawWikiText);
			fStack = parser.parseRecursiveInternal(wikiModel, true, false);
			List<BaseToken> list = fStack.getNodeList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof TagNode) {
					((TagNode) list.get(i)).setParent(parent);
				}
			}
		}
	}

	/**
	 * @return Returns the startPos.
	 */
	public int getStartPos() {
		return fStartPos;
	}

	/**
	 * @param startPos
	 *          The startPos to set.
	 */
	public void setStartPos(int startPos) {
		fStartPos = startPos;
	}

	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (fType != UNDEFINED) {
			if (HTMLTag.NEW_LINES) {
				if (fType == CAPTION) {
					buf.append("\n<caption");
				} else if (fType == TH) {
					buf.append("\n<th");
				} else {
					buf.append("\n<td");
				}
			} else {
				if (fType == CAPTION) {
					buf.append("<caption");
				} else if (fType == TH) {
					buf.append("<th");
				} else {
					buf.append("<td");
				}
			}

			HTMLTag.appendEscapedAttributes(buf, fAttributes);
		}

		if (fStack != null) {

			List<BaseToken> list = fStack.getNodeList();
			if (list.isEmpty()) {
				buf.append(" />");
			} else {
				buf.append(">");
				converter.nodesToText(fStack.getNodeList(), buf, wikiModel);
				if (fType != UNDEFINED) {
					if (fType == CAPTION) {
						buf.append("</caption>");
					} else if (fType == TH) {
						buf.append("</th>");
					} else {
						buf.append("</td>");
					}
				}
			}
		} else {
			if (fType != UNDEFINED) {
				buf.append(">"); 
				if (fType == CAPTION) {
					buf.append("</caption>");
				} else if (fType == TH) {
					buf.append("</th>");
				} else {
					buf.append("</td>");
				}
			}
		}
	}

	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel wikiModel) throws IOException {
		if (fStack != null) {
			boolean hasLBrks = false;
			List<BaseToken> list = fStack.getNodeList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof BrTag) {
					hasLBrks = true;
					break; // for
				}
			}

			if (fType == TH) {
				_out.append(PropertyManager.get("Table.HeaderCell.Begin"));
			}
			if (hasLBrks) {
				_out.append(PropertyManager.get("Table.CellWithLBreaks.Begin"));
			}
			converter.nodesToText(fStack.getNodeList(), _out, wikiModel);

			if (hasLBrks) {
				_out.append(PropertyManager.get("Table.CellWithLBreaks.End"));
			}
			if (fType == TH) {
				_out.append(PropertyManager.get("Table.HeaderCell.End"));
			}

		}
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return fType;
	}

	public int getAlign() {
		return fAlign;
	}

	/**
	 * @param type
	 *          The type to set.
	 */
	public void setType(int type) {
		fType = type;
	}

	public TagStack getTagStack() {
		return fStack;
	}

	public Map<String, String> getNodeAttributes() {
		return fAttributes;
	}
}