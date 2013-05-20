package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class LongArrayTraversable extends TraversableImpl<Long> {
	private static class LongArrayIterator implements Iterator<Long> {
		private final long[] array;
		private int index = 0;

		public LongArrayIterator(long[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Long next() {
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

	public LongArrayTraversable(final long[] source) {
		super(
			new TraversableImpl<Long>(
				new Iterable<Long>() {
					@Override
					public Iterator<Long> iterator() {
						return new LongArrayIterator(source);
					}
				}
			)
		);
	}
}
