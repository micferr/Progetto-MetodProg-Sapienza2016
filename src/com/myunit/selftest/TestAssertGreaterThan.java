package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.assertGreaterThan;

public class TestAssertGreaterThan {
    @Test
    void testAssertGreaterThan_actuallyGreaterThan() {
        assertGreaterThan((byte) 1, (byte) 0);
        assertGreaterThan((short) 3, (short) 1);
        assertGreaterThan(5, 4);
        assertGreaterThan((long) 7, (long) 6);
        assertGreaterThan(1.f, 0.f);
        assertGreaterThan(3.d, 2.d);
        assertGreaterThan('b', 'a');
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_byte() {
        assertGreaterThan((byte)0, (byte)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_byte() {
        assertGreaterThan((byte)0, (byte)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_short() {
        assertGreaterThan((short)0, (short)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_short() {
        assertGreaterThan((short)0, (short)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_int() {
        assertGreaterThan(0, 1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_int() {
        assertGreaterThan(0, 0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_long() {
        assertGreaterThan((long)0, (long)1);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_long() {
        assertGreaterThan((long)0, (long)0);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_float() {
        assertGreaterThan(0.f, 1.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_float() {
        assertGreaterThan(0.f, 0.f);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_double() {
        assertGreaterThan(0.d, 1.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_double() {
        assertGreaterThan(0.d, 0.d);
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyLessThan_char() {
        assertGreaterThan('a', 'b');
    }

    @Test(expected = TestFailedError.class)
    void testAssertGreaterThan_actuallyEqual_char() {
        assertGreaterThan('a', 'a');
    }
}
