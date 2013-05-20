package ch.julien.query.core;

import java.util.Iterator;

class ConcatIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Iterator<? extends TSource> appendant;

	public ConcatIterator(Iterator<? extends TSource> parent, Iterator<? extends TSource> appendant) {
		super(parent);

		this.appendant = appendant;
	}

	@Override
	protected TSource computeNext() {
		if (this.parent.hasNext()) {
			return this.parent.next();
		}

		if (this.appendant.hasNext()) {
			return this.appendant.next();
		}

		return computationEnd();
	}
}
