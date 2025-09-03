package com.microsoft.xal.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaHasher {
    private MessageDigest md = MessageDigest.getInstance("SHA-256");

    public ShaHasher() throws NoSuchAlgorithmException {
    }

    public void AddBytes(byte[] bArr) {
        this.md.update(bArr);
    }

    byte[] SignHash() {
        return this.md.digest();
    }
}
