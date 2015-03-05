package com.orhanobut.hawk;

import android.content.Context;
import android.test.InstrumentationTestCase;

/**
 * @author Orhan Obut
 */
public class DataUtilTest extends InstrumentationTestCase {

    Context context;
    Logger logger = new Logger(LogLevel.FULL);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty(
                "dexmaker.dexcache",
                getInstrumentation().getTargetContext().getCacheDir().getPath());
        context = getInstrumentation().getContext();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        context = null;
    }

    public void testGetDataInfoShouldReturnNull_null() {
        try {
            DataUtil.getDataInfo(logger, null);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testGetDataInfoShouldThrowException_NotContainDelimiter() {
        try {
            DataUtil.getDataInfo(logger, "324234");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testGetDataInfoShouldReturnNotNull() {
        DataInfo info = DataUtil.getDataInfo(logger, "String11@asdfjasdf");
        assertNotNull(info);
    }

    public void testGetDataInfoShouldBeSerializable() {
        DataInfo info = DataUtil.getDataInfo(logger, "String11@asdfjasdf");
        assertTrue(info.isSerializable());
    }

    public void testGetDataInfoShouldNotBeSerializable() {
        DataInfo info = DataUtil.getDataInfo(logger, "String10@asdfjasdf");
        assertFalse(info.isSerializable());
    }

    public void testGetDataInfoShouldBeList() {
        DataInfo info = DataUtil.getDataInfo(logger, "String11@asdfjasdf");
        assertTrue(info.isSerializable());
    }

    public void testGetDataInfoShouldNotBeList() {
        DataInfo info = DataUtil.getDataInfo(logger, "String00@asdfjasdf");
        assertFalse(info.isSerializable());
    }


}
