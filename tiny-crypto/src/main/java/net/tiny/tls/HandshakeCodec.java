package net.tiny.tls;
import net.tiny.crypto.CryptoUtils;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;

import static net.tiny.tls.HandshakeCodec.Type.ClientHello;
import static net.tiny.tls.HandshakeCodec.Type.ServerHello;

public class HandshakeCodec {


    public static int concat(Decoder a, Decoder b, MemorySegment memorySegment) {
        int sizea = a.copyWithoutHeader(memorySegment, 0);
        int sizeb = b.copyWithoutHeader(memorySegment, sizea);
        return sizea + sizeb;
    }

    public enum Type {
        ClientHello, ServerHello
    }
    public static class Decoder {
        private int handShakeRecordSize;
        private Type hanshakeType;
        private MutableDirectBuffer buffer = new UnsafeBuffer(new byte[4096]);
        private short size;

        public void init(DirectBuffer directBuffer, int offset) {
            short size = directBuffer.getShort(3, ByteOrder.BIG_ENDIAN);
            handShakeRecordSize = directBuffer.getInt(5, ByteOrder.BIG_ENDIAN) & 0x00FFFFFF;
            assert size == handShakeRecordSize + 4;
            final byte type = directBuffer.getByte(offset);
            if (type != 0x16) {
                throw new IllegalArgumentException("not a handshake record");
            }
            hanshakeType = getHandShakeType(directBuffer.getByte(5));
            buffer.putBytes(0, directBuffer, offset + 5, size);
            this.size = size;
        }

        private Type getHandShakeType(byte aByte) {
            if (aByte == 0x1) return ClientHello;
            if (aByte == 0x2) return ServerHello;
            throw new IllegalArgumentException("invalid handshake type" + aByte);
        }

        public int handShakeSize() {
            return handShakeRecordSize;
        }

        public Type type() {
            return hanshakeType;
        }

        public int copyWithoutHeader(MemorySegment memorySegment, int offset) {
            for (int i = 0; i < size; i++) {
                memorySegment.setAtIndex(ValueLayout.JAVA_BYTE, offset + i, buffer.getByte(i));
            }
            return size;
        }
    }
}
