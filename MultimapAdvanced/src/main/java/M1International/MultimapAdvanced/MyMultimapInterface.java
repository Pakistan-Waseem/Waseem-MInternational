/**
 * 
 */
package M1International.MultimapAdvanced;

import java.util.Collection;

/**
 * @author waseem
 *
 */
public interface MyMultimapInterface<K, V> {
	boolean add(K key, V value);

	boolean containsKey(K key);

	MyMultimapInterface<V, K> inverse();

	Collection<?> get(K key);

	int size();

	boolean isEmpty();
}
