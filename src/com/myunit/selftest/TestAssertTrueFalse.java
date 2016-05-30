package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

public class TestAssertTrueFalse {
    @Test
    void testAssertTrue_actuallyTrue() {
        assertTrue(true);
    }

    @Test(expected = TestFailedError.class)
    void testAssertTrue_actuallyFalse() {
        assertTrue(false);
    }

    @Test
    void testAssertFalse_actuallyFalse() {
        assertFalse(false);
    }

    @Test(expected = TestFailedError.class)
    void testAssertFalse_actuallyTrue() {
        assertFalse(true);
    }
}
