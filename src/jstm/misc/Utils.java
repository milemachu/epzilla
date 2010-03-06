/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Utils {

    private static final SecureRandom _rand = new SecureRandom();

    private Utils() {
    }

    /**
     * toArray not supported by gwt
     */
    public static <T> T[] copy(Set<?> set, T[] array) {
        return set.toArray(array);
    }

    /**
     * toArray not supported by gwt
     */
    public static void copy(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * arraycopy not supported by gwt
     */
    public static void copy(Object[] src, int srcPos, Object[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * arraycopy not supported by gwt
     */
    public static void copy(boolean[] src, int srcPos, boolean[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * toArray not supported by gwt
     */
    public static void copy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * clone not supported on arrays by gwt
     */
    public static <T> T[] clone(T[] array) {
        return array.clone();
    }

    /**
     * gwt hashmap raises concurrent exceptions
     */
    public static <K, V> Collection<V> values(ConcurrentHashMap<K, V> map) {
        return map.values();
    }

    /**
     * See UUID, adapted here for GWT support
     */
    public static String createUID() {
        byte[] bytes = new byte[16];
        _rand.nextBytes(bytes);
        char[] chars = Base64Coder.encode(bytes);
        Debug.assertion(chars[chars.length - 2] == '=' && chars[chars.length - 1] == '=');
        return new String(chars, 0, chars.length - 2);
    }

    /**
     * Lacking in GWT
     */
    public static String toString(long[] a) {
        if (a == null)
            return "null";

        if (a.length == 0)
            return "[]";

        StringBuilder buf = new StringBuilder();

        buf.append('[');
        buf.append(a[0]);

        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }

        buf.append("]");

        return buf.toString();
    }
}
