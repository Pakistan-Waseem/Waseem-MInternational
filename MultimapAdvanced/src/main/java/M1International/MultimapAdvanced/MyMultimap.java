package M1International.MultimapAdvanced;

/**
 * 
 */
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.*;

import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author waseem
 *
 */
public final class MyMultimap<K, V> implements Multimap<K, V> {

	private static final int VALUESPER_KEY = 5;
	transient int expectedValuesPerKey;

	private transient Map<K, Collection<V>> map;
	private transient int totalSize;

	private MyMultimap() {
		// TODO Auto-generated constructor stub
		map = new HashMap<K, Collection<V>>();
		expectedValuesPerKey = VALUESPER_KEY;
	}

	private MyMultimap(int expectedKeys, int expectedValuesPerKey1) {
		// TODO Auto-generated constructor stub
		Maps.<K, Collection<V>> newHashMapWithExpectedSize(expectedKeys);
		checkArgument(expectedValuesPerKey >= 0);
		this.expectedValuesPerKey = expectedValuesPerKey1;
	}

	public static <K, V> MyMultimap<K, V> create() {
		return new MyMultimap<K, V>();
	}

	public static <K, V> MyMultimap<K, V> create(int expectedKeys,
			int expectedValuesPerKey) {
		return new MyMultimap<K, V>(expectedKeys, expectedValuesPerKey);
	}

	public int size() {
		// TODO Auto-generated method stub
		return totalSize;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return totalSize == 0;
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		for (Collection<V> collection : map.values()) {
			if (collection.contains(value)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsEntry(Object key, Object value) {
		// TODO Auto-generated method stub
		Collection<V> collection = map.get(key);
		return collection != null && collection.contains(value);
	}

	public boolean put(K key, V value) {
		// TODO Auto-generated method stub
		Collection<V> collection = getOrCreateCollection(key);

		if (collection.add(value)) {
			totalSize++;
			return true;
		} else {
			return false;
		}
	}

	private Collection<V> getOrCreateCollection(K key) {
		Collection<V> collection = map.get(key);
		if (collection == null) {
			collection = createCollection(key);
			map.put(key, collection);
		}
		return collection;
	}

	private Collection<V> createCollection(K key) {
		// TODO Auto-generated method stub
		return createCollection();
	}

	private Collection<V> createCollection() {
		return new ArrayList<V>(expectedValuesPerKey);
	}

	public boolean remove(Object key, Object value) {
		// TODO Auto-generated method stub
		Collection<V> collection = map.get(key);
		if (collection == null) {
			return false;
		}

		boolean changed = collection.remove(value);
		if (changed) {
			totalSize--;
			if (collection.isEmpty()) {
				map.remove(key);
			}
		}
		return changed;
	}

	public Collection<V> removeAll(Object key) {
		// TODO Auto-generated method stub
		Collection<V> collection = map.remove(key);
		Collection<V> output = createCollection();

		if (collection != null) {
			output.addAll(collection);
			totalSize -= collection.size();
			collection.clear();
		}

		return unmodifiableCollectionSubclass(output);
	}

	public boolean putAll(K key, Iterable<? extends V> values) {
		// TODO Auto-generated method stub
		if (!values.iterator().hasNext()) {
			return false;
		}
		Collection<V> collection = getOrCreateCollection(key);
		int oldSize = collection.size();

		boolean changed = false;
		if (values instanceof Collection) {
			Collection<? extends V> c = (Collection<? extends V>) values;
			changed = collection.addAll(c);
		} else {
			for (V value : values) {
				changed |= collection.add(value);
			}
		}

		totalSize += (collection.size() - oldSize);
		return changed;
	}

	public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
		// TODO Auto-generated method stub
		boolean changed = false;
		for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
			changed |= put(entry.getKey(), entry.getValue());
		}
		return changed;
	}

	public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
		// TODO Auto-generated method stub
		Iterator<? extends V> iterator = values.iterator();
		if (!iterator.hasNext()) {
			return removeAll(key);
		}

		Collection<V> collection = getOrCreateCollection(key);
		Collection<V> oldValues = createCollection();
		oldValues.addAll(collection);

		totalSize -= collection.size();
		collection.clear();

		while (iterator.hasNext()) {
			if (collection.add(iterator.next())) {
				totalSize++;
			}
		}

		return unmodifiableCollectionSubclass(oldValues);
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
		// TODO Auto-generated method stub
		for (Collection<V> collection : map.values()) {
			collection.clear();
		}
		map.clear();
		totalSize = 0;
	}

	public Collection<V> get(K key) {
		// TODO Auto-generated method stub
		Collection<V> collection = map.get(key);
		return collection;
	}

	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	private transient Multiset<K> keys;

	public Multiset<K> keys() {
		// TODO Auto-generated method stub
		Multiset<K> multiset = keys;
		return (multiset == null) ? keys = new MyMultiset() : multiset;
	}

	@SuppressWarnings("unused")
	private class MyMultiset implements Multiset<K> {

		public boolean addAll(Collection<? extends K> c) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public void clear() {
			// TODO Auto-generated method stub
			MyMultimap.this.clear();
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
				collection = map.get(element);
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
			if (occurrences == 0) {
				return count(element);
			}
			checkArgument(occurrences > 0);

			Collection<V> collection;
			try {
				collection = map.get(element);
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
				int elements = collection.size();
				map.remove(element);
				totalSize = -elements;
				return elements;
			}

			Iterator<V> iterator = collection.iterator();
			for (int i = 0; i < occurrences; i++) {
				iterator.next();
				iterator.remove();
			}
			totalSize -= occurrences;
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
			return map.containsKey(element);
		}

		public boolean containsAll(Collection<?> elements) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public boolean add(K element) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object element) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
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
			return MyMultimap.this.size();
		}

		@Override
		public boolean contains(Object o) {
			return MyMultimap.this.containsValue(o);
		}

		@Override
		public void clear() {
			MyMultimap.this.clear();
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
		Collection<V> collection;
		Iterator<V> valueIterator;

		EntryIterator() {
			keyIterator = map.entrySet().iterator();
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
			return Maps.immutableEntry(key, valueIterator.next());
		}

		public void remove() {
			valueIterator.remove();
			if (collection.isEmpty()) {
				keyIterator.remove();
			}
			totalSize--;
		}
	}

	private transient Collection<Map.Entry<K, V>> entries;

	public Collection<Entry<K, V>> entries() {
		// TODO Auto-generated method stub
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
			MyMultimap.this.clear();
		}

		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
			return MyMultimap.this.remove(entry.getKey(), entry.getValue());
		}
	}

	Iterator<Map.Entry<K, V>> createEntryIterator() {
		return new EntryIterator();
	}

	public Map<K, Collection<V>> asMap() {
		// TODO Auto-generated method stub
		return map;
	}

	public void trimToSize() {
		for (Collection<V> collection : map.values()) {
			ArrayList<V> arrayList = (ArrayList<V>) collection;
			arrayList.trimToSize();
		}
	}
}
