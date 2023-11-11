package net.tiny.crypto.tests;

import net.tiny.crypto.CryptoUtils;
import net.tiny.crypto.Curve25519;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Curve25519Test {

    @Test
    public void testPubKeyFromSecret() {
        try(Curve25519 curve25519 = new Curve25519()) {

            byte[] secret = CryptoUtils.hexStringToBytes("202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f");

            byte[] pubKey = new byte[32];
            curve25519.publicKey(secret, pubKey);
            String hex = CryptoUtils.toHex(pubKey, null);

            String expectePubKey = "358072d6365880d1aeea329adf9121383851ed21a28e3b75e965d0d2cd166254".toUpperCase();
            Assertions.assertEquals(expectePubKey, hex);

            byte[] otherPrivKey = CryptoUtils.hexStringToBytes("909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeaf");
            byte[] otherPubKey = CryptoUtils.hexStringToBytes("9fd7ad6dcff4298dd3f96d5b1b2af910a0535b1488d7f8fabb349a982880b615");
            byte[] sharedSecret = new byte[32];
            curve25519.shareSecret(sharedSecret, secret, otherPubKey);
            String shareSecretHex = CryptoUtils.toHex(sharedSecret, null);
            String expectedSharedSecret = "df4a291baa1eb7cfa6934b29b474baad2697e29f1f920dcc77c8a0a088447624".toUpperCase();
            Assertions.assertEquals(expectedSharedSecret, shareSecretHex);

            //derive shared secret from otherside using otherPrivKey and pubKey
            byte[] otherSharedSecret  = new byte[32];
            curve25519.shareSecret(otherSharedSecret, otherPrivKey, pubKey);
            String otherSharedSecretHex = CryptoUtils.toHex(otherSharedSecret, null);
            Assertions.assertEquals(expectedSharedSecret, otherSharedSecretHex);

        }
    }

}
