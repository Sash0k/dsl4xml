package com.sjl.dsl4xml;

import java.util.*;

import org.xmlpull.v1.*;

import com.sjl.dsl4xml.support.*;
import com.sjl.dsl4xml.support.convert.*;

public final class MarshallingContext {

	private XmlPullParser parser;
	private Stack<Object> stack;
	private List<Converter<?>> converters;
	
	public MarshallingContext(XmlPullParser aParser) {
		stack = new Stack<Object>();
		parser = aParser;
		converters = new ArrayList<Converter<?>>();
		
		registerConverters(
			new StringConverter(),
			new PrimitiveIntConverter(),
			new IntegerConverter()
		);
	}
	
	public void registerConverters(Converter<?>... aConverters) {
		// push any registered converters on ahead of existing converters (allows simple override)
		converters.addAll(0, Arrays.asList(aConverters));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T peek() {
		return (T) stack.peek();
	}
	
	public void push(Object aContext) {
		stack.push(aContext);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T pop() {
		return (T) stack.pop();
	}
	
	public XmlPullParser getParser() {
		return parser;
	}
	
	public boolean hasMoreTags() {
		try {
			return parser.getEventType() != XmlPullParser.END_DOCUMENT;
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}
	
	public int next() {
		try {
			return parser.next();
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}

	public boolean isTagNamed(String aTagName) {
		return aTagName.equals(parser.getName());
	}

	public boolean isNotEndTag(String aTagName) {
		try {
			return !(
				(parser.getEventType() == XmlPullParser.END_TAG) && 
				(aTagName.equals(parser.getName()))
			);
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}

	public boolean isStartTag() {
		try {
			return parser.getEventType() == XmlPullParser.START_TAG;
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}
	
	public String getAttributeValue(String anAttributeName) {
		return parser.getAttributeValue(null, anAttributeName);
	}
	
	public boolean isTextNode() {
		try {
			return parser.getEventType() == XmlPullParser.TEXT;
		} catch (XmlPullParserException anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}

	public boolean isStartTagNamed(String aTagName) {
		try {
			return (
				(parser.getEventType() == XmlPullParser.START_TAG) && 
				(aTagName.equals(parser.getName()))
			);
		} catch (Exception anExc) {
			throw new XmlMarshallingException(anExc);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Converter<T> getConverter(Class<T> aArgType) {
		for (Converter<?> _c : converters) {
			if (_c.canConvertTo(aArgType)) {
				return (Converter<T>) _c;
			}
		}
		throw new RuntimeException("No converter registered that can convert to " + aArgType);
	}
}