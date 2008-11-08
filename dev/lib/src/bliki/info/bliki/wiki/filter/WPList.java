package info.bliki.wiki.filter;

import info.bliki.latex.PropertyManager;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wikipedia list
 *  
 */
public class WPList extends WPTag {

	private char[] fLastSequence;

	private InternalList fNestedElements;

	private ArrayList<InternalList> fInternalListStack;

	private static class InternalList extends ArrayList<Object> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3760843632697162014L;

		char fChar;

		public InternalList(char ch) {
			super();
			fChar = ch;
		}
	}

	public WPList() {
		super("*#");
		fLastSequence = null;
		fNestedElements = null;
		fInternalListStack = new ArrayList<InternalList>();
	}

	public boolean isEmpty() {
		return fNestedElements == null;
	}

	/**
	 * 
	 * @param listElement
	 * @return
	 */
	public boolean add(WPListElement listElement) {
		char[] sequence = listElement.getSequence();
		int s1Length = 0;
		int s2Length = sequence.length;
		if (fLastSequence != null) {
			s1Length = fLastSequence.length;
		} else {
			fNestedElements = new InternalList(sequence[0]);
			fInternalListStack.add(fNestedElements);
		}
		InternalList list;
		int min = 0;
		int level = 0;
		if (s1Length > s2Length) {
			min = s2Length;
		} else {
			min = s1Length;
		}
		level = min;
		for (int i = 0; i < min; i++) {
			if (sequence[i] != fLastSequence[i]) {
				level = i;
				break;
			}
		}

		popListStack(level);

		if (level < s2Length) {
			// push stack
			for (int i = level; i < s2Length; i++) {
				list = new InternalList(sequence[i]);
				((List) fInternalListStack.get(fInternalListStack.size() - 1)).add(list);
				fInternalListStack.add(list);
			}
		}
		((List) fInternalListStack.get(fInternalListStack.size() - 1)).add(listElement);

		fLastSequence = sequence;
		return true;
	}

	private void popListStack(int level) {
		if (fInternalListStack.size() > level) {
			for (int i = fInternalListStack.size() - 1; i > level; i--) {
				fInternalListStack.remove(i);
			}
		}
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (!isEmpty()) {
			fInternalListStack = null;

			for (int i = 0; i < fNestedElements.size(); i++) {
				Object element = fNestedElements.get(i);
				if (element instanceof InternalList) {
					InternalList subList = (InternalList) element;
					beginHTMLTag(buf, subList);
					renderSubListHTML(subList, converter, buf, wikiModel);
					if (subList.fChar == '*') {
						// bullet list
						buf.append("</ul>");
					} else {
						// numbered list
						buf.append("</ol>");
					}
				} else {
					TagStack stack = ((WPListElement) element).getTagStack();
					if (stack != null) {
						converter.nodesToText(stack.getNodeList(), buf, wikiModel);
					}
					// wikiModel.appendStack(((WPListElement)
					// element).getTagStack());
				}
			}
		}
	}

	private void beginHTMLTag(Appendable buf, InternalList subList) throws IOException {
		if (NEW_LINES) {
			if (subList.fChar == '*') {
				// bullet list
				buf.append("\n<ul>");
			} else {
				// numbered list
				buf.append("\n<ol>");
			}
		} else {
			if (subList.fChar == '*') {
				// bullet list
				buf.append("<ul>");
			} else {
				// numbered list
				buf.append("<ol>");
			}
		}
	}

	private void endHTMLTag(Appendable buf, InternalList subList) throws IOException {
		if (subList.fChar == '*') {
			// bullet list
			buf.append("</ul>");
		} else {
			// numbered list
			buf.append("</ol>");
		}
	}

	private void renderSubListHTML(InternalList list, ITextConverter converter, Appendable buf, IWikiModel wikiModel)
			throws IOException {
		if (list.size() > 0) {
			buf.append("\n<li>");
		}

		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				beginHTMLTag(buf, subList);
				// recursive call:
				renderSubListHTML(subList, converter, buf, wikiModel);
				endHTMLTag(buf, subList);
			} else {
				TagStack stack = ((WPListElement) element).getTagStack();
				if (stack != null) {
					converter.nodesToText(stack.getNodeList(), buf, wikiModel);
				}
				// wikiModel.appendStack(((WPListElement)
				// element).getTagStack());
			}

			if ((i < list.size() - 1) && list.get(i + 1) instanceof WPListElement) {
				if (NEW_LINES) {
					buf.append("</li>\n<li>");
				} else {
					buf.append("</li><li>");
				}
			}

		}
		if (list.size() > 0) {
			buf.append("</li>");
		}
	}

	private void beginLaTeXTag(Appendable _out, InternalList subList) throws IOException {
		if (subList.fChar == '*') {
			// bullet list
			_out.append(PropertyManager.get("List.Unnumbered.Begin"));
		} else if (subList.fChar == '#') {
			// numbered list
			_out.append(PropertyManager.get("List.Numbered.Begin"));
		}
	}

	private void endLaTeXTag(Appendable _out, InternalList subList) throws IOException {
		if (subList.fChar == '*') {
			// bullet list
			_out.append(PropertyManager.get("List.Unnumbered.End"));
		} else if (subList.fChar == '#') {
			// numbered list
			_out.append(PropertyManager.get("List.Numbered.End"));
		}
	}

	@Override
	public void renderLaTeX(ITextConverter converter, Appendable _out, IWikiModel wikiModel) throws IOException {
		if (!isEmpty()) {

			fInternalListStack = null;

			for (int i = 0; i < fNestedElements.size(); i++) {
				Object element = fNestedElements.get(i);
				if (element instanceof InternalList) {
					InternalList subList = (InternalList) element;
					beginLaTeXTag(_out, subList);
					renderSubListLaTeX(subList, converter, _out, wikiModel);
					endLaTeXTag(_out, subList);
				} else {
					TagStack stack = ((WPListElement) element).getTagStack();
					if (stack != null) {
						converter.nodesToText(stack.getNodeList(), _out, wikiModel);
					}
					// wikiModel.appendStack(((WPListElement)
					// element).getTagStack());
				}
			}
		}
	}

	private void renderSubListLaTeX(InternalList list, ITextConverter converter, Appendable _out, IWikiModel wikiModel)
			throws IOException {
		if (list.size() > 0) {
			_out.append(PropertyManager.get("List.Item.Begin"));
		}
		String itemStr = PropertyManager.get("List.Item.Begin") + PropertyManager.get("List.Item.End");
		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				beginLaTeXTag(_out, subList);
				// recursive call:
				renderSubListLaTeX(subList, converter, _out, wikiModel);
				_out.append(PropertyManager.get("List.Item.End"));
			} else {
				TagStack stack = ((WPListElement) element).getTagStack();
				converter.nodesToText(stack.getNodeList(), _out, wikiModel);
				// wikiModel.appendStack(((WPListElement)
				// element).getTagStack());
			}

			if ((i < list.size() - 1) && list.get(i + 1) instanceof WPListElement) {
				// open new lists as required by 'descr'
				_out.append(itemStr);
			}

		}
		if (list.size() > 0) {
			_out.append(PropertyManager.get("List.Item.End"));
		}
	}

	@Override
	public Object clone() {
		WPList tt = (WPList) super.clone();
		if (fNestedElements == null) {
			tt.fNestedElements = null;
		} else {
			tt.fNestedElements = (InternalList) this.fNestedElements.clone();
		}
		tt.fInternalListStack = (ArrayList<InternalList>) this.fInternalListStack.clone();
		if (fLastSequence == null) {
			tt.fLastSequence = null;
		} else {
			tt.fLastSequence = new char[this.fLastSequence.length];
			System.arraycopy(this.fLastSequence, 0, tt.fLastSequence, 0, this.fLastSequence.length);
		}
		return tt;
	}
	
	@Override
	public boolean isReduceTokenStack() {
		return true;
	}

	@Override
	public String getParents() {
		return Configuration.SPECIAL_BLOCK_TAGS;
	}
}