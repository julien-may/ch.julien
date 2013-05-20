package ch.julien.query;

public interface Group<K, V> extends Traversable<V> {
	K getKey();
}
