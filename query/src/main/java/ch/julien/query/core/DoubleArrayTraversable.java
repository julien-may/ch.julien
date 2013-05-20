package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class DoubleArrayTraversable extends TraversableImpl<Double> {
	private static class DoubleArrayIterator implements Iterator<Double> {
		private final double[] array;
		private int index = 0;

		public DoubleArrayIterator(double[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Double next() {
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

	public DoubleArrayTraversable(final double[] source) {
		super(
			new TraversableImpl<Double>(
				new Iterable<Double>() {
					@Override
					public Iterator<Double> iterator() {
						return new DoubleArrayIterator(source);
					}
				}
			)
		);
	}
}
