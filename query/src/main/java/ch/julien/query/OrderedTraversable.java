package ch.julien.query;

import ch.julien.common.delegate.Func;

import java.util.Comparator;

public interface OrderedTraversable<T, TKey> extends Traversable<T> {
	OrderedTraversable<T, TKey> thenBy(Func<T, TKey> keySelector);
	OrderedTraversable<T, TKey> thenBy(Func<T, TKey> keySelector, Comparator<TKey> comparator);

	OrderedTraversable<T, TKey> thenByDescending(Func<T, TKey> keySelector);
	OrderedTraversable<T, TKey> thenByDescending(Func<T, TKey> keySelector, Comparator<TKey> comparator);
}
