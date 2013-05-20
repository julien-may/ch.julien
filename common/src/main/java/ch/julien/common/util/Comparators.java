package ch.julien.common.util;

import ch.julien.common.delegate.EqualityComparator;

import java.util.Comparator;

public class Comparators {
	public static <T> Comparator<T> fromEqualityComparator(final EqualityComparator<T> equalityComparator) {
		return new Comparator<T>() {
			@Override
			public int compare(T a, T b) {
				if (equalityComparator.equals(a, b)) {
					return 0;
				}

				if (a.hashCode() > b.hashCode()) {
					return 1;
				}

				return -1;
			}
		};
	}
}
