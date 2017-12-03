package com.redsun.netty.protocol.stack;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

public class ChannelBufferByteInput implements ByteInput {

    ByteBuf byteBuf;

    public ChannelBufferByteInput(ByteBuf out) {
        this.byteBuf = out;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return 0;
    }

    @Override
    public int available() throws IOException {
        return 0;
    }

    @Override
    public long skip(long n) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
