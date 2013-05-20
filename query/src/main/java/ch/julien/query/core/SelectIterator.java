package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Predicate;

import java.util.Iterator;

class SelectIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Predicate<? super TSource> predicate;

	public SelectIterator(Iterator<? extends TSource> parent, Predicate<? super TSource> predicate) {
		super(parent);

		Check.notNull(predicate, "predicate");

		this.predicate = predicate;
	}

	@Override
	protected TSource computeNext() {
		while (this.parent.hasNext()) {
			TSource element = this.parent.next();

			if (this.predicate.invoke(element)) {
				return element;
			}
		}

		return computationEnd();
	}
}
