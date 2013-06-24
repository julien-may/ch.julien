package ch.julien.query.core;

import ch.julien.common.delegate.EqualityComparator;

class Key<T> {
	protected final T value;
	protected final EqualityComparator<T> equalityComparator;

	public Key(T value, EqualityComparator<T> equalityComparator) {
		this.value = value;
		this.equalityComparator = equalityComparator;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Key<T> key = (Key<T>)other;

		if (this.equalityComparator != null) {
			return this.equalityComparator.equals(this.value, key.value);
		}

		return !(value != null ? !value.equals(key.value) : key.value != null);
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
