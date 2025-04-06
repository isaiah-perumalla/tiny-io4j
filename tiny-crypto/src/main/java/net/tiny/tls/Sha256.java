package net.tiny.tls;

import net.tiny.crypto.CryptoUtils;
import net.tiny.crypto.libsodium.crypto_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class Sha256 {
    static final byte[] TLS_13_Bytes = "tls13 ".getBytes();
    public static final int HASH_SIZE = 32;
    private final Arena arena;
    private final MemorySegment buffer32_0;
    private final MemorySegment buffer32_1;
    private final MemorySegment buffer32_2;
    private MemorySegment dataBuffer;

    public Sha256(Arena arena) {
        this.arena = arena;
        buffer32_0 = arena.allocate(32);
        buffer32_1 = arena.allocate(32);
        buffer32_2 = arena.allocate(32);
        dataBuffer = arena.allocate(4096);
    }


    public void hkdfExpandLabel(byte[] secret, byte[] label, byte[] ctx, byte[] out) {
        MemorySegment secretKey = buffer32_0;
        copyFrom(secret,secret.length, secretKey);

        MemorySegment hkdfLabel = buffer32_1;
        makeHkdfLabel(hkdfLabel, label, ctx)

        crypto_h.crypto_kdf_hkdf_sha256_expand(out, HASH_SIZE, hkdfLabel, size, secretKey, keySize);

    }

    private void makeHkdfLabel(MemorySegment out, byte[] label, byte[] ctx, int labelLength) {
//        addBigEndianU16(labelLength, out, 0);
//        addU8LenghtPrefixedBytes(TLS_13_Bytes, label);
//        addU8LenghtPrefixedBytes(ctx);
    }

    private void copyFrom(byte[] src, int size, MemorySegment dst) {
        for (int i = 0; i < size; i++) {
            dst.setAtIndex(ValueLayout.JAVA_BYTE, i, src[i]);
        }
    }
}
