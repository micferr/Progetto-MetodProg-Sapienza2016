package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.assertNotEquals;

public class TestAssertNotEquals {
    @Test
    void testAssertNotEquals_actuallyNotEquals() {
        assertNotEquals(null, new Object());
        assertNotEquals(new Object(), null);
        assertNotEquals(1, new Object());
        assertNotEquals(new Object(), "");
        assertNotEquals((byte)0, (byte)1);
        assertNotEquals((short)2, (short)3);
        assertNotEquals(4, 5);
        assertNotEquals((long)6, (long)7);
        assertNotEquals(0.f, 1.f, 0.1f);
        assertNotEquals(2.d, 3.d, 0.1d);
        assertNotEquals('a', 'b');
        assertNotEquals(true, false);
        assertNotEquals(false, true);
    }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_null_null() {
        assertNotEquals(null, null);
    }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_Object() { assertNotEquals("", ""); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_byte() { assertNotEquals((byte)0, (byte)0); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_short() { assertNotEquals((short)0, (short)0); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_int() { assertNotEquals(0, 0); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_long() { assertNotEquals((long)0, (long)0); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_float_noTolerance() { assertNotEquals(0.f, 0.f); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyNotEquals_float_noTolerance() { assertNotEquals(0.f, 1.f); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_float_tolerance() { assertNotEquals(0.f, 0.f, 0.1f); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_double_noTolerance() { assertNotEquals(0.d, 0.d); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyNotEquals_double_noTolerance() { assertNotEquals(0.d, 1.d); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_double_tolerance() { assertNotEquals(0.d, 0.d, 0.1d); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_boolean() { assertNotEquals(true, true); }

    @Test(expected = TestFailedError.class)
    void testAssertNotEquals_actuallyEquals_char() { assertNotEquals('a', 'a'); }
}
