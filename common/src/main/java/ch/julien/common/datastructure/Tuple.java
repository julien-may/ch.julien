package ch.julien.common.datastructure;

public class Tuple<TFirst, TSecond> {
	private final TFirst first;
	private final TSecond second;

	public Tuple(TFirst first, TSecond second) {
		this.first = first;
		this.second = second;
	}

	public TFirst getFirst() {
		return this.first;
	}

	public TSecond getSecond() {
		return this.second;
	}
}
