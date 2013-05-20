package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class ByteArrayTraversable extends TraversableImpl<Byte> {
	private static class ByteArrayIterator implements Iterator<Byte> {
		private final byte[] array;
		private int index = 0;

		public ByteArrayIterator(byte[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Byte next() {
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

	public ByteArrayTraversable(final byte[] source) {
		super(
			new TraversableImpl<Byte>(
				new Iterable<Byte>() {
					@Override
					public Iterator<Byte> iterator() {
						return new ByteArrayIterator(source);
					}
				}
			)
		);
	}
}
