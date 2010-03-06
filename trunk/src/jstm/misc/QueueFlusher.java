/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.concurrent.ArrayBlockingQueue;

public final class QueueFlusher extends BinaryWriter.Flusher {

    public final ArrayBlockingQueue<byte[]> Queue;

    public QueueFlusher() {
        Queue = new ArrayBlockingQueue<byte[]>(10);
    }

    @Override
    public void flush(byte[] buffer, int length, boolean forced) {
        try {
            byte[] copy = new byte[length];
            System.arraycopy(buffer, 0, copy, 0, length);
            Queue.put(copy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
