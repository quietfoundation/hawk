package com.orhanobut.hawk;

import android.text.TextUtils;

import java.security.GeneralSecurityException;


/**
 * Provides AES algorithm
 *
 * @author Orhan Obut
 */
final class AesEncryption implements Encryption {

    /**
     * Key used to look up stored generated salt values, not the actual salt
     * Never ever change this value since it will break backward compatibility in terms of keeping previous data
     */
    private static final String KEY_STORAGE_SALT = "asdf3242klj";

    private AesCbcWithIntegrity.SecretKeys secretKeys;
    private String salt;
    private Storage storage;

    /**
     * Create an AesEncryption with a randomly generated salt value
     * @param storage Storage
     * @param password Encryption password
     */
    AesEncryption(Storage storage, String password) {
        this(storage, password, null);
    }

    /**
     * Create an AesEncryption with a custom salt value
     * @param storage Storage
     * @param password Encryption password
     * @param salt Custom salt value (pass {@code null} to generate a random one)
     */
    AesEncryption(Storage storage, String password, String salt) {
        if (TextUtils.isEmpty(salt)) {
            this.salt = storage.get(KEY_STORAGE_SALT);
        } else {
            this.salt = salt;
        }
        this.storage = storage;
        generateSecretKey(password);
    }

    @Override
    public String encrypt(byte[] value) {
        if (value == null) {
            return null;
        }
        String result = null;
        try {
            AesCbcWithIntegrity.CipherTextIvMac civ = AesCbcWithIntegrity.encrypt(value, secretKeys);
            result = civ.toString();
        } catch (GeneralSecurityException e) {
            Logger.d(e.getMessage());
        }

        return result;
    }

    @Override
    public byte[] decrypt(String value) {
        if (value == null) {
            return null;
        }
        byte[] result = null;

        try {
            AesCbcWithIntegrity.CipherTextIvMac civ = getCipherTextIvMac(value);
            result = AesCbcWithIntegrity.decrypt(civ, secretKeys);
        } catch (GeneralSecurityException e) {
            Logger.d(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean reset() {
        return storage.clear();
    }

    private AesCbcWithIntegrity.CipherTextIvMac getCipherTextIvMac(String cipherText) {
        return new AesCbcWithIntegrity.CipherTextIvMac(cipherText);
    }

    /**
     * Gets the secret keys by using salt and password.
     * If not provided, a salt is generated and stored in the storage
     */
    private void generateSecretKey(String password) {
        try {
            // No salt provided, generate and store a random one
            if (TextUtils.isEmpty(salt)) {
                salt = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt());
                storage.put(KEY_STORAGE_SALT, salt);
            }

            secretKeys = AesCbcWithIntegrity.generateKeyFromPassword(password, salt);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSalt() {
        return salt;
    }

    public AesCbcWithIntegrity.SecretKeys getSecretKeys() {
        return secretKeys;
    }
}