package com.addictedtor.jedit.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// see http://snippets.dzone.com/posts/show/2242

public class PrivateAccessor {

	public static Object getPrivateField(Object o, String fieldName) {
		return getPrivateField(o, fieldName, o.getClass());
	}

	public static Object getPrivateField(Object o, String fieldName,
			Class<?> clazz) {
		// Go and find the private field...
		final Field fields[] = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fieldName.equals(fields[i].getName())) {
				try {
					fields[i].setAccessible(true);
					return fields[i].get(o);
				} catch (IllegalAccessException ex) {
				}
			}
		}
		return null;
	}

	public static void setPrivateField(Object o, String fieldName,
			Class<?> clazz, Object value) {
		// Go and find the private field...
		final Field fields[] = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; ++i) {
			if (fieldName.equals(fields[i].getName())) {
				try {
					fields[i].setAccessible(true);
					fields[i].set(o, value);
				} catch (IllegalAccessException ex) {
				}
			}
		}
	}

	public static Object invokePrivateMethod(Object o, String methodName,
			Object[] params) {
		return invokePrivateMethod(o, methodName, params, o.getClass());
	}

	public static Object invokePrivateMethod(Object o, String methodName,
			Object[] params, Class<?> clazz) {
		// Go and find the private method...
		final Method methods[] = clazz.getDeclaredMethods();
		for (int i = 0; i < methods.length; ++i) {
			if (methodName.equals(methods[i].getName())) {
				try {
					methods[i].setAccessible(true);
					return methods[i].invoke(o, params);
				} catch (IllegalAccessException ex) {
				} catch (InvocationTargetException ite) {
				}
			}
		}
		return null;
	}

}
