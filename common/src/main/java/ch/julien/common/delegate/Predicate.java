package ch.julien.common.delegate;

public interface Predicate<T> {
	boolean invoke(T arg);
}
