package info.bliki.wiki.template;

import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #if: ... }}</code> syntax
 * 
 */
public class If extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new If();

	public If() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 1) {
//			String condition = parse(list.get(0), model);
//			if (condition.length() == 0) {
//				if (list.size() >= 3) {
//					// &lt;else text&gt;
//					return parse(list.get(2), model);
//				}
//				return null;
//			}
//			StringBuilder conditionBuffer = new StringBuilder(condition.length());
//			TemplateParser.parse(condition, model, conditionBuffer, false);
			
			String condition = parse(list.get(0), model);
			if (condition.length() > 0) {
				// &lt;then text&gt;
				return parse(list.get(1), model);
			} else {
				if (list.size() >= 3) {
					// &lt;else text&gt;
					return parse(list.get(2), model);
				}
			}
		}
		return null;
	}
}
