package com.myunit.selftest;

import static com.myunit.assertion.Assert.assertLessThanOrEquals;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

public class TestAssertLessThanOrEquals {
    @Test
    void testAssertLessThanOrEquals_actuallyLessThan() {
        assertLessThanOrEquals((byte)0, (byte)1);
        assertLessThanOrEquals((short)0, (short)1);
        assertLessThanOrEquals(0, 1);
        assertLessThanOrEquals((long)0, (long)1);
        assertLessThanOrEquals(0.f, 1.f);
        assertLessThanOrEquals(0.d, 1.d);
        assertLessThanOrEquals('a', 'b');
    }

    @Test
    void testAssertLessThanOrEquals_actuallyEquals() {
        assertLessThanOrEquals((byte)0, (byte)0);
        assertLessThanOrEquals((short)0, (short)0);
        assertLessThanOrEquals(0, 0);
        assertLessThanOrEquals((long)0, (long)0);
        assertLessThanOrEquals(0.f, 0.f);
        assertLessThanOrEquals(0.d, 0.d);
        assertLessThanOrEquals('a', 'a');
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_byte() {
        assertLessThanOrEquals((byte)1, (byte)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_short() {
        assertLessThanOrEquals((short)1, (short)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_int() {
        assertLessThanOrEquals(1, 0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_long() {
        assertLessThanOrEquals((long)1, (long)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_float() {
        assertLessThanOrEquals(1.f, 0.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_double() {
        assertLessThanOrEquals(1.d, 0.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThanOrEquals_actuallyGreater_char() {
        assertLessThanOrEquals('b', 'a');
    }
}
