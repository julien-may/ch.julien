package ch.julien.query.util;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;


public class PredicatesTest {

	@Test
	public void testNotNull() {
		// test basic cases
		assertThat(Predicates.notNull().invoke(null)).isFalse();
		assertThat(Predicates.notNull().invoke("my object")).isTrue();
	}
	
	@Test
	public void testNotEmptyCollection() {
		// test basic cases
		assertThat(Predicates.notEmptyCollection().invoke(null)).isFalse();
		assertThat(Predicates.notEmptyCollection().invoke(asList())).isFalse();
		assertThat(Predicates.notEmptyCollection().invoke(asList((String) null))).isTrue();
		assertThat(Predicates.notEmptyCollection().invoke(asList("my object"))).isTrue();
		
		// test a collection other than list
		assertThat(Predicates.notEmptyCollection().invoke(new HashSet<String>(asList("my object")))).isTrue();
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
		String[] array3 = {"my object"};	// not null element
		assertThat(Predicates.notEmptyArray().invoke(array3)).isTrue();
	}

}
