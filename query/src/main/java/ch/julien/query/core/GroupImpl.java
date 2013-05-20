package ch.julien.query.core;

import ch.julien.query.Group;

import java.util.ArrayList;
import java.util.List;

class GroupImpl<TKey, TElement> extends TraversableImpl<TElement> implements Group<TKey, TElement> {
	private final List<TElement> values;
	private final TKey key;

	public GroupImpl(TKey key) {
		super(new ArrayList<TElement>());

		this.values = (ArrayList<TElement>)super.source;
		this.key = key;
	}

	@Override
	public TKey getKey() {
		return this.key;
	}

	public void add(TElement item) {
		this.values.add(item);
	}
}
