package info.bliki;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handles the <code>Messages_XX.properties</code> files for I18N support.
 *
 */
public class Messages {
	public final static String RESOURCE_BUNDLE = "Messages";//$NON-NLS-1$

	private static ResourceBundle resourceBundle = null;

	public final static String WIKI_TAGS_TOC_CONTENT = "wiki.tags.toc.content";

	public Messages() {
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		try {
			resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE, locale);
			return resourceBundle;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getString(final ResourceBundle bundle, final String key) {
		try {
			return bundle.getString(key);
		} catch (final Exception e) {
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
	}
}
