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
public class AdvancedMyBiMultimap<K, V> extends AbstractMyBiMultimap<K, V> {

	public static <K, V> AdvancedMyBiMultimap<K, V> create() {
		return new AdvancedMyBiMultimap<K, V>();
	}

	private AdvancedMyBiMultimap() {

		super(new HashMap<K, Collection<V>>(), new HashMap<V, Collection<K>>());
	}
}
