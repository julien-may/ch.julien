package ch.julien.common.util;

import org.junit.Test;

import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;

public class MapBuilderTest {
	@Test
	public void testHashMap() {
		Map<Integer, String> actual = MapBuilder.<Integer, String>hashMap()
			.key(10).value("a")
			.key(20).value("b")
			.key(30).value("c")
		.build();

		assertThat(actual).hasSize(3);
		assertThat(actual).contains(
			entry(10, "a"), entry(20, "b"), entry(30, "c")
		);
	}
}
