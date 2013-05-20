package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Func;

import java.util.Iterator;

class FlatIterator<TSource, TResult> extends AbstractIterator<TSource, TResult> {
	private final Func<TSource, Iterable<TResult>> selector;

	private Iterator<TResult> child;

	public FlatIterator(Iterator<? extends TSource> parent, Func<TSource, Iterable<TResult>> selector) {
		super(parent);

		Check.notNull(selector, "selector");

		this.selector = selector;
	}

	@Override
	protected TResult computeNext() {
		if (this.child != null) {
			if (this.child.hasNext()) {
				return this.child.next();
			}
		}

		while (this.parent.hasNext()) {
			Iterable<TResult> childIterable = this.selector.invoke(
				this.parent.next()
			);

			this.child = childIterable.iterator();

			if (this.child.hasNext()) {
				return this.child.next();
			}
		}

		return computationEnd();
	}
}
