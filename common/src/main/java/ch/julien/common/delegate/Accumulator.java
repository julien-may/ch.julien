package ch.julien.common.delegate;

public interface Accumulator<TAccumulate, TSource> {
	TAccumulate accumulate(TAccumulate accumulate, TSource source);
}
