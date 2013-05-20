package ch.julien.common.contract;

import java.util.Collection;

public class Check {
	public static void notNull(Object object, String paramName) {
		notNull(IllegalArgumentException.class, object, parameter(paramName) + " must not be null.");
	}

	public static <T extends RuntimeException> void notNull(Class<T> type, Object object, String message) {
		if (type == null) {
			throw new IllegalArgumentException("type must not be null.");
		}

		try {
			if (object == null) {
				throw type.getConstructor().newInstance(message);
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void notNullOrEmpty(String string, String paramName) {
		if (string == null || string.trim().isEmpty()) {
			throw new IllegalArgumentException(parameter(paramName) + " must not be null or empty.");
		}
	}

	public static void notNullOrEmpty(Collection<?> collection, String paramName) {
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException(parameter(paramName) + " must not be null or empty.");
		}
	}

	private static String parameter(String paramName) {
		if (paramName != null && !paramName.trim().isEmpty()) {
			return  paramName;
		}

		return "Argument";
	}
}
