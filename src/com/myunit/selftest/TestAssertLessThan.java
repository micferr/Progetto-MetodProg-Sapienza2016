package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.assertLessThan;

public class TestAssertLessThan {
    @Test
    void testAssertLessThan_actuallyLessThan() {
        assertLessThan((byte) 0, (byte) 1);
        assertLessThan((short) 2, (short) 3);
        assertLessThan(4, 5);
        assertLessThan((long) 6, (long) 7);
        assertLessThan(0.f, 1.f);
        assertLessThan(2.d, 3.d);
        assertLessThan('a', 'b');
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_byte() {
        assertLessThan((byte)1, (byte)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_byte() {
        assertLessThan((byte)0, (byte)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_short() {
        assertLessThan((short)1, (short)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_short() {
        assertLessThan((short)0, (short)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_int() {
        assertLessThan(1, 0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_int() {
        assertLessThan(0, 0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_long() {
        assertLessThan((long)1, (long)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_long() {
        assertLessThan((long)0, (long)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_float() {
        assertLessThan(1.f, 0.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_float() {
        assertLessThan(0.f, 0.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_double() {
        assertLessThan(1.d, 0.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_double() {
        assertLessThan(0.d, 0.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyGreaterThan_char() {
        assertLessThan('b', 'a');
    }

    @Test(expected = TestFailedError.class)
    void testAssertLessThan_actuallyEqual_char() {
        assertLessThan('a', 'a');
    }
}
