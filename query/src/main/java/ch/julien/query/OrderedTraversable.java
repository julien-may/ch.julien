package ch.julien.query;

import ch.julien.common.delegate.Func;

import java.util.Comparator;

public interface OrderedTraversable<T> extends Traversable<T> {
	<TKey> OrderedTraversable<T> thenBy(Func<T, TKey> keySelector);
	<TKey> OrderedTraversable<T> thenBy(Func<T, TKey> keySelector, Comparator<TKey> comparator);

	<TKey> OrderedTraversable<T> thenByDescending(Func<T, TKey> keySelector);
	<TKey> OrderedTraversable<T> thenByDescending(Func<T, TKey> keySelector, Comparator<TKey> comparator);
}
