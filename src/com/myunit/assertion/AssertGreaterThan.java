package com.myunit.assertion;

import static com.myunit.assertion.Assert.assertTrue;

public class AssertGreaterThan {
    public static void assertGreaterThan(byte actual, byte expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(byte actual, byte expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(short actual, short expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(short actual, short expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(int actual, int expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(int actual, int expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(long actual, long expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(long actual, long expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(float actual, float expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(float actual, float expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(double actual, double expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(double actual, double expected) {
        assertGreaterThan(actual, expected, null);
    }

    public static void assertGreaterThan(char actual, char expected, String message) {
        assertTrue(actual > expected, message);
    }

    public static void assertGreaterThan(char actual, char expected) {
        assertGreaterThan(actual, expected, null);
    }
}
