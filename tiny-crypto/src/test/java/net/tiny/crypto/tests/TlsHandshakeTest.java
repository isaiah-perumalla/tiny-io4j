package net.tiny.crypto.tests;

import net.tiny.crypto.CryptoUtils;
import net.tiny.tls.HandshakeCodec;
import net.tiny.tls.TlsKeySchedule;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Locale;

public class TlsHandshakeTest {
    private final HandshakeCodec.Decoder clientHelloDecoder = new HandshakeCodec.Decoder();
    private final HandshakeCodec.Decoder serverHelloDecoder = new HandshakeCodec.Decoder();
    private final UnsafeBuffer buffer = new UnsafeBuffer(new byte[1024]);

    String clientHelloHex = "16 03 01 00 f8 01 00 00 f4 03 03 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 e0 e1 e2 e3 e4 e5 e6 e7 e8 e9 ea eb ec ed ee ef f0 f1 f2 f3 f4 f5 f6 f7 f8 f9 fa fb fc fd fe ff 00 08 13 02 13 03 13 01 00 ff 01 00 00 a3 00 00 00 18 00 16 00 00 13 65 78 61 6d 70 6c 65 2e 75 6c 66 68 65 69 6d 2e 6e 65 74 00 0b 00 04 03 00 01 02 00 0a 00 16 00 14 00 1d 00 17 00 1e 00 19 00 18 01 00 01 01 01 02 01 03 01 04 00 23 00 00 00 16 00 00 00 17 00 00 00 0d 00 1e 00 1c 04 03 05 03 06 03 08 07 08 08 08 09 08 0a 08 0b 08 04 08 05 08 06 04 01 05 01 06 01 00 2b 00 03 02 03 04 00 2d 00 02 01 01 00 33 00 26 00 24 00 1d 00 20 35 80 72 d6 36 58 80 d1 ae ea 32 9a df 91 21 38 38 51 ed 21 a2 8e 3b 75 e9 65 d0 d2 cd 16 62 54".replace(" ", "");
    String serverHelloHex = "16 03 03 00 7a 02 00 00 76 03 03 70 71 72 73 74 75 76 77 78 79 7a 7b 7c 7d 7e 7f 80 81 82 83 84 85 86 87 88 89 8a 8b 8c 8d 8e 8f 20 e0 e1 e2 e3 e4 e5 e6 e7 e8 e9 ea eb ec ed ee ef f0 f1 f2 f3 f4 f5 f6 f7 f8 f9 fa fb fc fd fe ff 13 02 00 00 2e 00 2b 00 02 03 04 00 33 00 24 00 1d 00 20 9f d7 ad 6d cf f4 29 8d d3 f9 6d 5b 1b 2a f9 10 a0 53 5b 14 88 d7 f8 fa bb 34 9a 98 28 80 b6 15".replace(" ", "");
    @Test
    public void testComputeTls1_3HandshakeHash() {
        byte[] clientHello = CryptoUtils.hexToBytes(clientHelloHex);
//        printBytes(clientHello, System.out);
        buffer.putBytes(0, clientHello);
        clientHelloDecoder.init(buffer, 0);
        Assertions.assertEquals(244, clientHelloDecoder.handShakeSize());
        Assertions.assertEquals(HandshakeCodec.Type.ClientHello, clientHelloDecoder.type());

        byte[] serverHello = CryptoUtils.hexToBytes(serverHelloHex);
//        printBytes(serverHello, System.out);
        buffer.putBytes(0, serverHello);
        serverHelloDecoder.init(buffer, 0);
        Assertions.assertEquals(118, serverHelloDecoder.handShakeSize());
        Assertions.assertEquals(HandshakeCodec.Type.ServerHello, serverHelloDecoder.type());

        String expectedHashSha256 = "b0a929f2178bb6983e1956842490b330557003ef032704fd88e1cd5ecf98e0ac".toUpperCase();
// sha384 -> e05f64fcd082bdb0dce473adf669c2769f257a1c75a51b7887468b5e0e7a7de4f4d34555112077f16e079019d5a845bd
        try(Arena arena = Arena.ofConfined()) {
            byte[] hash = new byte[32];
            MemorySegment memorySegment = arena.allocate(2048);
            MemorySegment sha256Out = arena.allocate(32);
            int length = HandshakeCodec.concat(clientHelloDecoder, serverHelloDecoder, memorySegment);
            System.out.println(CryptoUtils.toHex(memorySegment, length));
            CryptoUtils.sha256(sha256Out, memorySegment, length);
            Assertions.assertEquals(expectedHashSha256, CryptoUtils.toHex(sha256Out, 32));
        }
    }

    @Test
    public void testTls1_3_KeySchedule() {
        String earlySecretHex = CryptoUtils.toHex(CryptoUtils.Tls13Sha256EarlySecret, null);
        String empty256Hex = CryptoUtils.toHex(CryptoUtils.Sha256EmptyHash, null);
        String derivedSecret = CryptoUtils.toHex(CryptoUtils.Tls13Sha256DerivedSecret, null);
        Assertions.assertEquals("33ad0a1c607ec03b09e6cd9893680ce210adf300aa1f2660e1b22e10f170f92a".toUpperCase(), earlySecretHex);
        Assertions.assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".toUpperCase(), empty256Hex);
        Assertions.assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".toUpperCase(), derivedSecret);
        byte[] sharedSecret = CryptoUtils.hexStringToBytes("df4a291baa1eb7cfa6934b29b474baad2697e29f1f920dcc77c8a0a088447624");
        byte[] helloHash =    CryptoUtils.hexStringToBytes("b0a929f2178bb6983e1956842490b330557003ef032704fd88e1cd5ecf98e0ac");
        TlsKeySchedule keySchedule = TlsKeySchedule.sha256Schedule(sharedSecret, helloHash);
        byte[] handShakeSecret = keySchedule.getHandshakeSecret();
    }
}
