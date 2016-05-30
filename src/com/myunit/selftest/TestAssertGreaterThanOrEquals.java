package com.myunit.selftest;

import static com.myunit.assertion.Assert.assertGreaterThanOrEquals;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

public class TestAssertGreaterThanOrEquals {
    @Test
    void testAssertGreaterThanOrEquals_actuallyGreaterThan() {
        assertGreaterThanOrEquals((byte)1, (byte)0);
        assertGreaterThanOrEquals((short)1, (short)0);
        assertGreaterThanOrEquals(1, 0);
        assertGreaterThanOrEquals((long)1, (long)0);
        assertGreaterThanOrEquals(1.f, 0.f);
        assertGreaterThanOrEquals(1.d, 0.d);
        assertGreaterThanOrEquals('b', 'a');
    }

    @Test
    void testAssertGreaterThanOrEquals_actuallyEquals() {
        assertGreaterThanOrEquals((byte)0, (byte)0);
        assertGreaterThanOrEquals((short)0, (short)0);
        assertGreaterThanOrEquals(0, 0);
        assertGreaterThanOrEquals((long)0, (long)0);
        assertGreaterThanOrEquals(0.f, 0.f);
        assertGreaterThanOrEquals(0.d, 0.d);
        assertGreaterThanOrEquals('a', 'a');
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_byte() {
        assertGreaterThanOrEquals((byte)0, (byte)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_short() {
        assertGreaterThanOrEquals((short)0, (short)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_int() {
        assertGreaterThanOrEquals(0, 1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_long() {
        assertGreaterThanOrEquals((long)0, (long)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_float() {
        assertGreaterThanOrEquals(0.f, 1.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_double() {
        assertGreaterThanOrEquals(0.d, 1.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThanOrEquals_actuallyLess_char() {
        assertGreaterThanOrEquals('a', 'b');
    }
}
