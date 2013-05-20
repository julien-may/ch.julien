package ch.julien.common.delegate;

public interface Action<T> {
	void invoke(T arg);
}
