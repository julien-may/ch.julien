package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.datastructure.Tuple;

import java.util.Iterator;

class ZipAllIterator<TSource, TSourceOther> extends AbstractIterator<TSource, Tuple<TSource, TSourceOther>> {
	private final Iterator<TSourceOther> otherIterator;

	public ZipAllIterator(Iterator<? extends TSource> parent, Iterator<TSourceOther> otherIterator) {
		super(parent);

		Check.notNull(otherIterator, "otherIterator");

		this.otherIterator = otherIterator;
	}

	@Override
	protected Tuple<TSource, TSourceOther> computeNext() {
		if (super.parent.hasNext() || this.otherIterator.hasNext()) {
			TSource source = null;
			TSourceOther sourceOther = null;

			if (super.parent.hasNext()) {
				source = super.parent.next();
			}

			if (this.otherIterator.hasNext()) {
				sourceOther = this.otherIterator.next();
			}

			return new Tuple<TSource, TSourceOther>(source, sourceOther);
		}

		return computationEnd();
	}
}
