/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests;

import java.io.IOException;

import jstm.misc.BinaryReader;
import jstm.misc.BinaryWriter;

import org.junit.Assert;
import org.junit.Test;

public class BinaryTest extends TransactionsTest {

    private byte[] _data;

    private BinaryWriter createBinaryWriter() {
        return new BinaryWriter(new BinaryWriter.Flusher() {

            @Override
            public void flush(byte[] buffer, int length, boolean forced) {
                try {
                    _data = new byte[length];
                    System.arraycopy(buffer, 0, _data, 0, _data.length);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private BinaryReader createBinaryReader() {
        return new BinaryReader(new BinaryReader.Filler() {

            @Override
            public int fill(byte[] buffer) {
                try {
                    System.arraycopy(_data, 0, buffer, 0, _data.length);
                    return _data.length;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Test
    public void test1() throws IOException {
        BinaryWriter writer = createBinaryWriter();

        writer.writeBoolean(true);
        writer.writeByte((byte) 100);
        writer.writeShort((short) 4500);
        writer.writeInteger(546486321);
        writer.writeLong(86413183813L);
        writer.writeString("go go go");
        writer.flush();

        BinaryReader reader = createBinaryReader();

        Assert.assertTrue(reader.readBoolean());
        Assert.assertEquals((byte) 100, reader.readByte());
        Assert.assertEquals((short) 4500, reader.readShort());
        Assert.assertEquals(546486321, reader.readInteger());
        Assert.assertEquals(86413183813L, reader.readLong());
        Assert.assertEquals(reader.readString(), "go go go");
    }
}
