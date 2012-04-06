package com.sjl.dsl4xml.support.convert;

import com.sjl.dsl4xml.support.*;

public class ByteConverter implements Converter<Byte> {

	@Override
	public boolean canConvertTo(Class<?> aClass) {
		return aClass.isAssignableFrom(Byte.class);
	}

	@Override
	public Byte convert(String aValue) {
		return ((aValue == null) || ("".equals(aValue))) ? null : new Byte(aValue);
	}
	
}

