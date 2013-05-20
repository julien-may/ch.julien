package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class ShortArrayTraversable extends TraversableImpl<Short> {
	private static class ShortArrayIterator implements Iterator<Short> {
		private final short[] array;
		private int index = 0;

		public ShortArrayIterator(short[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Short next() {
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

	public ShortArrayTraversable(final short[] source) {
		super(
			new TraversableImpl<Short>(
				new Iterable<Short>() {
					@Override
					public Iterator<Short> iterator() {
						return new ShortArrayIterator(source);
					}
				}
			)
		);
	}
}
