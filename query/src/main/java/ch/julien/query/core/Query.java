package ch.julien.query.core;

import ch.julien.query.Traversable;

import java.util.Map;

public final class Query {
	private Query() {}

	public static <T> Traversable<T> from(Iterable<T> iterable) {
		return new TraversableImpl<T>(iterable);
	}

	public static <TKey, TElement> Traversable<Map.Entry<TKey, TElement>> from(Map<TKey, TElement> map) {
		return new TraversableImpl<Map.Entry<TKey, TElement>>(map.entrySet());
	}

	public static <T> Traversable<T> from(T[] array) {
		return new TraversableImpl<T>(array);
	}

	public static Traversable<Boolean> from(boolean[] array) {
		return new BooleanArrayTraversable(array);
	}

	public static Traversable<Byte> from(byte[] array) {
		return new ByteArrayTraversable(array);
	}

	public static Traversable<Character> from(char[] array) {
		return new CharArrayTraversable(array);
	}

	public static Traversable<Double> from(double[] array) {
		return new DoubleArrayTraversable(array);
	}

	public static Traversable<Float> from(float[] array) {
		return new FloatArrayTraversable(array);
	}

	public static Traversable<Integer> from(int[] array) {
		return new IntArrayTraversable(array);
	}

	public static Traversable<Long> from(long[] array) {
		return new LongArrayTraversable(array);
	}

	public static Traversable<Short> from(short[] array) {
		return new ShortArrayTraversable(array);
	}
}
