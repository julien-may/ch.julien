package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class IntArrayTraversable extends TraversableImpl<Integer> {
	private static class IntArrayIterator implements Iterator<Integer> {
		private final int[] array;
		private int index = 0;

		public IntArrayIterator(int[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Integer next() {
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

	public IntArrayTraversable(final int[] source) {
		super(
			new TraversableImpl<Integer>(
				new Iterable<Integer>() {
					@Override
					public Iterator<Integer> iterator() {
						return new IntArrayIterator(source);
					}
				}
			)
		);
	}
}
