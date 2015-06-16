package com.orhanobut.hawk;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orhan Obut
 */
public class HawkEncoderTest extends InstrumentationTestCase {

    private static final String ENCRYPTION_KEY = "1234";

    Context context;

    Encoder encoder;

    Logger logger = new Logger(LogLevel.FULL);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getContext();

        encoder = new HawkEncoder(
                logger,
                new AesEncryption(logger, new SharedPreferencesStorage(context, "test"), "password"),
                new GsonParser(new Gson())
        );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        context = null;
    }

    public void testEncodeShouldReturnNull() {
        Object object = encoder.encode(null);
        assertNull(object);
    }

    public void testDecodeShouldReturnNull() {
        Object result;
        try {
            result = encoder.decode(null);
        } catch (Exception e) {
            result = null;
        }

        assertNull(result);
    }

    public void testConstructorShouldThrowNPE() {
        try {
            new HawkEncoder(logger, null, null);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testEncodeShouldNotReturnNull_string() {
        String result = encoder.encode("asdf");
        assertNotNull(result);
    }

    public void testEncodeShouldNotReturnNull_primitive() {
        assertNotNull(encoder.encode(0));
        assertNotNull(encoder.encode(true));
        assertNotNull(encoder.encode('c'));
    }

    public void testEncodeShouldNotReturnNull_list() {
        List<String> list = new ArrayList<>();
        list.add("asdfdsf");
        list.add("asdfdsf");
        list.add("asdfdsf");

        assertNotNull(encoder.encode(list));
    }

    public void testEncodeShouldNotReturnNull_list2() {
        List<FooParcelable> list = new ArrayList<>();
        list.add(new FooParcelable());
        list.add(new FooParcelable());
        list.add(new FooParcelable());
        list.add(new FooParcelable());

        assertNotNull(encoder.encode(list));
    }

    public void testEncodeWithKeyReturnsNullForNullValue() {
        assertNull(encoder.encode(ENCRYPTION_KEY, null));
    }

    public void testEncodeWithKeyReturnsNullForNullKey() {
        assertNull(encoder.encode(null, "test"));
    }

    public void testEncodeDecodeWithKey() throws Exception {
        final String value = "test";
        final String encodedValue = encoder.encode(ENCRYPTION_KEY, value);
        assertNotNull(encodedValue);
        assertEquals(value, encoder.decode(ENCRYPTION_KEY, DataUtil.addTypeAsObject(encodedValue, value.getClass())));
    }
}
