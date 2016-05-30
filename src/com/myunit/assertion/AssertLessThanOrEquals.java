package com.myunit.assertion;

import static com.myunit.assertion.Assert.assertTrue;

public class AssertLessThanOrEquals {
    public static void assertLessThanOrEquals(byte actual, byte expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(byte actual, byte expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(short actual, short expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(short actual, short expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(int actual, int expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(int actual, int expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(long actual, long expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(long actual, long expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(float actual, float expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(float actual, float expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(double actual, double expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(double actual, double expected) {
        assertLessThanOrEquals(actual, expected, null);
    }

    public static void assertLessThanOrEquals(char actual, char expected, String message) {
        assertTrue(actual <= expected, message);
    }

    public static void assertLessThanOrEquals(char actual, char expected) {
        assertLessThanOrEquals(actual, expected, null);
    }
}
