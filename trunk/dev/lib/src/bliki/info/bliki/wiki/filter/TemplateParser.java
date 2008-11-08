package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.WikiTagNode;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A template parser for the first pass in the parsing of a Wikipedia text
 * 
 * @see WikipediaParser for the second pass
 */
public class TemplateParser extends AbstractParser {
	public final boolean fParseOnlySignature;

	private final boolean fRenderTemplate;

	public TemplateParser(String stringSource) {
		this(stringSource, false, false);
	}

	public TemplateParser(String stringSource, boolean parseOnlySignature, boolean renderTemplate) {
		super(stringSource);
		fParseOnlySignature = parseOnlySignature;
		fRenderTemplate = renderTemplate;
	}

	public static void parse(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean renderTemplate) throws IOException {
		parse(rawWikitext, wikiModel, writer, false, renderTemplate);
	}

	/**
	 * Parse the wiki texts templates, comments and signatures into the given
	 * <code>StringBuilder</code>.
	 * 
	 * @param rawWikitext
	 * @param wikiModel
	 * @param writer
	 * @param parseOnlySignature
	 *          change only the signature string and ignore templates and comments
	 *          parsing
	 * @param renderTemplate
	 */
	public static void parse(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean parseOnlySignature,
			boolean renderTemplate) throws IOException {
		parseRecursive(rawWikitext, wikiModel, writer, parseOnlySignature, renderTemplate, null);
	}

	// private static Pattern noinclude =
	// Pattern.compile("<noinclude[^>]*>.*?<\\\\/noinclude[^>]*>");
	//
	// private static Pattern INCLUDEONLY_PATTERN =
	// Pattern.compile("<includeonly[^>]*>(.*?)<\\/includeonly[^>]*>");

	protected static void parseRecursive(String rawWikitext, IWikiModel wikiModel, Appendable writer, boolean parseOnlySignature,
			boolean renderTemplate, HashMap<String, String> templateParameterMap) throws IOException {
		try {
			int level = wikiModel.incrementRecursionLevel();
			if (level > Configuration.PARSER_RECURSION_LIMIT) {
				writer.append("Error - recursion limit exceeded parsing templates.");
				return;
			}
			TemplateParser parser = new TemplateParser(rawWikitext, false, renderTemplate);
			parser.setModel(wikiModel);
			StringBuilder sb = new StringBuilder(rawWikitext.length());
			parser.runPreprocessParser(sb);
			
			StringBuilder plainBuffer = sb;
			if (templateParameterMap != null && (!templateParameterMap.isEmpty())) {
				String preprocessedContent = sb.toString();
				WikipediaScanner scanner = new WikipediaScanner(preprocessedContent);
				plainBuffer = scanner.replaceTemplateParameters(preprocessedContent, templateParameterMap);
				if (plainBuffer==null) {
					plainBuffer = sb;
				}
			}
			parser = new TemplateParser(plainBuffer.toString(), false, renderTemplate);
			parser.setModel(wikiModel);
			// parser.initialize(plainBuffer.toString());
			parser.runParser(writer);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			writer.append(e.getClass().getSimpleName());
		} catch (Error e) {
			e.printStackTrace();
			writer.append(e.getClass().getSimpleName());
		} finally {
			wikiModel.decrementRecursionLevel();
		}
	}

