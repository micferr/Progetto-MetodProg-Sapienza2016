package com.myunit.assertion;

import static com.myunit.assertion.Assert.assertTrue;

public class AssertLessThan {
    public static void assertLessThan(byte actual, byte expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(byte actual, byte expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(short actual, short expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(short actual, short expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(int actual, int expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(int actual, int expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(long actual, long expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(long actual, long expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(float actual, float expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(float actual, float expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(double actual, double expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(double actual, double expected) {
        assertLessThan(actual, expected, null);
    }

    public static void assertLessThan(char actual, char expected, String message) {
        assertTrue(actual < expected, message);
    }

    public static void assertLessThan(char actual, char expected) {
        assertLessThan(actual, expected, null);
    }
}
