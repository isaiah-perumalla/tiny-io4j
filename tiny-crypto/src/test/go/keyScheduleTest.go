package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"golang.org/x/crypto/hkdf"
)

func main() {
	hash := sha256.New()
	zeros := make([]byte, 1)
	psk := make([]byte, 32)
	h := sha256.New
	earlySecret := hkdf.Extract(h, psk, zeros)
	hexStr := hex.EncodeToString(earlySecret)
	emptyHash := hash.Sum(nil)
	fmt.Printf("earlySecret: %s\n", hexStr)
	fmt.Printf("emptyHash: %s\n", hex.EncodeToString(emptyHash))
}