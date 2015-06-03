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
public class AdvancedMyMultimap<K, V> extends AbstractMyMultimap<K, V> {

	public static <K, V> AdvancedMyMultimap<K, V> create() {
		return new AdvancedMyMultimap<K, V>();
	}

	private AdvancedMyMultimap() {

		super(new HashMap<K, Collection<V>>(), new HashMap<V, Collection<K>>());
	}
}
