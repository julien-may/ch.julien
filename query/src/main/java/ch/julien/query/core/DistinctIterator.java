package ch.julien.query.core;

import ch.julien.common.delegate.EqualityComparator;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class DistinctIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Set<Key<TSource>> set;

	public DistinctIterator(Iterator<? extends TSource> parent, EqualityComparator<TSource> equalityComparator) {
		super(parent);

		this.set = new TreeSet<Key<TSource>>(
			new KeyComparator<TSource>(equalityComparator)
		);
	}

	@Override
	protected TSource computeNext() {
		while (parent.hasNext()) {
			TSource item = parent.next();
			Key<TSource> key = new Key<TSource>(item);

			if (this.set.contains(key)) {
				continue;
			}

			this.set.add(key);
			return item;
		}

		return computationEnd();
	}
}
