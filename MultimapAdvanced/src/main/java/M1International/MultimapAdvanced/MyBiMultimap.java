/**
 * 
 */
package M1International.MultimapAdvanced;

import java.lang.Object;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Multiset;

/**
 * @author waseem
 *
 */
public interface MyBiMultimap<K, V> {
	
	boolean put(K key, V value);

	boolean containsKey(Object key);
	
	boolean containsValue(Object value);
	
	boolean containsEntry(Object key,Object value);
	
	boolean remove(Object key,Object value);
	
	boolean putAll(K key, Iterable<? extends V> values);
	
	Set<K> keySet();
	
	Multiset<K> keys();
	
	Collection<V> values();
	
	Collection<Map.Entry<K, V>> entries();
	
	Collection<V> get(K key);
	
	Map<K, Collection<V>> asMap();

	MyBiMultimap<V, K> inverse();
	
	int size();

	boolean isEmpty();
	
	void clear();
	
}
