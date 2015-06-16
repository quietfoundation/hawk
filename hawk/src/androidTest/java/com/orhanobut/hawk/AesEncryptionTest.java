package com.orhanobut.hawk;

import android.test.InstrumentationTestCase;

/**
 * Created by bosgood on 2/27/15.
 */
public class AesEncryptionTest extends InstrumentationTestCase {

    private static final String PASSWORD = "hawkFTW";

    private static final String SALT = "NaCl";

    private Storage fooStorage;

    private static final Logger LOGGER = new Logger(LogLevel.FULL);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fooStorage = new FooStorage();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        fooStorage.clear();
        fooStorage = null;
    }

    public void testFoo() {
        assertTrue(true);
    }

    public void testSaltGenerationAndStorage() {
        final AesEncryption encryption = new AesEncryption(LOGGER, fooStorage, PASSWORD);
        // Verify salt was created and stored
        assertEquals(fooStorage.count(), 1);
    }

    public void testSaltNoGenerationWhenProvided() {
        final AesEncryption encryption = new AesEncryption(LOGGER, fooStorage, PASSWORD, SALT);
        // Verify salt was not created and stored
        assertEquals(fooStorage.count(), 0);
    }

    public void testEncryptDecryptWithSpecifiedKey() {
        final String encryptionKey = "1234";
        final String value = "test";
        final AesEncryption encryption = new AesEncryption(LOGGER, fooStorage, PASSWORD, SALT);
        final String encryptedString = encryption.encrypt(encryptionKey, value.getBytes());
        assertNotNull(encryptedString);
        assertEquals(value, new String((encryption.decrypt(encryptionKey, encryptedString))));
    }
}
