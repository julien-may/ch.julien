package ch.julien.query.core;

import ch.julien.common.delegate.Accumulator;
import ch.julien.common.delegate.EqualityComparator;
import ch.julien.common.delegate.Func;
import ch.julien.common.delegate.Predicate;
import ch.julien.common.monad.Option;
import ch.julien.query.Lookup;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.entry;

public class TraversableImplTests {
	private static class Person {
		public static Person withFirstAndLastName(String firstName, String lastName) {
			return new Person(firstName, lastName);
		}

		public static Person withFirstName(String firstName) {
			return new Person(firstName, null);
		}

		public final String firstName;
		private final String lastName;

		public Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Person person = (Person) o;

			return !(firstName != null
				? !firstName.equals(person.firstName)
				: person.firstName != null) && !(lastName != null
					? !lastName.equals(person.lastName)
					: person.lastName != null);

		}

		@Override
		public int hashCode() {
			int result = firstName != null ? firstName.hashCode() : 0;
			result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "Person{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				'}';
		}
	}

	private <TSource> TraversableImpl<TSource> from(Iterable<TSource> source) {
		return new TraversableImpl<TSource>(source);
	}

	private TraversableImpl<Character> from(char[] source) {
		return new CharArrayTraversable(source);
	}

	@Test
	public void testAggregate() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Integer actual = from(integers).aggregate(
			new Accumulator<Integer, Integer>() {
				@Override
				public Integer accumulate(Integer a, Integer b) {
					return a + b;
				}
			}
		);

		assertThat(actual).isEqualTo(10);
	}

	@Test
	public void testAggregateWithInitial() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Integer actual = from(integers).aggregate(10,
			new Accumulator<Integer, Integer>() {
				@Override
				public Integer accumulate(Integer a, Integer b) {
					return a + b;
				}
			}
		);

		assertThat(actual).isEqualTo(20);
	}

	@Test
	public void testAggregateWithInitialAndResultSelector() {
		List<Integer> integers = asList(1, 2, 3, 4);

		String actual = from(integers).aggregate(10,
			new Accumulator<Integer, Integer>() {
				@Override
				public Integer accumulate(Integer a, Integer b) {
					return a + b;
				}
			},
			new Func<Integer, String>() {
				@Override
				public String invoke(Integer arg) {
					return arg.toString();
				}
			}
		);

		assertThat(actual).isEqualTo("20");
	}

	@Test
	public void testAllEvenNumbers() {
		List<Integer> a = asList(1, 2);

		boolean actual = from(a).all(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return arg % 2 == 0;
				}
			}
		);

		assertThat(actual).isFalse();
	}

	@Test
	public void testAllNumbersGreaterZero() {
		List<Integer> integers = asList(1, 2);

		boolean actual = from(integers).all(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return arg > 0;
				}
			}
		);

		assertThat(actual).isTrue();
	}

	@Test
	public void testAnyReturnsTrue() {
		List<Integer> integers = asList(1, 2);

		boolean actual = from(integers).any();

		assertThat(actual).isTrue();
	}

	@Test
	public void testAnyReturnsFalse() {
		List<Integer> integers = asList();

		boolean actual = from(integers).any();

		assertThat(actual).isFalse();
	}

	@Test
	public void testAnyWithPredicateReturnsTrue() {
		List<Integer> integers = asList(1, 2);

		boolean actual = from(integers).any(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return arg % 2 == 0;
				}
			}
		);

		assertThat(actual).isTrue();
	}

	@Test
	public void testAnyWithPredicateReturnsFalse() {
		List<Integer> integers = asList(1, 2);

		boolean actual = from(integers).any(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return false;
				}
			}
		);

		assertThat(actual).isFalse();
	}

	@Test
	public void testAsArray() {
		List<Integer> integers = asList(1, 2);

		Integer[] actual = from(integers).asArray(
			new Func<Integer, Integer[]>() {
				@Override
				public Integer[] invoke(Integer arg) {
					return new Integer[arg];
				}
			}
		);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testAsArrayList() {
		List<Integer> integers = asList(1, 2);

		ArrayList<Integer> actual = from(integers).asArrayList();

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testAsCollection() {
		List<Integer> integers = asList(1, 2);

		LinkedList<Integer> actual = from(integers).asCollection(new LinkedList<Integer>());

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testAsHashMap() {
		Person leeloo = Person.withFirstAndLastName("leeloo", "dallas");
		Person korban = Person.withFirstAndLastName("korban", "dallas");

		List<Person> persons = asList(leeloo, korban);

		HashMap<String, Person> actual = from(persons).asHashMap(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		);

		assertThat(actual).contains(entry(leeloo.firstName, leeloo));
		assertThat(actual).contains(entry(korban.firstName, korban));
	}

	@Test
	public void testAsHashMapWithElementSelector() {
		List<Integer> integers = asList(1, 2);

		HashMap<Integer, String> actual = from(integers).asHashMap(
			new Func<Integer, Integer>() {
				@Override
				public Integer invoke(Integer arg) {
					return arg;
				}
			},
			new Func<Integer, String>() {
				@Override
				public String invoke(Integer arg) {
					return arg.toString();
				}
			}
		);

		assertThat(actual).contains(entry(1, "1"));
		assertThat(actual).contains(entry(2, "2"));
	}

	@Test
	public void testAsHashSet() {
		List<Integer> integers = asList(1, 2);

		HashSet<Integer> actual = from(integers).asHashSet();

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testAsHashSetWithKeySelector() {
		Person leeloo = Person.withFirstAndLastName("leeloo", "dallas");
		Person korban = Person.withFirstAndLastName("korban", "dallas");

		List<Person> persons = asList(leeloo, korban);

		HashSet<String> actual = from(persons).asHashSet(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		);

		assertThat(actual).containsExactly(leeloo.firstName, korban.firstName);
	}

	@Test
	public void testAsLookup() {
		List<Integer> integers = asList(1, 2, 3, 1, 3, 3);

		Lookup<Integer, Integer> actual = from(integers).asLookup(
			new Func<Integer, Integer>() {
				@Override
				public Integer invoke(Integer arg) {
					return arg;
				}
			}
		);

		assertThat(actual).hasSize(3);
		assertThat(actual.getItems(1)).containsExactly(1, 1);
		assertThat(actual.getItems(2)).containsExactly(2);
		assertThat(actual.getItems(3)).containsExactly(3, 3, 3);
	}

	@Test
	public void testAsLookupWithEqualityComparator() {
		Person leeloo1 = Person.withFirstAndLastName("leeloo", "dallas");
		Person leeloo2 = Person.withFirstAndLastName("leeloo", "multipass");
		Person korban = Person.withFirstAndLastName("korban", "dallas");

		List<Person> persons = asList(leeloo1, leeloo2, korban);

		Lookup<Person, Person> actual = from(persons).asLookup(
			new Func<Person, Person>() {
				@Override
				public Person invoke(Person arg) {
					return arg;
				}
			},
			new EqualityComparator<Person>() {
				@Override
				public boolean equals(Person first, Person second) {
					return first.firstName.equals(second.firstName);
				}
			}
		);

		assertThat(actual).hasSize(2);
		assertThat(actual.getItems(leeloo1)).containsExactly(leeloo1, leeloo2);
		assertThat(actual.getItems(leeloo2)).containsExactly(leeloo1, leeloo2);
		assertThat(actual.getItems(korban)).containsExactly(korban);
	}

	@Test
	public void testAsLookupWithElementSelector() {
		Person leeloo1 = Person.withFirstAndLastName("leeloo", "dallas");
		Person leeloo2 = Person.withFirstAndLastName("leeloo", "multipass");
		Person korban = Person.withFirstAndLastName("korban", "dallas");

		List<Person> persons = asList(leeloo1, leeloo2, korban);

		Lookup<String, Person> actual = from(persons).asLookup(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			},
			new Func<Person, Person>() {
				@Override
				public Person invoke(Person arg) {
					return arg;
				}
			}
		);

		assertThat(actual).hasSize(2);
		assertThat(actual.getItems("leeloo")).containsExactly(leeloo1, leeloo2);
		assertThat(actual.getItems("korban")).containsExactly(korban);
	}

	@Test
	public void testConcatListOfSameType() {
		List<Integer> first = asList(1, 2);
		List<Integer> second = asList(3, 4);

		Iterable<Integer> actual = from(first).concat(second);

		assertThat(actual).containsExactly(1, 2, 3, 4);
	}

	@Test
	public void testConcatListOfSameBaseType() {
		List<Number> first = Arrays.<Number>asList(1L, 2L);
		List<Integer> second = asList(3, 4);

		Iterable<Number> actual = from(first).concat(second);

		assertThat(actual).containsExactly(1L, 2L, 3, 4);
	}

	@Test
	public void testConcatArray() {
		List<Integer> first = asList(1, 2);
		Integer[] second = {3, 4};

		Iterable<Integer> actual = from(first).concat(second);

		assertThat(actual).containsExactly(1, 2, 3, 4);
	}

	@Test
	public void testCountFromListIsGreaterThanZero() {
		List<Integer> integers = asList(1, 2);

		long count = from(integers).count();

		assertThat(count).isEqualTo(2);
	}

	@Test
	public void testCountFromListIsZero() {
		List<Integer> integers = asList();

		long count = from(integers).count();

		assertThat(count).isEqualTo(0);
	}

	@Test
	public void testCountFromIterableIsGreaterThanZero() {
		List<Integer> integers = asList(1, 2);

		long count = from(integers).select(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return true;
				}
			}
		).count();

		assertThat(count).isEqualTo(2);
	}

	@Test
	public void testCountFromIterableIsZero() {
		List<Integer> integers = asList();

		long count = from(integers).select(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return false;
				}
			}
		).count();

		assertThat(count).isEqualTo(0);
	}

	@Test
	public void testCountUsesSizeFromCollection() {
		Collection<Integer> integers = new ArrayList<Integer>() {
			@Override
			public int size() {
				return 10;
			}
		};

		long count = from(integers).count();

		assertThat(count).isEqualTo(10);
	}

	@Test
	public void testDifference() {
		List<Integer> a = asList(1, 2, 3, 4);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).difference(b);

		assertThat(actual).containsExactly(2, 3);
	}

	@Test
	public void testDifferenceWhereSelfIsBiggerThanOther() {
		List<Integer> a = asList(1, 2, 3, 4, 1, 4);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).difference(b);

		assertThat(actual).containsExactly(2, 3);
	}

	@Test
	public void testDifferenceWhereSelfIsSmallerThanOther() {
		List<Integer> a = asList(2, 3);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).difference(b);

		assertThat(actual).containsExactly(2, 3);
	}

	@Test
	public void testDistinct() {
		List<Integer> integers = asList(1, 2, 1, 1);

		Iterable<Integer> actual = from(integers).distinct();

		assertThat(actual).hasSize(2);
		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testDistinctWithEqualityComparator() {
		List<Person> persons = asList(
			Person.withFirstName("leeloo"), Person.withFirstName("korben"), Person.withFirstName("leeloo"), Person.withFirstName("leeloo"));

		Iterable<Person> actual = from(persons).distinct(
			new EqualityComparator<Person>() {
				@Override
				public boolean equals(Person first, Person second) {
					return first.firstName.equals(second.firstName);
				}
			}
		);

		assertThat(actual).hasSize(2);
		assertThat(actual).containsExactly(Person.withFirstName("leeloo"), Person.withFirstName("korben"));
	}

	@Test
	public void testFirstReturnsSome()  {
		List<Integer> integers = asList(1, 2, 3, 4);

		Option<Integer> actual = from(integers).first();

		assertThat(actual.get()).isEqualTo(1);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFirstReturnsNone()  {
		List<Integer> integers = asList();

		from(integers).first().get();
	}

	@Test
	public void testFirstWithPredicateReturnsSome() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Option<Integer> actual = from(integers).first(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return arg % 2 == 0;
				}
			}
		);

		assertThat(actual.get()).isEqualTo(2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFirstWithPredicateReturnsNone() {
		List<Integer> integers = asList(1, 2, 3, 4);

		from(integers).first(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return false;
				}
			}
		).get();
	}

	@Test
	public void testFlat() {
		List<String> strings = asList("abc", "a", "bb");

		Iterable<Character> actual = from(strings).flat(
			new Func<String, Iterable<Character>>() {
				@Override
				public Iterable<Character> invoke(String arg) {
					return from(arg.toCharArray());
				}
			}
		);

		assertThat(actual).containsExactly('a', 'b', 'c', 'a', 'b', 'b');
	}

	@Test
	public void testIntersect() {
		List<Integer> a = asList(1, 2, 3, 4);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).intersect(b);

		assertThat(actual).containsExactly(1, 4);
	}

	@Test
	public void testIntersectWhereSelfIsBiggerThanOther() {
		List<Integer> a = asList(1, 2, 3, 4, 1, 8, 9);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).intersect(b);

		assertThat(actual).containsExactly(1, 4);
	}

	@Test
	public void testIntersectWhereSelfIsSmallerThanOther() {
		List<Integer> a = asList(1, 4);
		List<Integer> b = asList(4, 5, 6, 1);

		Iterable<Integer> actual = from(a).intersect(b);

		assertThat(actual).containsExactly(1, 4);
	}

	@Test
	public void testLastReturnsSome()  {
		List<Integer> integers = asList(1, 2, 3, 4);

		Option<Integer> actual = from(integers).last();

		assertThat(actual.get()).isEqualTo(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLastReturnsNone()  {
		List<Integer> integers = asList();

		from(integers).last().get();
	}

	@Test
	public void testLastWithPredicateReturnsSome() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Option<Integer> actual = from(integers).last(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return arg % 2 == 0;
				}
			}
		);

		assertThat(actual.get()).isEqualTo(4);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testLastWithPredicateReturnsNone() {
		List<Integer> integers = asList(1, 2, 3, 4);

		from(integers).last(
			new Predicate<Integer>() {
				@Override
				public boolean invoke(Integer arg) {
					return false;
				}
			}
		).get();
	}

	@Test
	public void testMapFromIntegerToString() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Iterable<String> actual = from(integers).map(
			new Func<Integer, String>() {
				@Override
				public String invoke(Integer arg) {
					return arg.toString();
				}
			}
		);

		assertThat(actual).containsExactly("1", "2", "3", "4");
	}

	@Test
	public void testMapFromIntegerToBooleanToString() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Iterable<String> actual = from(integers).map(
			new Func<Integer, Boolean>() {
				@Override
				public Boolean invoke(Integer arg) {
					return arg % 2 == 0;
				}
			}
		).map(
			new Func<Boolean, String>() {
				@Override
				public String invoke(Boolean arg) {
					return arg.toString();
				}
			}
		);

		assertThat(actual).containsExactly(
			Boolean.toString(false), Boolean.toString(true), Boolean.toString(false), Boolean.toString(true));
	}

	class MockedArrayList<E> extends ArrayList<E> {
		class Counter {
			private int count = 0;

			public void inc() {
				this.count++;
			}

			public int getCount() {
				return this.count;
			}
		}

		private final Counter counter = new Counter();

		public int getCallCount() {
			return this.counter.getCount();
		}

		@Override
		public Iterator<E> iterator() {
			final Iterator<E> iterator = super.iterator();

			return new Iterator<E>() {
				@Override
				public boolean hasNext() {

					return iterator.hasNext();
				}

				@Override
				public E next() {
					counter.inc();
					return iterator.next();
				}

				@Override
				public void remove() {
					iterator.remove();
				}
			};
		}
	}

	@Test
	public void testMapLazyness() {
		MockedArrayList<Integer> integers = new MockedArrayList<Integer>();
		integers.add(1);
		integers.add(2);
		integers.add(3);
		integers.add(4);

		Iterable<String> actual = from(integers).map(
			new Func<Integer, String>() {
				@Override
				public String invoke(Integer arg) {
					return arg.toString();
				}
			}
		).map(
			new Func<String, String>() {
				@Override
				public String invoke(String arg) {
					return arg;
				}
			}
		);

		assertThat(integers.getCallCount()).isZero();

		for (String ignored : actual) {}

		assertThat(integers.getCallCount()).isEqualTo(4);
	}

	@Test
	public void testReverse() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Iterable<Integer> actual = from(integers).reverse();

		assertThat(actual).containsExactly(4, 3, 2, 1);
	}

	@Test
	public void testSkip() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Iterable<Integer> actual = from(integers).skip(2);

		assertThat(actual).containsExactly(3, 4);
	}

	@Test
	public void testTake() {
		List<Integer> integers = asList(1, 2, 3, 4);

		Iterable<Integer> actual = from(integers).take(2);

		assertThat(actual).containsExactly(1, 2);
	}

	@Test
	public void testSkipAndTakeAsSlice() {
		List<Integer> integers = asList(1, 2, 3, 4, 5, 6);

		Iterable<Integer> actual = from(integers).skip(2).take(2);

		assertThat(actual).containsExactly(3, 4);
	}

	@Test
	public void testSortBy() {
		List<Integer> integers = asList(2, 4, 1, 3);

		Iterable<Integer> actual = from(integers).sortBy(
			new Func<Integer, Integer>() {
				@Override
				public Integer invoke(Integer arg) {
					return arg;
				}
			}
		);

		assertThat(actual).containsExactly(1, 2, 3, 4);
	}

	@Test
	public void testSortByString() {
		List<Person> persons = asList(
			Person.withFirstName("b"),
			Person.withFirstName("a")
		);

		Iterable<Person> actual = from(persons).sortBy(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		);

		assertThat(actual).containsExactly(
			Person.withFirstName("a"), Person.withFirstName("b")
		);
	}

	@Test
	public void testSortByStringAsArrayList() {
		List<Person> persons = asList(
			Person.withFirstName("b"),
			Person.withFirstName("a")
		);

		List<Person> actual = from(persons).sortBy(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		).asArrayList();

		assertThat(actual).containsExactly(
			Person.withFirstName("a"), Person.withFirstName("b")
		);
	}

	@Test
	public void testSortByDescending() {
		List<Integer> integers = asList(2, 4, 1, 3);

		Iterable<Integer> actual = from(integers).sortByDescending(
			new Func<Integer, Integer>() {
				@Override
				public Integer invoke(Integer arg) {
					return arg;
				}
			}
		);

		assertThat(actual).containsExactly(4, 3, 2, 1);
	}

	@Test
	public void testThenBy() {
		List<Person> persons = asList(
			Person.withFirstAndLastName("Leeloo", "Dallas"),
			Person.withFirstAndLastName("Ruby", "Rhod"),
			Person.withFirstAndLastName("Korben", "Dallas"),
			Person.withFirstAndLastName("Vito", "Cornelius"),
			Person.withFirstAndLastName("Ruby", "Zorg")
		);

		Iterable<Person> actual = from(persons).sortBy(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		).thenBy(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.lastName;
				}
			}
		);

		assertThat(actual).containsExactly(
			Person.withFirstAndLastName("Korben", "Dallas"),
			Person.withFirstAndLastName("Leeloo", "Dallas"),
			Person.withFirstAndLastName("Ruby", "Rhod"),
			Person.withFirstAndLastName("Ruby", "Zorg"),
			Person.withFirstAndLastName("Vito", "Cornelius")
		);
	}

	@Test
	public void testThenByDescending() {
		List<Person> persons = asList(
			Person.withFirstAndLastName("Leeloo", "Dallas"),
			Person.withFirstAndLastName("Ruby", "Rhod"),
			Person.withFirstAndLastName("Korben", "Dallas"),
			Person.withFirstAndLastName("Vito", "Cornelius"),
			Person.withFirstAndLastName("Ruby", "Zorg")
		);

		Iterable<Person> actual = from(persons).sortBy(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.firstName;
				}
			}
		).thenByDescending(
			new Func<Person, String>() {
				@Override
				public String invoke(Person arg) {
					return arg.lastName;
				}
			}
		);

		assertThat(actual).containsExactly(
			Person.withFirstAndLastName("Korben", "Dallas"),
			Person.withFirstAndLastName("Leeloo", "Dallas"),
			Person.withFirstAndLastName("Ruby", "Zorg"),
			Person.withFirstAndLastName("Ruby", "Rhod"),
			Person.withFirstAndLastName("Vito", "Cornelius")
		);
	}
}
