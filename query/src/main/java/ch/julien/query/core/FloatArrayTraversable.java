package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class FloatArrayTraversable extends TraversableImpl<Float> {
	private static class FloatArrayIterator implements Iterator<Float> {
		private final float[] array;
		private int index = 0;

		public FloatArrayIterator(float[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Float next() {
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

	public FloatArrayTraversable(final float[] source) {
		super(
			new TraversableImpl<Float>(
				new Iterable<Float>() {
					@Override
					public Iterator<Float> iterator() {
						return new FloatArrayIterator(source);
					}
				}
			)
		);
	}
}
