package net.tiny.tls;

public enum CipherSuites {
    TLS_AES_128_GCM_SHA256(0x01),
    TLS_CHACHA20_POLY1305_SHA256(0x03);

    private final int code;

    CipherSuites(int code) {

        this.code = code;
    }
}
