/**
 * 
 */
package M1International.MultimapAdvanced;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author waseem
 *
 */
public class AdvancedMultimap<K, V> extends AbstractMyMultimap<K, V> {

	public static <K, V> AdvancedMultimap<K, V> create() {
		return new AdvancedMultimap<K, V>();
	}

	private AdvancedMultimap() {

		super(new HashMap<K, Collection<V>>(), new HashMap<V, Collection<K>>());
	}
}
