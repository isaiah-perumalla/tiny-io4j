package net.tiny.crypto;

import net.tiny.crypto.libsodium.crypto_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class CryptoUtils {
   static final Arena arena = Arena.global();
   static final MemorySegment buffer32 = arena.allocate(32);
    static {
        System.loadLibrary("sodium");
        final int loadResult = crypto_h.sodium_init();
        if (loadResult < 0) {
            throw new IllegalStateException("cannot init libsodium");
        }
    }

        public static void randomBytes32(byte[] output) {
            crypto_h.randombytes_buf(buffer32, 32);
            for (int i = 0; i < 32; i++) {
                output[i] = buffer32.getAtIndex(ValueLayout.JAVA_BYTE, i);
            }
        }

    public static byte[] hexToBytes(String s) {
        byte[] bytes = new byte[s.length()/2];
        hexToBytes(s, bytes);
        return bytes;
    }
    public static void hexToBytes(String s, byte[] bytes) {
        assert s.length() % 2 == 0;
        assert bytes.length >= s.length() / 2;
        int len = s.length();
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }

    }

    public static String toHex(byte[] bytes, Character sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02X", bytes[i]));
            if (sep != null && i != bytes.length -1) {
                sb.append(sep);
            }
        }
        return sb.toString();
    }

    public static void init() {

    }

    public static byte[] hexStringToBytes(String hex) {
        byte[] bytes = new byte[32];
        hexToBytes(hex,
                bytes);
        return bytes;
    }

    public static int sha256(MemorySegment out, MemorySegment memorySegment, int length) {
        return crypto_h.crypto_hash_sha256(out, memorySegment, length);
    }

    public static String toHex(MemorySegment memorySegment, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X", memorySegment.getAtIndex(ValueLayout.JAVA_BYTE, i)));
        }
        return sb.toString();
    }
}

