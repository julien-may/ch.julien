package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.common.delegate.Action;

import java.util.Iterator;

class EachIterator<TSource> extends AbstractIterator<TSource, TSource> {
	private final Action<TSource> action;

	public EachIterator(Iterator<? extends TSource> parent, Action<TSource> action) {
		super(parent);

		Check.notNull(action, "action");

		this.action = action;
	}

	@Override
	protected TSource computeNext() {
		if (this.parent.hasNext()) {
			TSource item = this.parent.next();

			this.action.invoke(item);

			return item;
		}

		return computationEnd();
	}
}
