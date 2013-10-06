package ch.julien.common.monad;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class OptionTest {
	@Test
	public void testHasValueOfSome() {
		Option<Boolean> actual = Option.some(true);

		assertThat(actual.hasValue()).isTrue();
	}

	@Test
	public void testHasValueOfNone() {
		Option<Boolean> actual = Option.none();

		assertThat(actual.hasValue()).isFalse();
	}

	@Test
	public void testGetOfSome() {
		Option<Boolean> actual = Option.some(true);

		assertThat(actual.get()).isTrue();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetOfNone() {
		Option<Boolean> actual = Option.none();

		actual.get();
	}

	@Test
	public void testGetOrElseOfNone() {
		Option<Boolean> actual = Option.none();

		assertThat(actual.getOrElse(true)).isTrue();
	}

	@Test
	public void testGetOrElseOfSome() {
		Option<Boolean> actual = Option.some(true);

		assertThat(actual.getOrElse(false)).isTrue();
	}

	@Test
	public void testOrOfNone() {
		Option<Boolean> actual = Option.none();
		Option<Boolean> or = Option.some(true);

		assertThat(actual.or(or)).isSameAs(or);
		assertThat(actual.or(or).get()).isTrue();
	}

	@Test
	public void testOrOfSome() {
		Option<Boolean> actual = Option.some(true);
		Option<Boolean> or = Option.some(false);

		assertThat(actual.or(or)).isSameAs(actual);
		assertThat(actual.or(or).get()).isTrue();
	}

	@Test
	public void testOrOptionOfNone() {
		Option<Boolean> actual = Option.none();

		assertThat(actual.orOption(true).get()).isTrue();
	}

	@Test
	public void testOrOptionOfSome() {
		Option<Boolean> actual = Option.some(true);

		assertThat(actual.orOption(false)).isSameAs(actual);
		assertThat(actual.orOption(false).get()).isTrue();
	}
}
