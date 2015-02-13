package com.infinities.keystone4j.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtils {

	private final static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);


	private ReflectUtils() {

	}

	public static Class<?> getReturnType(Class<?> obj, String expr) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String[] split = expr.split("\\.");
		if (split.length > 1) {
			throw new IllegalArgumentException("expr not allow: " + expr);
		}

		String methodName = concentrateMethodName(split[0]);
		logger.debug("reflect method: {}", methodName);
		Method m = obj.getMethod(methodName);
		return m.getReturnType();
	}

	public static boolean hasProperty(Class<?> obj, String expr) throws SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String[] split = expr.split("\\.");
		if (split.length > 1) {
			throw new IllegalArgumentException("expr not allow: " + expr);
		}

		try {
			String methodName = concentrateMethodName(split[0]);
			logger.debug("reflect method: {}", methodName);
			obj.getMethod(methodName);
		} catch (NoSuchMethodException e) {
			return false;
		}

		return true;
	}

	public static Object reflact(Object obj, String expr) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String[] split = expr.split("\\.");
		return reflectValue(obj, split);
	}

	@SuppressWarnings("unchecked")
	private static Object reflectValue(Object target, String[] split) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object value = null;
		if (split.length == 0) {
			return target;
		}

		if (target instanceof Map) {
			value = ((Map<String, Object>) target).get(split[0]);
		} else {
			Method m = null;
			String methodName = concentrateMethodName(split[0]);
			logger.debug("reflect method: {}", methodName);
			m = target.getClass().getMethod(methodName);

			value = m.invoke(target);
		}
		split = Arrays.copyOfRange(split, 1, split.length);

		return reflectValue(value, split);
	}

	private static String concentrateMethodName(String orig) {
		logger.debug("orig: {}", orig);
		if (orig.contains("_")) {
			String[] split = orig.split("_");
			orig = "";
			for (String s : split) {
				orig += Character.toUpperCase(s.charAt(0)) + s.substring(1);
			}
		}
		orig = orig.replaceFirst(orig.substring(0, 1), orig.substring(0, 1).toUpperCase());

		return "get" + orig;
	}

}
