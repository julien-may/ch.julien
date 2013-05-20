package ch.julien.query.core;

import ch.julien.common.contract.Check;

import java.util.Iterator;
import java.util.NoSuchElementException;

class CharArrayTraversable extends TraversableImpl<Character> {
	private static class CharArrayIterator implements Iterator<Character> {
		private final char[] array;
		private int index = 0;

		public CharArrayIterator(char[] array) {
			Check.notNull(array, "array");

			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.array.length;
		}

		@Override
		public Character next() {
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

	public CharArrayTraversable(final char[] source) {
		super(
			new TraversableImpl<Character>(
				new Iterable<Character>() {
					@Override
					public Iterator<Character> iterator() {
						return new CharArrayIterator(source);
					}
				}
			)
		);
	}
}
