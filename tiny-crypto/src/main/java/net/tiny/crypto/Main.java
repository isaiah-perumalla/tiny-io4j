package net.tiny.crypto;

import net.tiny.crypto.libsodium.crypto_h;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public class Main {


    public static void main(String[] args) {


        CryptoUtils.init();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment key = arena.allocate(32);
            crypto_h.randombytes_buf(key, 32);
            int hexSize = 32 * 2 + 1;
            MemorySegment hex = arena.allocate(hexSize); //hex is longer
            MemorySegment ret = crypto_h.sodium_bin2hex(hex, hexSize, key, 32);

            System.out.println(hex.getUtf8String(0));

                Curve25519 curve25519 = new Curve25519();
                byte[] secret = new byte[32];
                CryptoUtils.hexToBytes("202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f",
                        secret);

                byte[] pubKey = new byte[32];
                curve25519.publicKey(secret, pubKey);
            String hexStr = CryptoUtils.toHex(pubKey, null);
            System.out.println(hexStr);

            }
    }

    static void hexToBytes(String hex, MemorySegment bytes) {

    }
}