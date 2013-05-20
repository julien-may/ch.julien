package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class BooleanArrayTraversable extends TraversableImpl<Boolean> {
	private static class BooleanArrayIterator implements Iterator<Boolean> {
		private final boolean[] array;
		private int index = 0;

		public BooleanArrayIterator(boolean[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Boolean next() {
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

	public BooleanArrayTraversable(final boolean[] source) {
		super(
			new TraversableImpl<Boolean>(
				new Iterable<Boolean>() {
					@Override
					public Iterator<Boolean> iterator() {
						return new BooleanArrayIterator(source);
					}
				}
			)
		);
	}
}
