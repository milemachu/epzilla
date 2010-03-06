/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

public final class Debug {

    public static final int Level = 0;

    public static final boolean MESSAGING = false;

    public static final boolean ASSERT_COMMUNICATIONS = false;

    private Debug() {
    }

    public static void assertion(boolean value) {
        assertion(value, "");
    }

    public static void assertion(boolean value, String message) {
        if (Level > 0 && !value)
            throw new RuntimeException("Assertion failed " + message);
    }

    public static void fail() {
        fail(null);
    }

    public static void fail(String message) {
        if (Level > 0)
            assertion(false, message);
    }

    public static void assertAlways(boolean value) {
        if (!value)
            throw new RuntimeException("Assertion failed");
    }

    public static void log(int level, String message) {
        if (Level >= level)
            log(message);
    }

    public static void log(String message) {
        Log.write(Thread.currentThread().getName() + ": " + message);
    }
}
