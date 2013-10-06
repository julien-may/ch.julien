package ch.julien.common.monad;

import ch.julien.common.contract.Check;

public abstract class Option<T> {
	public static <T> Option<T> none() {
		return new None<T>();
	}

	public static <T> Option<T> some(T value) {
		return new Some<T>(value);
	}

	private Option() {}

	public abstract Boolean hasValue();

	public abstract T get();

	/**
	 * A value access method to obtain the value contained in this {@code Option}
	 * or an alternative. If a value is present, it is returned. If no value is
	 * present, the supplied alternative value is returned.
	 */
	public abstract T getOrElse(T value);

	public abstract Option<T> or(Option<? extends T> other);

	public abstract Option<T> orOption(T other);

	private static class None<T> extends Option<T> {
		@Override
		public Boolean hasValue() {
			return false;
		}

		@Override
		public T get() {
			throw new UnsupportedOperationException("Cannot resolve value on None.");
		}

		@Override
		public T getOrElse(T value) {
			return value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Option<T> or(Option<? extends T> other) {
			return (Option<T>)other;
		}

		@Override
		public Option<T> orOption(T other) {
			return some(other);
		}
	}

	private static class Some<T> extends Option<T> {
		private final T value;

		public Some(T value) {
			this.value = value;
		}

		@Override
		public Boolean hasValue() {
			return true;
		}

		@Override
		public T get() {
			return this.value;
		}

		@Override
		public T getOrElse(T value) {
			return get();
		}

		@Override
		public Option<T> or(Option<? extends T> other) {
			return this;
		}

		@Override
		public Option<T> orOption(T other) {
			return this;
		}
	}
}
