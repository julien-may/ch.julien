package ch.julien.query.util;

import ch.julien.common.delegate.Func;


public class Funcs {
	
	public static final <T> Func<Object, T> to(final Class<T> clazz) {
		return new Func<Object, T>() {
			@SuppressWarnings("unchecked")
			@Override public T invoke(Object arg) {
				return (T) arg;
			}
		};
	}
	
	public static final Func<String, String> replaceAll(final String regex, final String replacement) {
		return new Func<String, String>() {
			@Override
			public String invoke(String arg) {
				return arg.replaceAll(regex, replacement);
			}
		};
	}
	
	public static final Func<String, String> trimString() {
		return new Func<String, String>() {
			@Override
			public String invoke(String arg) {
				return arg.trim();
			}
		};
	}
	
	public static final Func<String, Integer> parseInteger() {
		return new Func<String, Integer>() {
			@Override
			public Integer invoke(String arg) {
				return Integer.parseInt(arg);
			}
		};
	}
	
	public static final Func<String, Double> parseDouble() {
		return new Func<String, Double>() {
			@Override
			public Double invoke(String arg) {
				return Double.parseDouble(arg);
			}
		};
	}
	
}
