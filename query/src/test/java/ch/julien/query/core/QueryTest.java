package ch.julien.query.core;

import ch.julien.common.delegate.Predicate;
import ch.julien.query.Traversable;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static ch.julien.query.core.Query.from;
import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;

public class QueryTest {
	@Test
	public void testFromIterable() {
		Traversable<Integer> actual = Query.from(asList(1, 2));

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "leeloo");
		map.put(2, "multipass");

		Traversable<Map.Entry<Integer, String>> actual = Query.from(map);

		assertThat(actual).containsExactly(
			entry(1, "leeloo"), entry(2, "multipass")
		);
	}

	@Test
	public void testFromArray() {
		Integer[] integers = new Integer[] {1, 2};

		Traversable<Integer> actual = Query.from(integers);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromPrimitiveBooleanArray() {
		boolean[] booleans = new boolean[] {true, false};

		Traversable<Boolean> actual = Query.from(booleans);

		assertThat(actual).containsExactly(true, false);
	}

	@Test
	public void testFromPrimitiveByteArray() {
		byte[] bytes = new byte[] {'A', 'B'};

		Traversable<Byte> actual = Query.from(bytes);

		assertThat(actual).containsExactly(new Byte("65"), new Byte("66"));
	}

	@Test
	public void testFromPrimitiveCharArray() {
		char[] chars = new char[] {'a', 'b'};

		Traversable<Character> actual = Query.from(chars);

		assertThat(actual).containsExactly('a', 'b');
	}

	@Test
	public void testFromPrimitiveDoubleArray() {
		double[] doubles = new double[] {1.0, 2.0};

		Traversable<Double> actual = Query.from(doubles);

		assertThat(actual).containsExactly(1.0, 2.0);
	}

	@Test
	public void testFromPrimitiveFloatArray() {
		float[] floats = new float[] {1.0f, 2.0f};

		Traversable<Float> actual = Query.from(floats);

		assertThat(actual).containsExactly(1.0f, 2.0f);
	}

	@Test
	public void testFromPrimitiveIntegerArray() {
		int[] ints = new int[] {1, 2};

		Traversable<Integer> actual = Query.from(ints);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testFromPrimitiveLongArray() {
		long[] longs = new long[] {1L, 2L};

		Traversable<Long> actual = Query.from(longs);

		assertThat(actual).containsExactly(1L, 2L);
	}

	@Test
	public void testFromPrimitiveShortArray() {
		short[] longs = new short[] {1, 2};

		Traversable<Short> actual = Query.from(longs);

		assertThat(actual).containsExactly((short)1, (short)2);
	}

	// elegant way to suppress generic array creation warning
	private <TA, TB> Map.Entry entry(TA a, TB b) {
		return new HashMap.SimpleEntry<TA, TB>(a, b);
	}
}
