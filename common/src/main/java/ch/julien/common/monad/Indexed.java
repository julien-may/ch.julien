package ch.julien.common.monad;

public class Indexed<T> {
	private final int index;
	private final T value;


	public Indexed(int index, T value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return this.index;
	}

	public T getValue() {
		return this.value;
	}
}