	/**
	 * Preprocess parsing of the <code>&lt;includeonly&gt;</code> and
	 * <code>&lt;noinclude&gt;</code> tags
	 * 
	 * @param writer
	 * @throws IOException
	 */
	protected void runPreprocessParser(Appendable writer) throws IOException {
		fWhiteStart = true;
		fWhiteStartPosition = fCurrentPosition;
		try {
			while (true) {
				fCurrentCharacter = fSource[fCurrentPosition++];

				// ---------Identify the next token-------------
				switch (fCurrentCharacter) {
				case '<':
					int htmlStartPosition = fCurrentPosition;
					if (!fParseOnlySignature && parseIncludeWikiTags(writer)) {
						continue;
					}
					fCurrentPosition = htmlStartPosition;
					break;
				}

				if (!fWhiteStart) {
					fWhiteStart = true;
					fWhiteStartPosition = fCurrentPosition - 1;
				}

			}
			// -----------------end switch while try--------------------
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		try {
			appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
	}

	protected void runParser(Appendable writer) throws IOException {
		fWhiteStart = true;
		fWhiteStartPosition = fCurrentPosition;
		try {
			while (true) {
				fCurrentCharacter = fSource[fCurrentPosition++];

				// ---------Identify the next token-------------
				switch (fCurrentCharacter) {
				case '{': // wikipedia template handling
					if (!fParseOnlySignature && parseTemplate(writer)) {
						fWhiteStart = true;
						fWhiteStartPosition = fCurrentPosition;
						continue;
					}
					break;

				case '<':
					int htmlStartPosition = fCurrentPosition;
					if (!fParseOnlySignature && parseSpecialWikiTags(writer)) {
						continue;
					}
					fCurrentPosition = htmlStartPosition;
					break;
				case '~':
					int tildeCounter = 0;
					if (fSource[fCurrentPosition] == '~' && fSource[fCurrentPosition + 1] == '~') {
						// parse signatures '~~~', '~~~~' or '~~~~~'
						tildeCounter = 3;
						try {
							if (fSource[fCurrentPosition + 2] == '~') {
								tildeCounter = 4;
								if (fSource[fCurrentPosition + 3] == '~') {
									tildeCounter = 5;
								}
							}
						} catch (IndexOutOfBoundsException e1) {
							// end of scanner text
						}
						appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
						fWikiModel.appendSignature(writer, tildeCounter);
						fCurrentPosition += (tildeCounter - 1);
						fWhiteStart = true;
						fWhiteStartPosition = fCurrentPosition;
					}
				}

				if (!fWhiteStart) {
					fWhiteStart = true;
					fWhiteStartPosition = fCurrentPosition - 1;
				}

			}
			// -----------------end switch while try--------------------
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		try {
			appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
	}

	protected boolean parseIncludeWikiTags(Appendable writer) throws IOException {
		try {
			switch (fSource[fCurrentPosition]) {
			case '!': // <!-- html comment -->
				if (parseHTMLCommentTags(writer)) {
					return true;
				}
				break;
			default:

				if (fSource[fCurrentPosition] != '/') {
					// starting tag
					int lessThanStart = fCurrentPosition - 1;
					WikiTagNode tagNode = parseTag(fCurrentPosition);
					if (tagNode != null) {
						fCurrentPosition = tagNode.getEndPosition();
						int tagStart = fCurrentPosition;
						String tagName = tagNode.getTagName();
						if (tagName.equals("nowiki")) {
							if (readUntilIgnoreCase("</", "nowiki>")) {
								return true;
							}
						} else if (tagName.equals("source")) {
							if (readUntilIgnoreCase("</", "source>")) {
								return true;
							}
						} else if (tagName.equals("math")) {
							if (readUntilIgnoreCase("</", "math>")) {
								return true;
							}
						}
						if (!isTemplate()) {
							if (tagName.equals("includeonly")) {
								if (readUntilIgnoreCase("</", "includeonly>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = tagStart;

									appendContent(writer, fWhiteStart, fWhiteStartPosition, 2 + "includeonly>".length());
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
							} else if (tagName.equals("noinclude")) {
								if (readUntilIgnoreCase("</", "noinclude>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
							}
						} else {
							if (tagName.equals("noinclude")) {
								if (readUntilIgnoreCase("</", "noinclude>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = tagStart;

									appendContent(writer, fWhiteStart, fWhiteStartPosition, 2 + "noinclude>".length());
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
							} else if (tagName.equals("includeonly")) {
								if (readUntilIgnoreCase("</", "includeonly>")) {
									appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - lessThanStart);
									fWhiteStart = true;
									fWhiteStartPosition = fCurrentPosition;
									return true;
								}
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return false;
	}

	protected boolean parseSpecialWikiTags(Appendable writer) throws IOException {
		try {
			switch (fSource[fCurrentPosition]) {
			case '!': // <!-- html comment -->
				if (parseHTMLCommentTags(writer)) {
					return true;
				}
				break;
			default:

				if (fSource[fCurrentPosition] != '/') {
					// starting tag
					WikiTagNode tagNode = parseTag(fCurrentPosition);
					if (tagNode != null) {
						fCurrentPosition = tagNode.getEndPosition();
						String tagName = tagNode.getTagName();
						if (tagName.equals("nowiki")) {
							if (readUntilIgnoreCase("</", "nowiki>")) {
								return true;
							}
						} else if (tagName.equals("source")) {
							if (readUntilIgnoreCase("</", "source>")) {
								return true;
							}
						} else if (tagName.equals("math")) {
							if (readUntilIgnoreCase("</", "math>")) {
								return true;
							}
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return false;
	}

	protected void appendContent(Appendable writer, boolean whiteStart, final int whiteStartPosition, final int diff)
			throws IOException {
		if (whiteStart) {
			try {
				final int whiteEndPosition = fCurrentPosition - diff;
				int count = whiteEndPosition - whiteStartPosition;
				if (count > 0) {
					writer.append(fStringSource, whiteStartPosition, whiteEndPosition); // count);
				}
			} finally {
				fWhiteStart = false;
			}
		}
	}

	private boolean parseTemplate(Appendable writer) throws IOException {
		if (fSource[fCurrentPosition] == '{') {
			appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
			int startTemplatePosition = ++fCurrentPosition;
			if (fSource[fCurrentPosition] != '{') {
				int templateEndPosition = findTemplateEnd(fSource, fCurrentPosition);
				if (templateEndPosition < 0) {
					fCurrentPosition--;
				} else {
					fCurrentPosition = templateEndPosition;
					// insert template handling
					int endPosition = fCurrentPosition;
					String plainContent = null;
					int endOffset = fCurrentPosition - 2;
					String function = checkParserFunction(startTemplatePosition, endOffset);
					if (function != null) {
						ITemplateFunction templateFunction = fWikiModel.getTemplateFunction(function.toLowerCase());
						if (templateFunction != null) {
							// if (function.charAt(0) == '#') {
							// #if:, #ifeq:,...
							plainContent = templateFunction.parseFunction(fSource, fCurrentPosition, endOffset, fWikiModel);
							fCurrentPosition = endPosition;
							if (plainContent != null) {
								TemplateParser.parseRecursive(plainContent, fWikiModel, writer, false, false, null);
								return true;
							}
							// } else {
							// // lcfirst:, ucfirst:,...
							// plainContent = new String(fSource, fCurrentPosition, endOffset
							// -
							// fCurrentPosition).trim();
							// StringBuilder buf = new StringBuilder();
							// TemplateParser.parseRecursive(plainContent, fWikiModel, buf,
							// false, false);
							// String result = buf.toString();
							// plainContent =
							// templateFunction.parseFunction(result.toCharArray(), 0,
							// result.length(), fWikiModel);
							// fCurrentPosition = endPosition;
							// if (plainContent != null) {
							// TemplateParser.parseRecursive(plainContent, fWikiModel, writer,
							// false,
							// false);
							// return true;
							// }
							// }
							return true;
						}
						fCurrentPosition = endOffset + 2;
					}
					Object[] objs = createParameterMap(fSource, startTemplatePosition, fCurrentPosition - startTemplatePosition - 2);
					HashMap<String, String> map = (HashMap<String, String>) objs[0];
					String templateName = ((String) objs[1]).trim();
					if (templateName.length() > 0 && templateName.charAt(0) == ':') {
						plainContent = fWikiModel.getRawWikiContent("", templateName.substring(1), map);
					} else {
						fWikiModel.addTemplate(templateName);
						plainContent = fWikiModel.getRawWikiContent(fWikiModel.getTemplateNamespace(), templateName, map);
					}

					fCurrentPosition = endPosition;
					if (plainContent != null) {
//						WikipediaScanner scanner = new WikipediaScanner(plainContent);
//						StringBuilder plainBuffer = scanner.replaceTemplateParameters(plainContent, map);
//						if (plainBuffer == null) {
//							TemplateParser.parseRecursive(plainContent, fWikiModel, writer, false, false, map);
//							return true;
//						}
						TemplateParser.parseRecursive(plainContent.trim(), fWikiModel, writer, false, false, map);
						return true;
					}
					// if no template found insert plain template name string:
					writer.append("{{" + templateName + "}}");

					return true;
				}
			} else {
				// parse template parameters
				int templateEndPosition = findTemplateParameterEnd(fCurrentPosition + 1);
				if (templateEndPosition > 0) {
					String plainContent = new String(fSource, startTemplatePosition - 2, templateEndPosition - startTemplatePosition + 2);
					if (plainContent != null) {
						fCurrentPosition = templateEndPosition;
						WikipediaScanner scanner = new WikipediaScanner(plainContent);
						StringBuilder plainBuffer = scanner.replaceTemplateParameters(plainContent, null);
						if (plainBuffer == null) {
							writer.append(plainContent);
							return true;
						}
						TemplateParser.parseRecursive(plainBuffer.toString().trim(), fWikiModel, writer, false, false, null);
						return true;
					}
				} else {
					--fCurrentPosition;
				}
			}
		}
		return false;
	}

	/**
	 * Create a map from the parameters defined in a template call
	 * 
	 * @return the templates parameter map at index [0] and the template name at
	 *         index [1]
	 * 
	 */
	private static Object[] createParameterMap(char[] src, int startOffset, int len) {
		Object[] objs = new Object[2];
		HashMap<String, String> map = new HashMap<String, String>();
		objs[0] = map;
		int currOffset = startOffset;
		int endOffset = startOffset + len;
		List<String> resultList = new ArrayList<String>();
		resultList = splitByPipe(src, currOffset, endOffset, resultList);
		if (resultList.size() <= 1) {
			// set the templates name
			objs[1] = new String(src, startOffset, len);
			return objs;
		}
		objs[1] = resultList.get(0);

		for (int i = 1; i < resultList.size(); i++) {
			createSingleParameter(i, resultList.get(i).toCharArray(), map);
		}

		return objs;
	}

	/**
	 * Create a single parameter defined in a template call and add it to the
	 * parameters map
	 * 
	 */
	private static void createSingleParameter(int parameterCounter, char[] src, HashMap<String, String> map) {
		int currOffset = 0;
		int endOffset = src.length;
		char ch;
		String parameter = null;
		String value;
		boolean equalCharParsed = false;

		int lastOffset = currOffset;
		int temp;
		try {
			while (currOffset < endOffset) {
				ch = src[currOffset++];
				if (ch == '[' && src[currOffset] == '[') {
					currOffset++;
					temp = findNestedEnd(src, '[', ']', currOffset);
					if (temp >= 0) {
						currOffset = temp;
					}
				} else if (ch == '{' && src[currOffset] == '{') {
					currOffset++;
					if (src[currOffset] == '{') {
						currOffset++;
						temp = findNestedParamEnd(src, currOffset);
						if (temp >= 0) {
							currOffset = temp;
						} else {
							currOffset--;
							temp = findNestedTemplateEnd(src, currOffset);
							if (temp >= 0) {
								currOffset = temp;
							}
						}
					} else {
						temp = findNestedTemplateEnd(src, currOffset);
						if (temp >= 0) {
							currOffset = temp;
						}
					}
				} else if (ch == '=') {
					if (!equalCharParsed) {
						parameter = new String(src, lastOffset, currOffset - lastOffset - 1).trim();
						lastOffset = currOffset;
					}
					equalCharParsed = true;
				}
			}

		} catch (IndexOutOfBoundsException e) {

		} finally {
			if (currOffset > lastOffset) {
				value = new String(src, lastOffset, currOffset - lastOffset).trim();
				map.put(Integer.toString(parameterCounter), value);
				if (parameter != null) {
					map.put(parameter, value);
				}
			}
		}
	}

	/**
	 * Check if this template contains a template function
	 * 
	 * Note: repositions this#fCurrentPosition behind the parser function string
	 * if possible
	 * 
	 * @param startOffset
	 * @param endOffset
	 * @return the parser function name (without the # character) or
	 *         <code>null</code> if no parser function can be found in this
	 *         template
	 */
	private String checkParserFunction(int startOffset, int endOffset) {
		// String function = null;
		int currOffset = startOffset;
		int functionStart = startOffset;
		char ch;
		while (currOffset < endOffset) {
			ch = fSource[currOffset++];
			if (!Character.isWhitespace(ch)) {
				functionStart = currOffset - 1;
				while (currOffset < endOffset) {
					ch = fSource[currOffset++];
					if (ch == ':') {
						fCurrentPosition = currOffset;
						return new String(fSource, functionStart, currOffset - functionStart - 1);
						// break;
					}
				}
				break;
			}
			// if (ch == '#') {
			// functionStart = currOffset;
			// while (currOffset < endOffset) {
			// ch = fSource[currOffset++];
			// if (ch == ':') {
			// fCurrentPosition = currOffset;
			// function = new String(fSource, functionStart, currOffset -
			// functionStart - 1);
			// break;
			// }
			// }
			// break;
			// } else if (Character.isWhitespace(ch)) {
			// continue;
			// }
			// break;
		}
		return null;
	}

	protected boolean parseHTMLCommentTags(Appendable writer) throws IOException {
		int temp = readWhitespaceUntilStartOfLine(2);
		String htmlCommentString = new String(fSource, fCurrentPosition - 1, 4);
		// f.substring(fCurrentPosition - 1, fCurrentPosition + 3);
		if (htmlCommentString.equals("<!--")) {
			if (temp >= 0) {
				appendContent(writer, fWhiteStart, fWhiteStartPosition, fCurrentPosition - temp - 1);
			} else {
				appendContent(writer, fWhiteStart, fWhiteStartPosition, 1);
			}
			fCurrentPosition += 3;
			if (readUntil("-->")) {
				if (temp >= 0) {
					temp = readWhitespaceUntilEndOfLine(0);
					if (temp >= 0) {
						fCurrentPosition++;
					}
				}
				fWhiteStart = true;
				fWhiteStartPosition = fCurrentPosition;
				return true;
			}
		}
		return false;
	}

	@Override
	public void runParser() {
		// do nothing here
	}

	@Override
	public void setNoToC(boolean noToC) {
		// do nothing here
	}

	public boolean isTemplate() {
		return fRenderTemplate;
	}

}
