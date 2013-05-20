package ch.julien.common.delegate;

public interface Func<T, TResult> {
	TResult invoke(T arg);
}
