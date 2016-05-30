package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.assertNull;
import static com.myunit.assertion.Assert.assertNotNull;

public class TestNullNotNull {
    @Test
    void testAssertNull_actuallyNull() {
        assertNull(null);
    }

    @Test(expected = TestFailedError.class)
    void testAssertNull_actuallyNotNull() {
        assertNull(new Object());
    }

    @Test
    void testAssertNotNull_actuallyNotNull() {
        assertNotNull(new Object());
    }

    @Test(expected = TestFailedError.class)
    void testAssertNotNull_actuallyNull() {
        assertNotNull(null);
    }
}
