package com.myunit.assertion;

import static com.myunit.assertion.Assert.assertTrue;

public class AssertGreaterThanOrEquals {
    public static void assertGreaterThanOrEquals(byte actual, byte expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(byte actual, byte expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(short actual, short expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(short actual, short expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(int actual, int expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(int actual, int expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(long actual, long expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(long actual, long expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(float actual, float expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(float actual, float expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(double actual, double expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(double actual, double expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }

    public static void assertGreaterThanOrEquals(char actual, char expected, String message) {
        assertTrue(actual >= expected, message);
    }

    public static void assertGreaterThanOrEquals(char actual, char expected) {
        assertGreaterThanOrEquals(actual, expected, null);
    }
}
