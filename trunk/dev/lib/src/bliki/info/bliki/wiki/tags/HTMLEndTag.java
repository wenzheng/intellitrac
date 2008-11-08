package info.bliki.wiki.tags;

import org.htmlcleaner.EndTagToken;


public class HTMLEndTag extends EndTagToken 
{

	public HTMLEndTag(String name)
	{
		super(name);
	}


	@Override
	public boolean isReduceTokenStack()
	{
		return false;
	}
	
}