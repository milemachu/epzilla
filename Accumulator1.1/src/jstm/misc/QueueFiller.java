/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

import java.util.concurrent.ArrayBlockingQueue;

public final class QueueFiller extends BinaryReader.Filler {

    private final ArrayBlockingQueue<byte[]> _queue;

    public QueueFiller(ArrayBlockingQueue<byte[]> queue) {
        _queue = queue;
    }

    @Override
    public int fill(byte[] buffer) {
        try {
            byte[] data = _queue.take();
            Debug.assertion(data.length <= buffer.length);
            System.arraycopy(data, 0, buffer, 0, data.length);
            return data.length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
