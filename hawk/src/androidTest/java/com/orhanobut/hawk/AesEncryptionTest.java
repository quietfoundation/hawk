package com.orhanobut.hawk;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

/**
 * Created by bosgood on 2/27/15.
 */
public class AesEncryptionTest extends InstrumentationTestCase {
    private static final String PASSWORD = "hawkFTW";
    Storage fooStorage;
    Logger logger = new Logger(LogLevel.FULL);

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
        AesEncryption encryption = new AesEncryption(logger, fooStorage, PASSWORD);
        // Verify salt was created and stored
        assertEquals(fooStorage.count(), 1);
    }

    public void testSaltNoGenerationWhenProvided() {
        AesEncryption encryption = new AesEncryption(logger, fooStorage, PASSWORD, "NaCl");
        // Verify salt was not created and stored
        assertEquals(fooStorage.count(), 0);
    }
}
