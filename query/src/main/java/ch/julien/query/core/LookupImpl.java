package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.query.Group;
import ch.julien.query.Lookup;
import ch.julien.query.Traversable;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

class LookupImpl<TKey, TElement> extends TraversableImpl<Group<TKey, TElement>> implements Lookup<TKey, TElement> {
	private final Map<Key<TKey>, Group<TKey, TElement>> map;

	public LookupImpl(Iterable<Group<TKey, TElement>> iterable, Map<Key<TKey>, Group<TKey, TElement>> map) {
		super(iterable);

		Check.notNull(map, "map");

		this.map = map;
	}

	@Override
	public Traversable<TElement> getItems(TKey key) {
		if (this.map.containsKey(new Key<TKey>(key))) {
			return new TraversableImpl<TElement>(
				this.map.get(new Key<TKey>(key))
			);
		}

		return new TraversableImpl<TElement>(
			new Iterable<TElement>() {
				@Override
				public Iterator<TElement> iterator() {
					return new Iterator<TElement>() {
						@Override
						public boolean hasNext() {
							return false;
						}

						@Override
						public TElement next() {
							throw new NoSuchElementException();
						}

						@Override
						public void remove() {
							throw new IllegalStateException();
						}
					};
				}
			}
		);
	}
}
