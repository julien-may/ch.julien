package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Func;
import ch.julien.common.monad.Indexed;
import ch.julien.query.OrderedTraversable;

import java.util.*;

class OrderedTraversableImpl<TSource, TKey> extends TraversableImpl<TSource> implements OrderedTraversable<TSource, TKey> {
	private final List<Comparator<TSource>> comparators;

	public static <TSource, TKey> OrderedTraversableImpl<TSource, TKey> create(Iterable<TSource> source, Func<TSource, TKey> keySelector, Comparator<TKey> comparator,
		boolean descending) {

		return create(source, null, keySelector, comparator, descending);
	}

	private static <TSource, TKey> OrderedTraversableImpl<TSource, TKey> create(Iterable<TSource> source, List<Comparator<TSource>> comparators,
		final Func<TSource, TKey> keySelector, final Comparator<TKey> comparator, final boolean descending) {

		return new OrderedTraversableImpl<TSource, TKey>(
			new OrderedIterable<TSource, TKey>(source, comparators, keySelector, comparator, descending)
		);
	}

	private OrderedTraversableImpl(OrderedIterable<TSource, TKey> source) {
		super(source);

		this.comparators = source.comparators;
	}

	@Override
	public OrderedTraversable<TSource, TKey> thenBy(Func<TSource, TKey> keySelector) {
		return thenBy(keySelector, null);
	}

	@Override
	public OrderedTraversable<TSource, TKey> thenBy(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(
			new OrderedIterable<TSource, TKey>(this, comparators, keySelector, comparator, false)
		);
	}

	@Override
	public OrderedTraversable<TSource, TKey> thenByDescending(Func<TSource, TKey> keySelector) {
		return thenByDescending(keySelector, null);
	}

	@Override
	public OrderedTraversable<TSource, TKey> thenByDescending(Func<TSource, TKey> keySelector, Comparator<TKey> comparator) {
		return new OrderedTraversableImpl<TSource, TKey>(
			new OrderedIterable<TSource, TKey>(this, comparators, keySelector, comparator, true)
		);
	}


	private static class OrderedIterable<TSource, TKey> implements Iterable<TSource> {
		private final Iterable<TSource> source;
		private final List<Comparator<TSource>> comparators;

		public OrderedIterable(Iterable<TSource> source, List<Comparator<TSource>> comparators,
			final Func<TSource, TKey> keySelector, final Comparator<TKey> comparator, final boolean descending) {

			Check.notNull(source, "source");
			Check.notNull(keySelector, "keySelector");

			this.source = source;

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

						return Integer.valueOf(a.getIndex()).compareTo(b.getIndex());
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
}
