package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Func;

import java.util.Iterator;

class MapIterator<TSource, TResult> extends AbstractIterator<TSource, TResult> {
	private final Func<? super TSource, TResult> resultSelector;

	public MapIterator(Iterator<? extends TSource> parent, Func<? super TSource, TResult> resultSelector) {
		super(parent);

		Check.notNull(resultSelector, "resultSelector");

		this.resultSelector = resultSelector;
	}

	@Override
	protected TResult computeNext() {
		if (this.parent.hasNext()) {
			return this.resultSelector.invoke(
				this.parent.next()
			);
		}

		return computationEnd();
	}
}
