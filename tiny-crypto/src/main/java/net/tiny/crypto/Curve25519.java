package net.tiny.crypto;

import net.tiny.crypto.libsodium.crypto_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Curve25519 implements AutoCloseable {
    private final Arena arena;
    private final MemorySegment buffer0;
    private final MemorySegment buffer1;
    private final MemorySegment buffer2;

    public Curve25519() {
        arena = Arena.ofConfined();
        buffer0 = arena.allocate(32);
        buffer1 = arena.allocate(32);
        buffer2 = arena.allocate(32);
    }

    @Override
    public void close() {
        arena.close();
    }


    public void publicKey(byte[] secret, byte[] pubKey) {
        copyToSegment(secret, buffer1);
        crypto_h.crypto_scalarmult_base(buffer0, buffer1);
        copySegmentToBytes(buffer0, pubKey);
    }

    private void copySegmentToBytes(MemorySegment src, byte[] dst ) {
        for (int i = 0; i < 32; i++) {
            dst[i] = src.get(ValueLayout.JAVA_BYTE, i);
        }
    }

    private static void copyToSegment(byte[] bytes, MemorySegment memorySegment) {
        for (int i = 0; i < 32; i++) {
            memorySegment.setAtIndex(ValueLayout.JAVA_BYTE, i, bytes[i]);
        }
    }

    public void shareSecret(byte[] sharedSecret, byte[] secret, byte[] otherPubKey) {
        MemorySegment secretKey = buffer0;
        copyToSegment(secret, secretKey);
        MemorySegment pubKey = buffer1;
        copyToSegment(otherPubKey, pubKey);
        MemorySegment sharedKey = buffer2;
        crypto_h.crypto_scalarmult(sharedKey, secretKey, pubKey);
        copySegmentToBytes(sharedKey, sharedSecret);
    }
}
