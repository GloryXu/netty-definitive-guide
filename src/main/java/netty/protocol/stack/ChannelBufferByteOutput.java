package netty.protocol.stack;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

public class ChannelBufferByteOutput implements ByteOutput {

    ByteBuf byteBuf;

    public ChannelBufferByteOutput(ByteBuf out) {
        this.byteBuf = out;
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
