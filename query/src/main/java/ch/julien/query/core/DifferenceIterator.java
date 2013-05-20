package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.EqualityComparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DifferenceIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Iterator<? extends TSource> looseIterator;
	private final EqualityComparator<TSource> comparator;
	private final List<TSource> processed = new ArrayList<TSource>();

	public DifferenceIterator(Iterator<? extends TSource> parent, Iterator<? extends TSource> looseIterator, EqualityComparator<TSource> comparator) {
		super(parent);

		Check.notNull(looseIterator, "looseIterator");
		Check.notNull(comparator, "comparator");

		this.looseIterator = looseIterator;
		this.comparator = comparator;
	}

	@Override
	protected TSource computeNext() {
		while (this.parent.hasNext()) {
			boolean elementEquals = false;
			TSource parentItem = this.parent.next();

			for (TSource looseItem : this.processed) {
				if (this.comparator.equals(looseItem, parentItem)) {
					elementEquals = true;
					break;
				}
			}

			if (!elementEquals) {
				while (this.looseIterator.hasNext()) {
					TSource looseItem = this.looseIterator.next();

					this.processed.add(looseItem);

					if (this.comparator.equals(looseItem, parentItem)) {
						elementEquals = true;
					}
				}
			}

			if (!elementEquals) {
				return parentItem;
			}
		}

		return computationEnd();
	}
}
