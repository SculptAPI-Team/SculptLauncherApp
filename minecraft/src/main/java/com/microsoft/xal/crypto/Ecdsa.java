package com.microsoft.xal.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.spongycastle.jce.provider.BouncyCastleProvider;

public class Ecdsa {
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String ECDSA_SIGNATURE_NAME = "NONEwithECDSA";
    private static final String EC_ALGORITHM_NAME = "secp256r1";
    private static final String KEY_ALIAS_PREFIX = "xal_";
    private KeyPair keyPair;
    private String uniqueId;

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public static Ecdsa restoreKeyAndId(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalArgumentException, NoSuchProviderException, ClassCastException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.microsoft.xal.crypto", 0);
        if (!sharedPreferences.contains("id") || !sharedPreferences.contains("public") || !sharedPreferences.contains("private")) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.clear();
            editorEdit.commit();
            return null;
        }
        String string = sharedPreferences.getString("public", "");
        String string2 = sharedPreferences.getString("private", "");
        String string3 = sharedPreferences.getString("id", "");
        if (string.isEmpty() || string2.isEmpty() || string3.isEmpty()) {
            SharedPreferences.Editor editorEdit2 = sharedPreferences.edit();
            editorEdit2.clear();
            editorEdit2.commit();
            return null;
        }
        byte[] bytesFromBase64String = getBytesFromBase64String(string);
        byte[] bytesFromBase64String2 = getBytesFromBase64String(string2);
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        ECPublicKey eCPublicKey = (ECPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytesFromBase64String));
        ECPrivateKey eCPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytesFromBase64String2));
        Ecdsa ecdsa = new Ecdsa();
        ecdsa.uniqueId = string3;
        ecdsa.keyPair = new KeyPair(eCPublicKey, eCPrivateKey);
        return ecdsa;
    }

    public void generateKey(String str) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        keyPairGenerator.initialize(new ECGenParameterSpec(EC_ALGORITHM_NAME));
        this.uniqueId = str;
        this.keyPair = keyPairGenerator.generateKeyPair();
    }

    public EccPubKey getPublicKey() {
        return new EccPubKey((ECPublicKey) this.keyPair.getPublic());
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public boolean storeKeyPairAndId(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("com.microsoft.xal.crypto", 0).edit();
        editorEdit.putString("id", str);
        editorEdit.putString("public", getBase64StringFromBytes(this.keyPair.getPublic().getEncoded()));
        editorEdit.putString("private", getBase64StringFromBytes(this.keyPair.getPrivate().getEncoded()));
        return editorEdit.commit();
    }

    public byte[] sign(byte[] bArr) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature signature = Signature.getInstance(ECDSA_SIGNATURE_NAME);
        signature.initSign(this.keyPair.getPrivate());
        signature.update(bArr);
        return toP1363SignedBuffer(signature.sign());
    }

    public byte[] hashAndSign(byte[] bArr) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        ShaHasher shaHasher = new ShaHasher();
        shaHasher.AddBytes(bArr);
        return sign(shaHasher.SignHash());
    }

    private byte[] toP1363SignedBuffer(byte[] bArr) {
        byte b = bArr[3];
        int i = 4 + b + 1;
        int i2 = i + 1;
        byte b2 = bArr[i];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeAdjustedHalfOfAsn1ToP1363(bArr, 4, b, byteArrayOutputStream);
        writeAdjustedHalfOfAsn1ToP1363(bArr, i2, b2, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void writeAdjustedHalfOfAsn1ToP1363(byte[] bArr, int i, int i2, ByteArrayOutputStream byteArrayOutputStream) {
        if (i2 > 32) {
            byteArrayOutputStream.write(bArr, i + (i2 - 32), 32);
        } else {
            if (i2 < 32) {
                int i3 = 32 - i2;
                byteArrayOutputStream.write(new byte[i3], 0, i3);
                byteArrayOutputStream.write(bArr, i, i2);
                return;
            }
            byteArrayOutputStream.write(bArr, i, i2);
        }
    }

    private static String getKeyAlias(String str) {
        return KEY_ALIAS_PREFIX + str;
    }

    private static String getBase64StringFromBytes(byte[] bArr) {
        return Base64.encodeToString(bArr, 0, bArr.length, 11);
    }

    private static byte[] getBytesFromBase64String(String str) throws IllegalArgumentException {
        return Base64.decode(str, 11);
    }
}
