/**
 * 
 */
package M1International.MultimapAdvanced;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author waseem
 * @param <K>
 * @param <V>
 *
 */
public abstract class AbstractMyMultimap<K, V> implements
		MyMultimapInterface<K, V> {

	private transient Map<K, Collection<V>> original;
	private transient AbstractMyMultimap<V, K> inverse;
	private transient int totalSize;
	private static final int initialCollectionSize = 5;

	public AbstractMyMultimap(Map<K, Collection<V>> a, Map<V, Collection<K>> b) {
		original = a;
		inverse = new Reverse<V, K>(b, this);

	}

	private AbstractMyMultimap(Map<K, Collection<V>> c,
			AbstractMyMultimap<V, K> d) {
		original = c;
		inverse = d;
	}

	public boolean add(K key, V value) {

		if (updateMaps(key, value)) {
			totalSize++;
			return true;
		}
		return false;
	}

	private boolean updateMaps(K key, V value) {
		// CurrentMap Update
		if (updateCurrentMap(key, value))
			if (updateInverseMap(value, key))// update InverseMap
				return true;
		return false;
	}

	private boolean updateInverseMap(V value, K key) {
		boolean isUpdate = false;
		Collection<K> collection = (Collection<K>) inverse.original.get(value);
		if (collection == null)
			collection = new ArrayList<K>(initialCollectionSize);
		if (collection.add(key)) {
			inverse.original.put(value, collection);
			isUpdate = true;
		}
		return isUpdate;
	}

	private boolean updateCurrentMap(K key, V value) {
		boolean isUpdate = false;
		Collection<V> collection = (Collection<V>) original.get(key);
		if (collection == null)
			collection = new ArrayList<V>(initialCollectionSize);
		if (collection.add(value)) {
			original.put(key, collection);
			isUpdate = true;
		}
		return isUpdate;
	}

	public boolean containsKey(K key) {
		return original.containsKey(key);
	}

	private static class Reverse<K, V> extends AbstractMyMultimap<K, V> {

		private Reverse(Map<K, Collection<V>> c, AbstractMyMultimap<V, K> d) {
			super(c, d);
		}
	}

	public int size() {
		return totalSize;
	}

	public boolean isEmpty() {
		return totalSize == 0;
	}

	public Collection<V> get(K key) {
		Collection<V> collection = original.get(key);
		return collection;
	}

	public AbstractMyMultimap<V, K> inverse() {
		return inverse;
	}

}
