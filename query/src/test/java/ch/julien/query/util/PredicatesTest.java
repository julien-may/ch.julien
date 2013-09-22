package ch.julien.query.util;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.julien.common.delegate.Predicate;
import ch.julien.query.core.Query;


public class PredicatesTest {

	@Test
	public void testNotNull() {
		// test basic cases
		assertThat(Predicates.notNull().invoke(null)).isFalse();
		assertThat(Predicates.notNull().invoke("my-object")).isTrue();
	}
	
	@Test
	public void testNotEmptyCollection() {
		// test basic cases
		assertThat(Predicates.notEmptyCollection().invoke(null)).isFalse();
		assertThat(Predicates.notEmptyCollection().invoke(asList())).isFalse();
		assertThat(Predicates.notEmptyCollection().invoke(asList((String) null))).isTrue();
		assertThat(Predicates.notEmptyCollection().invoke(asList("my-object"))).isTrue();
		
		// test a collection other than list
		assertThat(Predicates.notEmptyCollection().invoke(new HashSet<String>(asList("my-object")))).isTrue();
	}
	
	@Test
	public void testNotEmptyMap() {
		// test basic cases
		assertThat(Predicates.notEmptyMap().invoke(null)).isFalse();
		Map<String, Object> map1 = new HashMap<String, Object>();
											// no entry
		assertThat(Predicates.notEmptyMap().invoke(map1)).isFalse();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put(null, null);				// null key
		assertThat(Predicates.notEmptyMap().invoke(map2)).isTrue();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("key", null);				// null value
		assertThat(Predicates.notEmptyMap().invoke(map3)).isTrue();
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("key", "value");			// not null value
		assertThat(Predicates.notEmptyMap().invoke(map4)).isTrue();
		
	}
	
	@Test
	public void testNotEmptyArray() {
		// test basic cases
		assertThat(Predicates.notEmptyArray().invoke(null)).isFalse();
		String[] array1 = {};				// no element
		assertThat(Predicates.notEmptyArray().invoke(array1)).isFalse();
		String[] array2 = {null};			// null element
		assertThat(Predicates.notEmptyArray().invoke(array2)).isTrue();
		String[] array3 = {"my-object"};	// not null element
		assertThat(Predicates.notEmptyArray().invoke(array3)).isTrue();
	}
	
	private static class TestType {}
	private static class TestSubtype extends TestType {}
	
	@Test
	public void testElementOfInstance() {
		// test basic cases
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(null)).isFalse();
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(new Object())).isFalse();		// super type
		assertThat(Predicates.elementOfInstance(TestType.class).invoke("non-parent-object")).isFalse();	// any type
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(new Object() {})).isFalse();		// any anonymous type
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(new TestType())).isTrue();		// THE type
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(new TestSubtype())).isTrue();	// subtype
		assertThat(Predicates.elementOfInstance(TestType.class).invoke(new TestType() {})).isTrue();	// anonymous subtype
	}
	
	@Test
	public void integrationtestElementOfInstanceTo() {
		List<Object> list = asList(new Object(), "my-string");
		List<String> castedList = Query.from(list).select(Predicates.elementOfInstance(String.class)).map(Funcs.to(String.class)).asArrayList();
		assertThat(castedList.size()).isEqualTo(1);
		assertThat(castedList.get(0)).isEqualTo((String) list.get(1));
	}
	
	private Predicate<Object> all = new Predicate<Object>() {
		@Override
		public boolean invoke(Object arg) {
			return true;
		}
		public String toString() { return "all"; };
	};
	private Predicate<Object> none = new Predicate<Object>() {
		@Override
		public boolean invoke(Object arg) {
			return false;
		}
		public String toString() { return "none"; };
	};

	@Test
	public void testNot() {
		assertThat(Predicates.not(all).invoke(null)).isFalse();
		assertThat(Predicates.not(none).invoke(null)).isTrue();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testAnd() {
		assertThat(Predicates.and(all, all).invoke(null)).isTrue();
		assertThat(Predicates.and(all, none).invoke(null)).isFalse();
		assertThat(Predicates.and(none, all).invoke(null)).isFalse();
		assertThat(Predicates.and(none, none).invoke(null)).isFalse();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testOr() {
		assertThat(Predicates.or(all, all).invoke(null)).isTrue();
		assertThat(Predicates.or(all, none).invoke(null)).isTrue();
		assertThat(Predicates.or(none, all).invoke(null)).isTrue();
		assertThat(Predicates.or(none, none).invoke(null)).isFalse();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testXor() {
		assertThat(Predicates.xor(all, all).invoke(null)).isFalse();
		assertThat(Predicates.xor(all, none).invoke(null)).isTrue();
		assertThat(Predicates.xor(none, all).invoke(null)).isTrue();
		assertThat(Predicates.xor(none, none).invoke(null)).isFalse();
	}
	
	@Test
	public void testNotEmptyString() {
		assertThat(Predicates.notEmptyString().invoke(null)).isFalse();
		assertThat(Predicates.notEmptyString().invoke("")).isFalse();
		assertThat(Predicates.notEmptyString().invoke("not empty")).isTrue();
	}
	
	@Test
	public void testStringStartingWith() {
		assertThat(Predicates.stringStartingWith("prefix").invoke(null)).isFalse();
		assertThat(Predicates.stringStartingWith("prefix").invoke("")).isFalse();
		assertThat(Predicates.stringStartingWith("prefix").invoke("suffix")).isFalse();
		assertThat(Predicates.stringStartingWith("prefix").invoke("prefix")).isTrue();
		assertThat(Predicates.stringStartingWith("prefix").invoke(" prefix")).isFalse();
		assertThat(Predicates.stringStartingWith("prefix").invoke("prefix-suffix")).isTrue();
	}
	
	@Test
	public void testStringEndingWith() {
		assertThat(Predicates.stringEndingWith("suffix").invoke(null)).isFalse();
		assertThat(Predicates.stringEndingWith("suffix").invoke("")).isFalse();
		assertThat(Predicates.stringEndingWith("suffix").invoke("prefix")).isFalse();
		assertThat(Predicates.stringEndingWith("suffix").invoke("suffix")).isTrue();
		assertThat(Predicates.stringEndingWith("suffix").invoke("suffix ")).isFalse();
		assertThat(Predicates.stringEndingWith("suffix").invoke("prefix-suffix")).isTrue();
	}
	
	@Test
	public void testStringMatching() {
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke(null)).isFalse();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke("")).isFalse();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke("a")).isFalse();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke(" a")).isTrue();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke(" a ")).isFalse();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke(" b")).isTrue();
		assertThat(Predicates.stringMatching("^.[ab]{1}$").invoke(" c")).isFalse();
	}
	
}
