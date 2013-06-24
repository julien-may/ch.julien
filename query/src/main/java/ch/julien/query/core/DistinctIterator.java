package ch.julien.query.core;

import ch.julien.common.delegate.EqualityComparator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class DistinctIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final EqualityComparator<TSource> equalityComparator;
	private final Set<Key<TSource>> set = new HashSet<Key<TSource>>();

	public DistinctIterator(Iterator<? extends TSource> parent, EqualityComparator<TSource> equalityComparator) {
		super(parent);

		this.equalityComparator = equalityComparator;
	}

	@Override
	protected TSource computeNext() {
		while (parent.hasNext()) {
			Key<TSource> key = new Key<TSource>(parent.next(), this.equalityComparator);

			if (this.set.contains(key)) {
				continue;
			}

			this.set.add(key);
			return key.value;
		}

		return computationEnd();
	}
}
