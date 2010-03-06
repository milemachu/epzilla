/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.concurrent.*;

public final class ThreadPool {

    public final static class Task {

        private ScheduledFuture _future;

        public Task(ScheduledFuture future) {
            _future = future;
        }

        public void cancel() {
            _future.cancel(false);
        }
    }

    private static final ScheduledThreadPoolExecutor _executor;

    static {
        _executor = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    private ThreadPool() {
    }

    public static Task scheduleAtFixedRate(Runnable runnable, int period) {
        return new Task(_executor.scheduleAtFixedRate(runnable, 0, period, TimeUnit.MILLISECONDS));
    }
}
