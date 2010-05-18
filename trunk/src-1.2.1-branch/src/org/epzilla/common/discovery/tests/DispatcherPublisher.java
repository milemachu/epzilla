package org.epzilla.common.discovery.tests;

import static org.junit.Assert.*;

import org.epzilla.common.discovery.multicast.MulticastReceiver;
import org.epzilla.util.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DispatcherPublisher {
	private String multicastGroupIp = "224.0.0.2";
	private int multicastPort = 5005;
	private boolean isReceived=false;

	Thread t;

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testAddSubscription() {
		// fail("Not yet implemented");
	}

	@Test
	public void testPublishService() {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				MulticastReceiver receiver = new MulticastReceiver(
						multicastGroupIp, multicastPort);

				while (!isReceived) {
					Logger.error(receiver.messageReceived(), null);
					isReceived=true;
				}
			}
		});

		t.start();

		org.epzilla.common.discovery.dispatcher.DispatcherPublisher dp = new org.epzilla.common.discovery.dispatcher.DispatcherPublisher();
		assertTrue(dp.publishService());

		while (!isReceived) {
			
		}
	
		t = null;
	}

	@Test
	public void testRemoveSubscrition() {
		// fail("Not yet implemented");
	}

}
