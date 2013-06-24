package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.datastructure.Tuple;

import java.util.Iterator;

class ZipIterator<TSource, TSourceOther> extends AbstractIterator<TSource, Tuple<TSource, TSourceOther>> {
	private final Iterator<TSourceOther> otherIterator;

	public ZipIterator(Iterator<? extends TSource> parent, Iterator<TSourceOther> otherIterator) {
		super(parent);

		Check.notNull(otherIterator, "otherIterator");

		this.otherIterator = otherIterator;
	}

	@Override
	protected Tuple<TSource, TSourceOther> computeNext() {
		if (super.parent.hasNext() && this.otherIterator.hasNext()) {
			return new Tuple<TSource, TSourceOther>(super.parent.next(), this.otherIterator.next());
		}

		return computationEnd();
	}
}
