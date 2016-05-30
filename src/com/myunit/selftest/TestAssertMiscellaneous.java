package com.myunit.selftest;

import com.myunit.assertion.TestFailedError;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.fail;

public class TestAssertMiscellaneous {
    @Test
    void testEmptyTest() {}

    @Test(expected = TestFailedError.class)
    void testFail() {
        fail();
    }

    @Test(expected = ArithmeticException.class)
    void testRuntimeException() {
        int x = 5/0;
    }
}
