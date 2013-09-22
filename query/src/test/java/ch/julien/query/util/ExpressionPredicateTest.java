package ch.julien.query.util;

import static ch.julien.query.util.Predicates.all;
import static ch.julien.query.util.Predicates.none;
import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import ch.julien.common.delegate.Predicate;


public class ExpressionPredicateTest {
	
	Predicate<Object> all = all();
	Predicate<Object> none = none();
	
	@Test
	public void testThat() {
		assertThat(Predicates.that(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).invoke(null)).isFalse();
	}

	@Test
	public void testExpressionPredicateAnd() {
		assertThat(Predicates.that(all).and(all).invoke(null)).isTrue();
		assertThat(Predicates.that(all).and(none).invoke(null)).isFalse();
		assertThat(Predicates.that(none).and(all).invoke(null)).isFalse();
		assertThat(Predicates.that(none).and(none).invoke(null)).isFalse();
	}
	
	@Test
	public void testExpressionPredicateOr() {
		assertThat(Predicates.that(all).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(all).or(none).invoke(null)).isTrue();
		assertThat(Predicates.that(none).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).or(none).invoke(null)).isFalse();
	}
	
	@Test
	public void testExpressionPredicatePrecedence() {
		assertThat(Predicates.that(all).or(all).and(all).invoke(null)).isTrue();
		assertThat(Predicates.that(all).or(all).and(none).invoke(null)).isTrue();
		assertThat(Predicates.that(all).or(none).and(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).or(all).and(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).or(none).and(all).invoke(null)).isFalse();
		assertThat(Predicates.that(none).or(all).and(none).invoke(null)).isFalse();
		assertThat(Predicates.that(all).or(none).and(none).invoke(null)).isTrue();
		assertThat(Predicates.that(none).or(none).and(none).invoke(null)).isFalse();
		
		assertThat(Predicates.that(all).and(all).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(all).and(all).or(none).invoke(null)).isTrue();
		assertThat(Predicates.that(all).and(none).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).and(all).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).and(none).or(all).invoke(null)).isTrue();
		assertThat(Predicates.that(none).and(all).or(none).invoke(null)).isFalse();
		assertThat(Predicates.that(all).and(none).or(none).invoke(null)).isFalse();
		assertThat(Predicates.that(none).and(none).or(none).invoke(null)).isFalse();
	}
	
}
