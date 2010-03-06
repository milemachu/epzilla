/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.vm;

import jstm.core.Site;
import jstm.tests.ClientTest;
import jstm.tests.generated.SimpleObjectModel;
import jstm.transports.clientserver.vm.VMClient;

public class DistributedVMTestClient extends ClientTest {

    private final DistributedVMTest _test;

    public DistributedVMTestClient(DistributedVMTest test, int number) {
        _test = test;

        _site = Site.createTestSite();
        _client = test.getServer().createClient(_site, number);
        _number = number;

        _site.registerObjectModel(new SimpleObjectModel());
        
        if (test.getConflicting())
            _structureName = "Structure 0";
    }

    @Override
    protected void onStart() {
        _test.started(this);
    }

    @Override
    protected void onFinished() {
        _test.getServer().remove((VMClient) _client);
    }
}
