package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.assertEquals;

public class TestAssertEquals {
    @Test
    void testAssertEquals_actuallyEquals() {
        assertEquals(null, null);
        assertEquals("foo", "foo");
        assertEquals((byte)1, (byte)1);
        assertEquals((short)2, (short)2);
        assertEquals(3, 3);
        assertEquals((long)4, (long)4);
        assertEquals(1.0f, 1.0f, 0.1f);
        assertEquals(2.0d, 2.0d, 0.1d);
        assertEquals('a', 'a');
        assertEquals(true, true);
        assertEquals(false, false);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_Object() {
        assertEquals(new Object(), new Object());
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_null_Object () {
        assertEquals(null, new Object());
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_Object_null() {
        assertEquals(new Object(), null);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_byte() { assertEquals((byte)0, (byte)1); }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_short() {
        assertEquals((short)0, (short)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_int() {
        assertEquals(0, 1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_long() {
        assertEquals((long)0, (long)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_float_noTolerance() {
        assertEquals(0.f, 1.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_float_tolerance() { assertEquals(0.f, 1.f, 0.1f); }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_double_noTolerance() {
        assertEquals(0.d, 1.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_double_tolerance() { assertEquals(0.d, 1.d, 0.1d); }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_boolean() {
        assertEquals(true, false);
    }

    @Test(expected = TestFailedError.class)
    void testAssertEquals_actuallyNotEquals_char() {
        assertEquals('a', 'b');
    }
}
