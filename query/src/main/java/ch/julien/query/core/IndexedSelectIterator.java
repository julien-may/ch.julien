package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Predicate;
import ch.julien.common.monad.Indexed;

import java.util.Iterator;

class IndexedSelectIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Predicate<Indexed<TSource>> predicate;
	private int index = 0;

	public IndexedSelectIterator(Iterator<? extends TSource> parent, Predicate<Indexed<TSource>> predicate) {
		super(parent);

		Check.notNull(predicate, "predicate");

		this.predicate = predicate;
	}

	@Override
	protected TSource computeNext() {
		while (this.parent.hasNext()) {
			TSource element = this.parent.next();

			if (this.predicate.invoke(new Indexed<TSource>(this.index++, element))) {
				return element;
			}
		}

		return computationEnd();
	}
}
