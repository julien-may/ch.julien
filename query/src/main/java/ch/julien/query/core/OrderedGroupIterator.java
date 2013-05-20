package ch.julien.query.core;

import ch.julien.common.contract.Check;
import ch.julien.query.Group;

import java.util.Iterator;
import java.util.Map;

class OrderedGroupIterator<K, V> extends AbstractIterator<Key<K>, Group<K, V>> {
	private final Map<Key<K>, Group<K, V>> map;

	public OrderedGroupIterator(Iterator<Key<K>> parent, Map<Key<K>, Group<K, V>> map) {
		super(parent);

		Check.notNull(map, "map");

		this.map = map;
	}

	@Override
	protected Group<K, V> computeNext() {
		if (this.parent.hasNext()) {
			return this.map.get(
				this.parent.next()
			);
		}

		return computationEnd();
	}
}
