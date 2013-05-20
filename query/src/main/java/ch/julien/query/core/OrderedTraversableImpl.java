package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Func;
import ch.julien.common.monad.Indexed;
import ch.julien.query.OrderedTraversable;

import java.util.*;

class OrderedTraversableImpl<TSource, TKey> extends TraversableImpl<TSource> implements OrderedTraversable<TSource> {
	private final List<Comparator<TSource>> comparators;

	public OrderedTraversableImpl(Iterable<TSource> source, final Func<TSource, TKey> keySelector, Comparator<TKey> comparator,
		boolean descending) {

		this(source, null, keySelector, comparator, descending);
	}

	private OrderedTraversableImpl(Iterable<TSource> source, List<Comparator<TSource>> comparators,
		final Func<TSource, TKey> keySelector, final Comparator<TKey> comparator, final boolean descending) {

		super(source);

		Check.notNull(keySelector, "keySelector");

		final Comparator<TKey> comparer = comparator == null ? new DefaultComparator<TKey>() : comparator;

		if (comparators == null) {
			comparators = new ArrayList<Comparator<TSource>>();
		}

		comparators.add(
			new Comparator<TSource>() {
				@Override
				public int compare(TSource a, TSource b) {
					return (descending ? - 1 : 1) * comparer.compare(
						keySelector.invoke(a), keySelector.invoke(b)
					);
				}
			}
		);

		this.comparators = comparators;
	}

	@Override
	public <TKey> OrderedTraversable<TSource> thenBy(Func<TSource, TKey> keySelector) {
		return thenBy(keySelector, null);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> thenBy(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(this, this.comparators, keySelector, comparator, false);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> thenByDescending(Func<TSource, TKey> keySelector) {
		return thenByDescending(keySelector, null);
	}

	@Override
	public <TKey> OrderedTraversable<TSource> thenByDescending(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(this, this.comparators, keySelector, comparator, true);
	}

	@Override
	public Iterator<TSource> iterator() {
		List<Indexed<TSource>> items = new ArrayList<Indexed<TSource>>();

		int index = 0;
		for (TSource item : this.source) {
			items.add(new Indexed<TSource>(index++, item));
		}

		Collections.sort(items,
			new Comparator<Indexed<TSource>>() {
				@Override
				public int compare(Indexed<TSource> a, Indexed<TSource> b) {
					for (Comparator<TSource> comparator : comparators) {
						int result = comparator.compare(a.getValue(), b.getValue());

						if (result != 0) {
							return result;
						}
					}

					return Integer.compare(a.getIndex(), b.getIndex());
				}
			}
		);

		return new TraversableImpl<Indexed<TSource>>(items).map(
			new Func<Indexed<TSource>, TSource>() {
				@Override
				public TSource invoke(Indexed<TSource> item) {
					return item.getValue();
				}
			}
		).iterator();
	}
}
