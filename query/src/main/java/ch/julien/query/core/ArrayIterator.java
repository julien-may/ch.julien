package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class ArrayIterator<TSource> implements Iterator<TSource> {
	private final TSource[] array;
	private int index = 0;

	public ArrayIterator(TSource[] array) {
		Check.notNull(array, "array");

		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return this.index < this.array.length;
	}

	@Override
	public TSource next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		return this.array[this.index++];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
