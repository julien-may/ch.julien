package ch.julien.query;

public interface Lookup<K, V> extends Traversable<Group<K, V>> {
	Traversable<V> getItems(K key);
}
