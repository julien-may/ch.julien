package ch.julien.query.core;

import ch.julien.common.delegate.EqualityComparator;

import java.util.Comparator;

class KeyComparator<T> implements Comparator<Key<T>> {
	private final EqualityComparator<T> equalityComparator;

	public KeyComparator(EqualityComparator<T> equalityComparator) {
		this.equalityComparator = equalityComparator != null
			? equalityComparator
			: new EqualityComparator<T>() {
				@Override
				public boolean equals(T first, T second) {
					return first == null ? second == null : first.equals(second);
				}
			};
	}

	@Override
	public int compare(Key<T> a, Key<T> b) {
		if (this.equalityComparator.equals(a.value, b.value)) {
			return 0;
		}

		if (a.hashCode() > b.hashCode()) {
			return 1;
		}

		return -1;
	}
}
