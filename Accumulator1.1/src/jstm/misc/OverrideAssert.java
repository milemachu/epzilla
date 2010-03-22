/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows to test if a function override calls it's base. Add before and after
 * each call to the function to test calls to 'add' and 'end', and 'set' in the
 * base declaration. If the base is not called, 'end' will fail.
 */
public final class OverrideAssert {

    private static ThreadLocal<List<Boolean>> _bools = new ThreadLocal<List<Boolean>>();

    private static ThreadLocal<ArrayList<Object>> _keys = new ThreadLocal<ArrayList<Object>>();

    private OverrideAssert() {
    }

    private static ArrayList<Boolean> getBools() {
        ArrayList<Boolean> bools = (ArrayList<Boolean>) _bools.get();

        if (bools == null)
            _bools.set(bools = new ArrayList<Boolean>());

        return bools;
    }

    private static ArrayList<Object> getKeys() {
        ArrayList<Object> keys = _keys.get();

        if (keys == null)
            _keys.set(keys = new ArrayList<Object>());

        return keys;
    }

    public static void add(Object key) {
        if (Debug.Level > 0) {
            getBools().add(false);
            getKeys().add(key);

            Debug.assertion(getBools().size() == getKeys().size());
        }
    }

    public static void set(Object key) {
        if (Debug.Level > 0) {
            ArrayList<Boolean> bools = getBools();
            ArrayList<Object> keys = getKeys();

            Debug.assertion(!bools.remove(bools.size() - 1).booleanValue());
            bools.add(new Boolean(true));
            Debug.assertion(key == keys.get(keys.size() - 1));

            Debug.assertion(bools.size() == keys.size());
        }
    }

    public static void end(Object key) {
        if (Debug.Level > 0) {
            ArrayList<Boolean> bools = getBools();
            ArrayList<Object> keys = getKeys();

            Debug.assertion(bools.remove(bools.size() - 1).booleanValue());
            Debug.assertion(key == keys.remove(keys.size() - 1));

            Debug.assertion(bools.size() == keys.size());
        }
    }
}
