package net.tiny.crypto;

import net.tiny.crypto.libsodium.crypto_h;
import net.tiny.tls.Sha256;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.charset.StandardCharsets;

public class CryptoUtils {
    public static final byte[] Tls13Sha256EarlySecret;
    public final static byte[] Tls13Sha256DerivedSecret;
    public static final byte[] Sha256EmptyHash;
   static final Arena arena = Arena.global();
   static final MemorySegment buffer32_0 = arena.allocate(32);
   static final MemorySegment buffer32_1 = arena.allocate(32);
   static final MemorySegment buffer32_2 = arena.allocate(32);

    static {
        System.loadLibrary("sodium");
        final int loadResult = crypto_h.sodium_init();
        if (loadResult < 0) {
            throw new IllegalStateException("cannot init libsodium");
        }
        MemorySegment secret = buffer32_0;
        MemorySegment salt = zeros(buffer32_1, 1);
        MemorySegment zeros = zeros(buffer32_2, 32);

        int res = crypto_h.crypto_kdf_hkdf_sha256_extract(secret, salt, 1, zeros, 32);
        Tls13Sha256EarlySecret = toByteArray(secret, 32);
        crypto_h.crypto_hash_sha256(secret, zeros, 0);
        Sha256EmptyHash = toByteArray(secret, 32);

        byte[] derivedSecret = new byte[32];
        Sha256.hkdfExpandLabel(Tls13Sha256EarlySecret, "derived".getBytes(), Sha256EmptyHash, derivedSecret);
        Tls13Sha256DerivedSecret = derivedSecret;
    }

    private static byte[] toByteArray(MemorySegment secret, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = secret.getAtIndex(ValueLayout.JAVA_BYTE, i);
        }
        return b;
    }

    public static MemorySegment zeros(MemorySegment segment, int len) {
        for (int i = 0; i < len; i++) {
            segment.setAtIndex(ValueLayout.JAVA_BYTE, i, (byte) 0 );
        }
        return segment;
    }

    public static void randomBytes32(byte[] output) {
            crypto_h.randombytes_buf(buffer32_0, 32);
            for (int i = 0; i < 32; i++) {
                output[i] = buffer32_0.getAtIndex(ValueLayout.JAVA_BYTE, i);
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

