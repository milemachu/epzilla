/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Console {

    public static String readLine() {
        return readLine(null);
    }

    public static String readLine(String message) {
        if (message != null && message.length() > 0)
            System.out.println(message);

        try {
            return new LineNumberReader(new InputStreamReader((System.in))).readLine();
        } catch (IOException ioe) {
            return null;
        }
    }
}
