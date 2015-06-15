/**
 * 
 */
package M1International.MultimapAdvanced;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.AbstractCollection;
import java.util.Map.Entry;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

/**
 * @author waseem
 * @param <K>
 * @param <V>
 *
 */
public abstract class AbstractMyBiMultimap<K, V> implements
		MyBiMultimap<K, V> {

	private transient Map<K, Collection<V>> original;
	private transient AbstractMyBiMultimap<V, K> inverse;
	private transient int totalSize;
	private static final int initialCollectionSize = 5;

	public AbstractMyBiMultimap(Map<K, Collection<V>> a, Map<V, Collection<K>> b) {
		original = a;
		inverse = new Reverse<V, K>(b, this);
	}

	private AbstractMyBiMultimap(Map<K, Collection<V>> c,
			AbstractMyBiMultimap<V, K> d) {
		original = c;
		inverse = d;
	}

	public boolean put(K key, V value) {

		if (updateMaps(key, value)) {
			totalSize++;
			this.inverse.totalSize++;
			return true;
		}
		return false;
	}

	private boolean updateMaps(K key, V value) {

		if (updateCurrentMap(key, value))// update CurrentMap
			if (updateInverseMap(value, key))// update InverseMap
				return true;
		return false;
	}

	private boolean updateInverseMap(V value, K key) {
		boolean isUpdate = false;
		Collection<K> collection = inverse.original.get(value);
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

	public boolean containsKey(Object key) {
		return original.containsKey(key);
	}

	private static class Reverse<K, V> extends AbstractMyBiMultimap<K, V> {

		private Reverse(Map<K, Collection<V>> c, AbstractMyBiMultimap<V, K> d) {
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

	public boolean containsValue(Object value) {
		AbstractMyBiMultimap<V, K> local = this.inverse();
		return local.containsKey(value);
	}

	public boolean containsEntry(Object key, Object value) {
		Collection<V> collection = original.get(key);
		return collection != null && collection.contains(value);
	}

	public boolean remove(Object key, Object value) {
		Collection<V> collection = original.get(key);
		if (collection == null) {
			return false;
		}
		boolean changed = collection.remove(value);
		if (changed) {
			if (removeFromInverse(key, value)) {
				totalSize--;
				if (collection.isEmpty()) {
					original.remove(key);
				}
			}
		}
		return changed;
	}

	private boolean removeFromInverse(Object key, Object value) {
		Collection<K> collection = inverse.original.get(value);
		if (collection == null) {
			return false;
		}
		boolean changed = collection.remove(key);
		if (changed) {
			if (collection.isEmpty()) {
				inverse.original.remove(value);
			}
		}
		return changed;
	}

	public Collection<V> removeAll(Object key) {
		// TODO Auto-generated method stub
		Collection<V> collection = original.remove(key);
		Collection<V> output = new ArrayList<V>(initialCollectionSize);

		if (collection != null) {
			Iterator<V> iterator = collection.iterator();
			while (iterator.hasNext()) {
				removeFromInverse(key, iterator.next());
			}
			output.addAll(collection);
			totalSize -= collection.size();
			collection.clear();
		}

		return unmodifiableCollectionSubclass(output);
	}

	private Collection<V> unmodifiableCollectionSubclass(
			Collection<V> collection) {
		if (collection instanceof SortedSet) {
			return Collections.unmodifiableSortedSet((SortedSet<V>) collection);
		} else if (collection instanceof Set) {
			return Collections.unmodifiableSet((Set<V>) collection);
		} else if (collection instanceof List) {
			return Collections.unmodifiableList((List<V>) collection);
		} else {
			return Collections.unmodifiableCollection(collection);
		}
	}

	public void clear() {
		for (Collection<V> collection : original.values()) {
			collection.clear();
		}
		original.clear();
		for (Collection<K> collection : inverse.original.values()) {
			collection.clear();
		}
		inverse.original.clear();
		totalSize = 0;
		this.inverse.totalSize=0;
	}

	public boolean putAll(K key, Iterable<? extends V> values) {
		if (!values.iterator().hasNext()) {
			return false;
		}
		boolean changed = false;
		for (V value : values) {
			changed |= updateMaps(key, value);
			totalSize++;
			this.inverse.totalSize++;
		}
		return changed;
	}
	private transient Set<K> keySet;

	public Set<K> keySet() {
		Set<K> result = keySet;
		return (result == null) ? keySet = new KeySet(original) : result;
	}

	private class KeySet extends AbstractSet<K> {

		final Map<K, Collection<V>> subMap;

		KeySet(final Map<K, Collection<V>> subMap) {
			this.subMap = subMap;
		}

		@Override
		public int size() {
			return subMap.size();
		}

		@Override
		public Iterator<K> iterator() {
			return new Iterator<K>() {
				final Iterator<Map.Entry<K, Collection<V>>> entryIterator = subMap
						.entrySet().iterator();
				Map.Entry<K, Collection<V>> entry;

				public boolean hasNext() {
					return entryIterator.hasNext();
				}

				public K next() {
					entry = entryIterator.next();
					return entry.getKey();
				}

				public void remove() {
					// checkState(entry != null);
					Collection<V> collection = entry.getValue();
					entryIterator.remove();
					if (collection != null) {
						Iterator<V> iterator = collection.iterator();
						while (iterator.hasNext()) {
							removeFromInverse(entry.getKey(), iterator.next());
						}
						totalSize -= collection.size();
						AbstractMyBiMultimap.this.inverse.totalSize-= collection.size();
						collection.clear();
					}
				}
			};
		}

		@Override
		public boolean contains(Object key) {
			return subMap.containsKey(key);
		}

		@Override
		public boolean remove(Object key) {
			int count = 0;
			Collection<V> collection = subMap.remove(key);
			if (collection != null) {
				Iterator<V> iterator = collection.iterator();
				while (iterator.hasNext()) {
					removeFromInverse(key, iterator.next());
				}
				count = collection.size();
				collection.clear();
			}
			totalSize -= count;
			AbstractMyBiMultimap.this.inverse.totalSize-= count;
			return count > 0;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return subMap.keySet().containsAll(c);
		}
	}

	private transient Multiset<K> keys;

	public Multiset<K> keys() {
		Multiset<K> multiset = keys;
		return (multiset == null) ? keys = new MyMultiset(original) : multiset;
	}

	private class MyMultiset implements Multiset<K> {

		final Map<K, Collection<V>> subMap;

		MyMultiset(final Map<K, Collection<V>> subMap) {
			this.subMap = subMap;
		}

		public boolean addAll(Collection<? extends K> c) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public void clear() {
			// TODO Auto-generated method stub
			AbstractMyBiMultimap.this.clear();
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return totalSize == 0;
		}

		public int size() {
			// TODO Auto-generated method stub
			return totalSize;
		}

		public Object[] toArray() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public int count(Object element) {
			// TODO Auto-generated method stub
			Collection<V> collection;
			try {
				collection = subMap.get(element);
				return collection == null ? 0 : collection.size();
			} catch (NullPointerException e) {
				return 0;
			} catch (ClassCastException e) {
				return 0;
			}
		}

		public int add(K element, int occurrences) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public int remove(Object element, int occurrences) {
			// TODO Auto-generated method stub
			checkArgument(occurrences > 0);
			Collection<V> collection;
			try {
				collection = subMap.get(element);
			} catch (NullPointerException e) {
				return 0;
			} catch (ClassCastException e) {
				return 0;
			}

			if (collection == null) {
				return 0;
			}
			int count = collection.size();

			if (occurrences >= count && collection != null) {
				Iterator<V> iterator = collection.iterator();
				while (iterator.hasNext()) {
					removeFromInverse(element, iterator.next());
				}
				count = collection.size();
				collection.clear();
				subMap.remove(element);
				totalSize -= count;
				AbstractMyBiMultimap.this.inverse.totalSize-=count;
				return count;
			}

			Iterator<V> iterator = collection.iterator();
			V value;
			for (int i = 0; i < occurrences; i++) {
				value = iterator.next();
				iterator.remove();
				removeFromInverse(element, value);
			}
			totalSize -= occurrences;
			AbstractMyBiMultimap.this.inverse.totalSize-= occurrences;
			return count;
		}

		public int setCount(K element, int count) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public boolean setCount(K element, int oldCount, int newCount) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public Set<K> elementSet() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public Set<com.google.common.collect.Multiset.Entry<K>> entrySet() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public Iterator<K> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean contains(Object element) {
			// TODO Auto-generated method stub
			return subMap.containsKey(element);
		}

		public boolean containsAll(Collection<?> elements) {
			// TODO Auto-generated method stub
			return subMap.keySet().containsAll(elements);
		}

		public boolean add(K element) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object element) {
			// TODO Auto-generated method stub
			int count = 0;
			Collection<V> collection = subMap.remove(element);
			if (collection != null) {
				Iterator<V> iterator = collection.iterator();
				while (iterator.hasNext()) {
					removeFromInverse(element, iterator.next());
				}
				count = collection.size();
				collection.clear();
			}
			totalSize -= count;
			AbstractMyBiMultimap.this.inverse.totalSize-= count;
			return count > 0;
		}

		public boolean removeAll(Collection<?> c) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> c) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

	}

	private transient Collection<V> values;

	public Collection<V> values() {
		// TODO Auto-generated method stub
		Collection<V> result = values;
		return (result == null) ? values = createValues() : result;
	}

	Collection<V> createValues() {
		return new Values();
	}

	class Values extends AbstractCollection<V> {
		@Override
		public Iterator<V> iterator() {
			return new ValueIterator();
		}

		@Override
		public int size() {
			return AbstractMyBiMultimap.this.size();
		}

		@Override
		public boolean contains(Object o) {
			return AbstractMyBiMultimap.this.containsValue(o);
		}

		@Override
		public void clear() {
			AbstractMyBiMultimap.this.clear();
		}
	}

	private class ValueIterator implements Iterator<V> {
		final Iterator<Map.Entry<K, V>> entryIterator = createEntryIterator();

		public boolean hasNext() {
			return entryIterator.hasNext();
		}

		public V next() {
			return entryIterator.next().getValue();
		}

		public void remove() {
			entryIterator.remove();
		}

		Iterator<Map.Entry<K, V>> createEntryIterator() {
			return new EntryIterator();
		}
	}

	private class EntryIterator implements Iterator<Map.Entry<K, V>> {
		final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
		K key;
		V value;
		Collection<V> collection;
		Iterator<V> valueIterator;

		EntryIterator() {
			keyIterator = original.entrySet().iterator();
			if (keyIterator.hasNext()) {
				findValueIteratorAndKey();
			}
		}

		void findValueIteratorAndKey() {
			Map.Entry<K, Collection<V>> entry = keyIterator.next();
			key = entry.getKey();
			collection = entry.getValue();
			valueIterator = collection.iterator();
		}

		public boolean hasNext() {
			return keyIterator.hasNext() || valueIterator.hasNext();
		}

		public Map.Entry<K, V> next() {
			if (!valueIterator.hasNext()) {
				findValueIteratorAndKey();
			}
			return Maps.immutableEntry(key, value = valueIterator.next());
		}

		public void remove() {
			valueIterator.remove();
			removeFromInverse(key, value);
			if (collection.isEmpty()) {
				keyIterator.remove();
			}
			totalSize--;
			AbstractMyBiMultimap.this.inverse.totalSize--;
		}
	}

	private transient Collection<Map.Entry<K, V>> entries;

	public Collection<Entry<K, V>> entries() {
		Collection<Map.Entry<K, V>> result = entries;
		return result == null ? entries = createEntries() : entries;
	}

	private Collection<Map.Entry<K, V>> createEntries() {

		return new Entries();
	}

	private class Entries extends AbstractCollection<Map.Entry<K, V>> {
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return createEntryIterator();
		}

		@Override
		public int size() {
			return totalSize;
		}

		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
			return containsEntry(entry.getKey(), entry.getValue());
		}

		@Override
		public void clear() {
			AbstractMyBiMultimap.this.clear();
		}

		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
			return AbstractMyBiMultimap.this.remove(entry.getKey(),
					entry.getValue());
		}

		Iterator<Map.Entry<K, V>> createEntryIterator() {
			return new EntryIterator();
		}
	}

	private transient Map<K, Collection<V>> asMap;

	public Map<K, Collection<V>> asMap() {
		Map<K, Collection<V>> result = asMap;
		return (result == null) ? asMap = createAsMap() : result;
	}

	private Map<K, Collection<V>> createAsMap() {
		return  new AsMap(original);
	}

	private class AsMap extends AbstractMap<K, Collection<V>> {

		final transient Map<K, Collection<V>> submap;

		AsMap(Map<K, Collection<V>> submap) {
			this.submap = submap;
		}

		transient Set<Map.Entry<K, Collection<V>>> entrySet;

		@Override
		public Set<Map.Entry<K, Collection<V>>> entrySet() {
			Set<Map.Entry<K, Collection<V>>> result = entrySet;
			return (result == null) ? entrySet = new AsMapEntries() : result;
		}

		@Override
		public boolean containsKey(Object key) {
			return submap.containsKey(key);
		}

		@Override
		public Collection<V> get(Object key) {
			Collection<V> collection = submap.get(key);
			if (collection == null) {
				return null;
			}
			return collection;
		}

		@Override
		public Set<K> keySet() {
			return AbstractMyBiMultimap.this.keySet();
		}

		@Override
		public Collection<V> remove(Object key) {
			Collection<V> collection = submap.remove(key);
			if (collection == null) {
				return null;
			}

			Collection<V> output = new ArrayList<V>(initialCollectionSize);

			Iterator<V> iterator = collection.iterator();
			while (iterator.hasNext()) {
				removeFromInverse(key, iterator.next());
			}
			output.addAll(collection);
			totalSize -= collection.size();
			AbstractMyBiMultimap.this.inverse.totalSize-= collection.size();
			collection.clear();
			return output;
		}

		class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
			@Override
			public Iterator<Map.Entry<K, Collection<V>>> iterator() {
				return new AsMapIterator();
			}

			@Override
			public int size() {
				return submap.size();
			}
			@Override
			public boolean remove(Object o) {
				if (!contains(o)) {
					return false;
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;

				Collection<V> collection;
				try {
					collection = submap.get(entry.getKey());
				} catch (NullPointerException e) {
					return false;
				} catch (ClassCastException e) {
					return false;
				}

				if (collection == null) {
					return false;
				}
				int count = collection.size();

				Iterator<V> iterator = collection.iterator();
				while (iterator.hasNext()) {
					removeFromInverse(entry.getKey(), iterator.next());
				}
				collection.clear();
				totalSize -= count;
				AbstractMyBiMultimap.this.inverse.totalSize-= count;
				return true;
			}
		}

		class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>> {
			final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = submap
					.entrySet().iterator();
			Collection<V> collection;
			K key;

			public boolean hasNext() {
				return delegateIterator.hasNext();
			}

			public Map.Entry<K, Collection<V>> next() {
				Map.Entry<K, Collection<V>> entry = delegateIterator.next();
				key = entry.getKey();
				collection = entry.getValue();
				return Maps.immutableEntry(key, collection);
			}
			public void remove() {
				Iterator<V> iterator = collection.iterator();
				while (iterator.hasNext()) {
					removeFromInverse(key, iterator.next());
				}
				delegateIterator.remove();
				totalSize -= collection.size();
				AbstractMyBiMultimap.this.inverse.totalSize-= collection.size();
				collection.clear();
			}
		}
	}
	public AbstractMyBiMultimap<V, K> inverse() {
		return inverse;
	}
}
