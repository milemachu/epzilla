/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.methods;

import jstm.tests.generated.SimpleStructure;

public class SimpleStructureImpl extends SimpleStructure {

    @Override
    protected String method(String sql, SimpleStructure eg) {
        if (getInt() == null)
            setInt(0);
        else
            setInt(getInt().intValue() + 1);

        return getInt().toString();
    }
}
