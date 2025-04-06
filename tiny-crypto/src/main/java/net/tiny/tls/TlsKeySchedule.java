package net.tiny.tls;

import net.tiny.crypto.CryptoUtils;
import net.tiny.crypto.libsodium.crypto_h;

import java.lang.foreign.MemorySegment;

public class TlsKeySchedule {

    private TlsKeySchedule(byte[] sharedSecret, byte[] helloHash) {

    }
    public static TlsKeySchedule sha256Schedule(byte[] sharedSecret, byte[] helloHash) {
        TlsKeySchedule tlsKeySchedule = new TlsKeySchedule(sharedSecret, helloHash);
        tlsKeySchedule.init();
        return tlsKeySchedule;
    }

    private void init() {

    }

    public byte[] getHandshakeSecret() {
        return new byte[0];
    }



}



