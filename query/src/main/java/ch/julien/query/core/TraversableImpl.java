package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.*;
import ch.julien.common.monad.Indexed;
import ch.julien.common.monad.Option;
import ch.julien.query.Group;
import ch.julien.query.Lookup;
import ch.julien.query.OrderedTraversable;
import ch.julien.query.Traversable;

import java.util.*;

import static ch.julien.common.util.Comparators.fromEqualityComparator;

class TraversableImpl<TSource> implements Traversable<TSource> {
	protected final Iterable<TSource> source;

	public TraversableImpl(Iterable<TSource> source) {
		Check.notNull(source, "source");

		this.source = source;
	}

	public TraversableImpl(final TSource[] source) {
		this.source = new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new ArrayIterator<TSource>(source);
				}
			}
		);
	}

	@Override
	public TSource aggregate(Accumulator<TSource, TSource> accumulator) {
		Iterator<? extends TSource> iterator = this.source.iterator();

		if (!iterator.hasNext()) {
			throw new NoSuchElementException();
		}

		return aggregate(iterator, iterator.next(), accumulator,
			new Func<TSource, TSource>() {
				@Override
				public TSource invoke(TSource arg) {
					return arg;
				}
			}
		);
	}

	@Override
	public <TAccumulate> TAccumulate aggregate(TAccumulate initial, Accumulator<TAccumulate, TSource> accumulator) {
		return aggregate(this.source.iterator(), initial, accumulator,
			new Func<TAccumulate, TAccumulate>() {
				@Override
				public TAccumulate invoke(TAccumulate arg) {
					return arg;
				}
			}
		);
	}

	@Override
	public <TAccumulate, TResult> TResult aggregate(TAccumulate initial, Accumulator<TAccumulate, TSource> accumulator,
		Func<TAccumulate, TResult> resultSelector) {

		return aggregate(source.iterator(), initial, accumulator, resultSelector);
	}

	private <TAccumulate, TResult> TResult aggregate(Iterator<? extends TSource> source, TAccumulate initial,
		Accumulator<TAccumulate, TSource> accumulator, Func<TAccumulate, TResult> resultSelector) {

		Check.notNull(accumulator, "accumulator");
		Check.notNull(resultSelector, "resultSelector");

		TAccumulate result = initial;

		while (source.hasNext()) {
			result = accumulator.accumulate(result, source.next());
		}

		return resultSelector.invoke(result);
	}

	@Override
	public boolean all(Predicate<? super TSource> predicate) {
		Check.notNull(predicate, "predicate");

		for (TSource element : this.source) {
			if (!predicate.invoke(element)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean any() {
		return this.source.iterator().hasNext();
	}

	@Override
	public boolean any(Predicate<? super TSource> predicate) {
		return select(predicate).any();
	}

	@Override
	public TSource[] asArray(Func<Integer, TSource[]> allocator) {
		ArrayList<TSource> list = asArrayList();

		TSource[] result = allocator.invoke(list.size());

		if (result.length < list.size()) {
			throw new UnsupportedOperationException(
				"Length of the returned array must be equal or greater than: " + list.size());
		}

		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}

		return result;
	}

	@Override
	public ArrayList<TSource> asArrayList() {
		ArrayList<TSource> list = new ArrayList<TSource>();

		for (TSource item : this.source) {
			list.add(item);
		}

		return list;
	}

	@Override
	public <TCollection extends Collection<TSource>> TCollection asCollection(TCollection collection) {
		Check.notNull(collection, "collection");

		for (TSource item : this.source) {
			collection.add(item);
		}

		return collection;
	}

	@Override
	public <TKey> HashMap<TKey, TSource> asHashMap(Func<TSource, TKey> keySelector) {
		return asHashMap(keySelector,
			new Func<TSource, TSource>() {
				@Override
				public TSource invoke(TSource element) {
					return element;
				}
			}
		);
	}

	@Override
	public <TKey, TElement> HashMap<TKey, TElement> asHashMap(Func<TSource, TKey> keySelector, Func<TSource, TElement> elementSelector) {
		Check.notNull(keySelector, "keySelector");
		Check.notNull(elementSelector, "elementSelector");

		HashMap<TKey, TElement> map = new HashMap<TKey, TElement>();

		for (TSource item : source) {
			map.put(keySelector.invoke(item), elementSelector.invoke(item));
		}

		return map;
	}

	@Override
	public HashSet<TSource> asHashSet() {
		HashSet<TSource> set = new HashSet<TSource>();

		for (TSource item : source) {
			set.add(item);
		}

		return set;
	}

	@Override
	public <TKey> HashSet<TKey> asHashSet(Func<TSource, TKey> keySelector) {
		Check.notNull(keySelector, "keySelector");

		HashSet<TKey> set = new HashSet<TKey>();

		for (TSource item : source) {
			set.add(keySelector.invoke(item));
		}

		return set;
	}

	@Override
	public <TKey> Lookup<TKey, TSource> asLookup(Func<TSource, TKey> keySelector) {
		return asLookup(keySelector,
			new Func<TSource, TSource>() {
				@Override
				public TSource invoke(TSource element) {
					return element;
				}
			}, null
		);
	}

	@Override
	public <TKey> Lookup<TKey, TSource> asLookup(Func<TSource, TKey> keySelector, EqualityComparator<TKey> equalityComparator) {
		return asLookup(keySelector,
			new Func<TSource, TSource>() {
				@Override
				public TSource invoke(TSource element) {
					return element;
				}
			}, equalityComparator
		);
	}

	@Override
	public <TKey, TElement> Lookup<TKey, TElement> asLookup(Func<TSource, TKey> keySelector, Func<TSource, TElement> elementSelector) {
		return asLookup(keySelector, elementSelector, null);
	}

	@Override
	public <TKey, TElement> Lookup<TKey, TElement> asLookup(Func<TSource, TKey> keySelector, Func<TSource, TElement> elementSelector,
		EqualityComparator<TKey> equalityComparator) {

		Check.notNull(keySelector, "keySelector");
		Check.notNull(elementSelector, "elementSelector");

		final Map<Key<TKey>, Group<TKey, TElement>> map = new TreeMap<Key<TKey>, Group<TKey, TElement>>(
			new KeyComparator<TKey>(equalityComparator)
		);
		final List<Key<TKey>> orderedKeys = new ArrayList<Key<TKey>>();

		for (TSource item : source) {
			TKey key = keySelector.invoke(item);

			GroupImpl<TKey, TElement> group = (GroupImpl<TKey, TElement>)map.get(new Key<TKey>(key));
			if (group == null) {
				group = new GroupImpl<TKey, TElement>(key);

				Key<TKey> keyli = new Key<TKey>(group.getKey());

				map.put(keyli, group);
				orderedKeys.add(keyli);
			}

			group.add(elementSelector.invoke(item));
		}

		return new LookupImpl<TKey, TElement>(
			new Iterable<Group<TKey, TElement>>() {
				@Override
				public Iterator<Group<TKey, TElement>> iterator() {
					return new OrderedGroupIterator<TKey, TElement>(
						orderedKeys.iterator(), map
					);
				}
			}, map
		);
	}

	@Override
	public Traversable<TSource> concat(final Iterable<? extends TSource> appendant) {
		Check.notNull(appendant, "appendant");

		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new ConcatIterator<TSource>(source.iterator(), appendant.iterator());
				}
			}
		);
	}

	@Override
	public Traversable<TSource> concat(final TSource[] appendant) {
		return concat(
			new TraversableImpl<TSource>(appendant)
		);
	}

	@Override
	public long count() {
		return source instanceof Collection
			? ((Collection)source).size()
			: aggregate(0, new Accumulator<Integer, TSource>() {
				@Override
				public Integer accumulate(Integer count, TSource item) {
					return count + 1;
				}
			}
		);
	}

	@Override
	public Traversable<TSource> difference(Iterable<? extends TSource> other) {
		return difference(other,
			new EqualityComparator<TSource>() {
				@Override
				public boolean equals(TSource first, TSource second) {
					return first.equals(second);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> difference(final Iterable<? extends TSource> other,
		final EqualityComparator<TSource> equalityComparator) {

		Check.notNull(other, "other");

		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new DifferenceIterator<TSource>(source.iterator(), other.iterator(), equalityComparator);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> distinct() {
		return distinct(
			new EqualityComparator<TSource>() {
				@Override
				public boolean equals(TSource first, TSource second) {
					return first == null ? second == null : first.equals(second);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> distinct(final EqualityComparator<TSource> equalityComparator) {
		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new DistinctIterator<TSource>(source.iterator(), equalityComparator);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> each(final Action<TSource> action) {
		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new EachIterator<TSource>(source.iterator(), action);
				}
			}
		);
	}

	@Override
	public Option<TSource> first() {
		if (this.source instanceof List) {
			List<TSource> list = (List<TSource>)this.source;

			return !list.isEmpty() ? Option.some(list.get(0)) : Option.<TSource>none();
		}

		for (TSource item : this.source) {
			return Option.some(item);
		}

		return Option.none();
	}

	@Override
	public Option<TSource> first(Predicate<? super TSource> predicate) {
		return select(predicate).first();
	}

	@Override
	public <TResult> Traversable<TResult> flat(final Func<TSource, Iterable<TResult>> selector) {
		return new TraversableImpl<TResult>(
			new Iterable<TResult>() {
				@Override
				public Iterator<TResult> iterator() {
					return new FlatIterator<TSource, TResult>(source.iterator(), selector);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> intersect(Iterable<? extends TSource> other) {
		return intersect(other,
			new EqualityComparator<TSource>() {
				@Override
				public boolean equals(TSource first, TSource second) {
					return first == second
						|| !(first == null || second == null) && first.equals(second);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> intersect(Iterable<? extends TSource> other, final EqualityComparator<TSource> equalityComparator) {
		final List<TSource> keys = new ArrayList<TSource>();
		final Map<TSource, Boolean> flags = new TreeMap<TSource, Boolean>(
			fromEqualityComparator(equalityComparator)
		);

		for (TSource item : select(new Predicate<TSource>() {
				@Override
				public boolean invoke(TSource item) {
					return !flags.containsKey(item);
				}
			})) {
			flags.put(item, false);
			keys.add(item);
		}

		for (TSource item : other) {
			if (flags.containsKey(item)) {
				flags.put(item, true);
			}
		}

		return new TraversableImpl<TSource>(keys).select(
			new Predicate<TSource>() {
				@Override
				public boolean invoke(TSource arg) {
					return flags.get(arg);
				}
			}
		);
	}

	@Override
	public Option<TSource> last() {
		if (this.source instanceof List) {
			List<TSource> list = (List<TSource>)this.source;

			return !list.isEmpty() ? Option.some(list.get(list.size() - 1)) : Option.<TSource>none();
		}

		Iterator<? extends TSource> iterator = this.source.iterator();

		if (!iterator.hasNext()) {
			return Option.none();
		}

		TSource last = iterator.next();
		while (iterator.hasNext()) {
			last = iterator.next();
		}

		return Option.some(last);
	}

	@Override
	public Option<TSource> last(Predicate<? super TSource> predicate) {
		return select(predicate).last();
	}

	@Override
	public <TResult> Traversable<TResult> map(final Func<? super TSource, TResult> resultSelector) {
		return new TraversableImpl<TResult>(
			new Iterable<TResult>() {
				@Override
				public Iterator<TResult> iterator() {
					return new MapIterator<TSource, TResult>(source.iterator(), resultSelector);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> reverse() {
		final Stack<TSource> stack = new Stack<TSource>();

		for (TSource item : source) {
			stack.push(item);
		}

		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					final ListIterator<TSource> listIterator = stack.listIterator(stack.size());

					return new Iterator<TSource>() {
						@Override
						public boolean hasNext() {
							return listIterator.hasPrevious();
						}

						@Override
						public TSource next() {
							return listIterator.previous();
						}

						@Override
						public void remove() {
							throw new UnsupportedOperationException();
						}
					};
				}
			}
		);
	}

	@Override
	public Traversable<TSource> select(final Predicate<? super TSource> predicate) {
		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new SelectIterator<TSource>(source.iterator(), predicate);
				}
			}
		);
	}

	@Override
	public Traversable<TSource> skip(final long count) {
		return indexedSelect(
			new Predicate<Indexed<TSource>>() {
				@Override
				public boolean invoke(Indexed<TSource> arg) {
					return arg.getIndex() >= count;
				}
			}
		);
	}

	@Override
	public Traversable<TSource> take(final long count) {
		return indexedSelect(
			new Predicate<Indexed<TSource>>() {
				@Override
				public boolean invoke(Indexed<TSource> arg) {
					return arg.getIndex() < count;
				}
			}
		);
	}

	private Traversable<TSource> indexedSelect(final Predicate<Indexed<TSource>> predicate) {
		return new TraversableImpl<TSource>(
			new Iterable<TSource>() {
				@Override
				public Iterator<TSource> iterator() {
					return new IndexedSelectIterator<TSource>(source.iterator(), predicate);
				}
			}
		);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> sortBy(Func<TSource, TKey> keySelector) {
		return sortBy(keySelector, null);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> sortBy(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(this, keySelector, comparator, false);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> sortByDescending(Func<TSource, TKey> keySelector) {
		return sortByDescending(keySelector, null);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> sortByDescending(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(this, keySelector, comparator, true);
	}

	@Override
	public Iterator<TSource> iterator() {
		return this.source.iterator();
	}
}
