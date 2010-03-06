/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.Arrays;
import java.util.Date;

public final class ImmutableClasses {

    public static final int BoolIndex = 0;

    public static final int ByteIndex = 1;

    public static final int CharIndex = 2;

    public static final int ShortIndex = 3;

    public static final int IntIndex = 4;

    public static final int LongIndex = 5;

    public static final int FloatIndex = 6;

    public static final int DoubleIndex = 7;

    public static final int StringIndex = 8;

    public static final int DateIndex = 9;

    public static final Class[] Little;

    public static final Class[] Big;

    static {
        Little = new Class[] { boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class };
        Big = new Class[] { Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class };
    }

    public static int getIndex(Class type) {
        int classIndex = Arrays.asList(ImmutableClasses.Little).indexOf(type);

        if (classIndex == -1)
            classIndex = Arrays.asList(ImmutableClasses.Big).indexOf(type);

        return classIndex;
    }

    public static boolean supported(Class type) {
        if (type == String.class || type == Date.class || type == void.class)
            return true;

        return getIndex(type) != -1;
    }

    public static boolean convertible(int index) {
        return index != StringIndex && index != DateIndex;
    }
}
