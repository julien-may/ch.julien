package ch.julien.query.util;

import java.util.Collection;
import java.util.Map;

import ch.julien.common.delegate.Predicate;

public class Predicates {
	
	public static final Predicate<Object> notNull() {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return arg != null;
			}
		};
	}
	
	public static final Predicate<Collection<?>> notEmptyCollection() {
		return new Predicate<Collection<?>>() {
			@Override
			public boolean invoke(Collection<?> arg) {
				return arg != null && ! arg.isEmpty();
			}
		};
	}
	
	public static final Predicate<Map<?, ?>> notEmptyMap() {
		return new Predicate<Map<?,?>>() {
			@Override
			public boolean invoke(Map<?, ?> arg) {
				return arg != null && ! arg.isEmpty();
			}
		};
	}
	
	public static final <T> Predicate<T[]> notEmptyArray() {
		return new Predicate<T[]>() {
			@Override
			public boolean invoke(T[] arg) {
				return arg != null && arg.length > 0;
			}
		};
	}
	
	public static final Predicate<Object> elementOfInstance(final Class<?> clazz) {
		return new Predicate<Object>() {
			@Override
			public boolean invoke(Object arg) {
				return clazz.isInstance(arg);
			}
		};
	}
	
}
