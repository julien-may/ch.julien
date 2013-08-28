package ch.julien.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapBuilder {
	public static class KeyBuilder<TKey, TValue> {
		private final Map<TKey, TValue> context;

		public KeyBuilder(Map<TKey, TValue> context) {
			this.context = context;
		}

		public ValueBuilder<TKey, TValue> key(TKey key) {
			return new ValueBuilder<TKey, TValue>(this.context, key);
		}

		public Map<TKey, TValue> build() {
			return this.context;
		}
	}

	public static class ValueBuilder<TKey, TValue> {
		private final Map<TKey, TValue> context;
		private final TKey key;

		public ValueBuilder(Map<TKey, TValue> context, TKey key) {
			this.context = context;
			this.key = key;
		}

		public KeyBuilder<TKey, TValue> value(TValue value) {
			this.context.put(this.key, value);

			return new KeyBuilder<TKey, TValue>(this.context);
		}
	}

	public static <TKey, TValue> KeyBuilder<TKey, TValue> hashMap() {
		return new KeyBuilder<TKey, TValue>(new HashMap<TKey, TValue>());
	}

	public static <TKey, TValue> KeyBuilder<TKey, TValue> hashMap(Class<TKey> keyType, Class<TValue> valueType) {
		return MapBuilder.<TKey, TValue>hashMap();
	}

	public static <TKey, TValue> KeyBuilder<TKey, TValue> treeMap() {
		return new KeyBuilder<TKey, TValue>(new TreeMap<TKey, TValue>());
	}

	public static <TKey, TValue> KeyBuilder<TKey, TValue> treeMap(Class<TKey> keyType, Class<TValue> valueType) {
		return MapBuilder.<TKey, TValue>treeMap();
	}
}
