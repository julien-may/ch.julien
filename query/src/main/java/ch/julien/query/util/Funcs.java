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
	
}
