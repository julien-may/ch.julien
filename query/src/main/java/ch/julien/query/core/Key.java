package ch.julien.query.core;

class Key<T> {
	protected final T value;

	public Key(T value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Key key = (Key)other;

		return !(value != null ? !value.equals(key.value) : key.value != null);

	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}
}
