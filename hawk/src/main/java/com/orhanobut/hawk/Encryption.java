package com.orhanobut.hawk;

/**
 * @author Orhan Obut
 */
interface Encryption {

    /**
     * Encrypt the given string with the key and returns cipher text
     *
     * @param key used to encrypt value
     * @param value is the plain text
     * @return cipher text as string
     */
    String encrypt(String key, byte[] value);

    /**
     * Encrypt the given string and returns cipher text
     *
     * @param value is the plain text
     * @return cipher text as string
     */
    String encrypt(byte[] value);

    /**
     * Decrypt the given cipher text and return plain text
     *
     * @param value is the cipher text
     * @return plain text
     */
    byte[] decrypt(String key, String value);

    /**
     * Decrypt the given cipher text and return plain text
     *
     * @param value is the cipher text
     * @return plain text
     */
    byte[] decrypt(String value);

    /**
     * Clear everything from the storage
     */
    boolean reset();

}
