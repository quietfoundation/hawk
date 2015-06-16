package com.orhanobut.hawk;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Orhan Obut
 */
public class HawkTest extends InstrumentationTestCase {

    private static final String ENCRYPTION_KEY = "1234";

    Context context;

    Hawk hawk;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getContext();
        hawk = new Hawk.Builder(context, "testPassword").build();
        hawk.clear();
        hawk.resetCrypto();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        context = null;
    }

    public void testBoolean() {
        hawk.put("tag", true);
        assertEquals(true, hawk.get("tag"));
    }

    public void testBooleanDefault() {
        assertEquals(Boolean.FALSE, hawk.get("tag", false));
    }

    public void testBooleanNotDefault() {
        hawk.put("tag", true);
        assertNotSame(true, hawk.get("tag", false));
    }

    public void testChar() {
        char expected = 'a';
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testCharDefault() {
        char expected = 'a';
        assertEquals(Character.valueOf(expected), hawk.get("tag", expected));
    }

    public void testCharNotDefault() {
        char expected = 'a';
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 'b'));
    }

    public void testByte() {
        byte expected = 0;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testByteDefault() {
        byte expected = 0;
        assertEquals(Byte.valueOf(expected), hawk.get("tag", expected));
    }

    public void testByteNotDefault() {
        byte expected = 0;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 1));
    }

    public void testShort() {
        short expected = 0;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testShortDefault() {
        short expected = 0;
        assertEquals(Short.valueOf(expected), hawk.get("tag", expected));
    }

    public void testShortNotDefault() {
        short expected = 0;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 1));
    }

    public void testInt() {
        int expected = 0;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testIntDefault() {
        int expected = 0;
        assertEquals(Integer.valueOf(expected), hawk.get("tag", expected));
    }

    public void testIntNotDefault() {
        int expected = 0;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 1));
    }

    public void testLong() {
        long expected = 100L;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testLongDefault() {
        long expected = 100L;
        assertEquals(Long.valueOf(expected), hawk.get("tag", expected));
    }

    public void testLongNotDefault() {
        long expected = 100L;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 99L));
    }

    public void testFloat() {
        float expected = 0.1f;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testFloatDefault() {
        float expected = 0.1f;
        assertEquals(expected, hawk.get("tag", expected));
    }

    public void testFloatNotDefault() {
        float expected = 0.1f;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 0.9f));
    }

    public void testDouble() {
        double expected = 11;
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testDoubleDefault() {
        double expected = 11;
        assertEquals(expected, hawk.get("tag", expected));
    }

    public void testDoubleNotDefault() {
        double expected = 11;
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", 99));
    }

    public void testString() {
        String expected = "test";
        hawk.put("tag", expected);
        assertEquals(expected, hawk.get("tag"));
    }

    public void testStringDefault() {
        String expected = "test";
        assertEquals(expected, hawk.get("tag", expected));
    }

    public void testStringNotDefault() {
        String expected = "test";
        hawk.put("tag", expected);
        assertNotSame(expected, hawk.get("tag", "default"));
    }

    public void testSerializableObject() {
        FooSerializable foo = new FooSerializable();

        hawk.put("tag", foo);
        FooSerializable foo1 = hawk.get("tag");

        assertNotNull(foo1);
    }

    public void testSerializableObjectDefault() {
        FooSerializable foo = new FooSerializable();

        FooSerializable foo1 = hawk.get("tag", foo);

        assertNotNull(foo1);
    }

    public void testNotSerializableObject() {
        FooNotSerializable foo = new FooNotSerializable();

        hawk.put("tag", foo);
        FooNotSerializable foo1 = hawk.get("tag");

        assertNotNull(foo1);
    }

    public void testNotSerializableObjectDefault() {
        FooNotSerializable foo = new FooNotSerializable();

        FooNotSerializable foo1 = hawk.get("tag", foo);

        assertNotNull(foo1);
    }

    public void testParcelableObject() {
        FooParcelable foo = new FooParcelable();
        hawk.put("tag", foo);
        FooParcelable fooParcelable = hawk.get("tag");

        assertNotNull(fooParcelable);
    }

    public void testParcelableObjectDefault() {
        FooParcelable foo = new FooParcelable();

        FooParcelable fooParcelable = hawk.get("tag", foo);

        assertNotNull(fooParcelable);
    }

    public void testListSerializable() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("foo");

        hawk.put("tag", list);

        List<String> list1 = hawk.get("tag");

        assertNotNull(list1);
    }

    public void testListSerializableDefault() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("foo");

        List<String> list1 = hawk.get("tag", list);

        assertNotNull(list1);
    }

    public void testListParcelable() {
        List<FooParcelable> list = new ArrayList<>();
        list.add(new FooParcelable());
        list.add(new FooParcelable());

        hawk.put("tag", list);

        List<FooParcelable> list1 = hawk.get("tag");

        assertNotNull(list1);
    }

    public void testListParcelableDefault() {
        List<FooParcelable> list = new ArrayList<>();
        list.add(new FooParcelable());
        list.add(new FooParcelable());

        List<FooParcelable> list1 = hawk.get("tag", list);

        assertNotNull(list1);
    }

    public void testEmptyList() {
        try {
            List<FooParcelable> list = new ArrayList<>();
            hawk.put("tag", list);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNullKeyPut() {
        try {
            hawk.put(null, "test");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNullKeyGet() {
        try {
            hawk.get(null);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNullKeyPutList() {
        try {
            hawk.put(null, new ArrayList<String>());
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNullValuePut() {
        try {
            hawk.put("tag", null);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testCount() {
        String value = "test";
        hawk.put("tag", value);
        hawk.put("tag1", value);
        hawk.put("tag2", value);
        hawk.put("tag3", value);
        hawk.put("tag4", value);

        int expected = 5;
        assertEquals(expected, hawk.count());
    }

    public void testClear() {
        String value = "test";
        hawk.put("tag", value);
        hawk.put("tag1", value);
        hawk.put("tag2", value);

        hawk.clear();
        int expected = 0;

        assertEquals(expected, hawk.count());
    }

    public void testRemove() {
        String value = "test";
        hawk.put("tag", value);
        hawk.put("tag1", value);
        hawk.put("tag2", value);

        hawk.remove("tag");

        String result = hawk.get("tag");

        assertNull(result);
        assertEquals(2, hawk.count());
    }

    public void testBulkRemoval() {
        hawk.put("tag", "test");
        hawk.put("tag1", 1);
        hawk.put("tag2", Boolean.FALSE);

        hawk.remove("tag", "tag1");

        String result = hawk.get("tag");

        assertNull(result);
        assertEquals(1, hawk.count());
    }

    public void testContains() {
        String value = "test";
        String key = "tag";
        hawk.put(key, value);

        assertTrue(hawk.contains(key));

        hawk.remove(key);

        assertFalse(hawk.contains(key));
    }

    public void testChain() {
        hawk.chain()
                .put("tag", 1)
                .put("tag1", "yes")
                .put("tag2", Boolean.FALSE)
                .commit();

        assertEquals(1, hawk.get("tag"));
        assertEquals("yes", hawk.get("tag1"));
        assertEquals(false, hawk.get("tag2"));
    }

    public void testChainWithLists() {
        List<String> items = new ArrayList<>();
        items.add("fst");
        items.add("snd");
        items.add("trd");

        hawk.chain()
                .put("tag", 1)
                .put("tag1", "yes")
                .put("tag2", Boolean.FALSE)
                .put("lst", items)
                .commit();

        assertEquals(1, hawk.get("tag"));
        assertEquals("yes", hawk.get("tag1"));
        assertEquals(false, hawk.get("tag2"));

        List<String> stored = hawk.get("lst");
        assertNotNull(stored);
        assertFalse(stored.isEmpty());

        for (int i = 0, s = stored.size(); i < s; i++) {
            assertEquals(items.get(i), stored.get(i));
        }
    }

    public void testStringWithDifferentEncryptionKey() {
        final String expected = "test";
        hawk.putWithEncryptionKey(ENCRYPTION_KEY, "tag", expected);
        assertEquals(expected, hawk.getWithEncryptionKey(ENCRYPTION_KEY, "tag"));
    }
}
