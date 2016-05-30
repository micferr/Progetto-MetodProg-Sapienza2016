package com.myunit.assertion;

import java.util.Objects;

import static com.myunit.assertion.Assert.fail;
import static com.myunit.assertion.Assert.assertFalse;

public class AssertNotEquals {
    public static void assertNotEquals(Object actual, Object expected, String message) {
        assertFalse(Objects.equals(actual, expected), message);
    }

    public static void assertNotEquals(Object actual, Object expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(byte actual, byte expected, String message) {
        assertFalse(actual == expected, message);
    }

    public static void assertNotEquals(byte actual, byte expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(short actual, short expected, String message) {
        assertFalse(actual == expected, message);
    }

    public static void assertNotEquals(short actual, short expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(int actual, int expected, String message) {
        assertFalse(actual == expected, message);
    }

    public static void assertNotEquals(int actual, int expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(long actual, long expected, String message) {
        assertFalse(actual == expected, message);
    }

    public static void assertNotEquals(long actual, long expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(float actual, float expected, String message) {
        fail("Float equality not a good test. Use assertNotEquals(float,float,float[,String]) instead");
    }

    public static void assertNotEquals(float actual, float expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(float actual, float expected, float tolerance, String message) {
        assertFalse(Math.abs(actual - expected) <= tolerance, message);
    }

    public static void assertNotEquals(float actual, float expected, float tolerance) {
        assertNotEquals(actual, expected, tolerance, null);
    }

    public static void assertNotEquals(double actual, double expected, String message) {
        fail("Double equality not a good test. Use assertNotEquals(double,double,double[,String]) instead");
    }

    public static void assertNotEquals(double actual, double expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertNotEquals(double actual, double expected, double tolerance, String message) {
        assertFalse(Math.abs(actual - expected) <= tolerance, message);
    }

    public static void assertNotEquals(double actual, double expected, double tolerance) {
        assertNotEquals(actual, expected, tolerance, null);
    }

    public static void assertNotEquals(char actual, char expected, String message) {
        assertFalse(actual == expected, message);
    }

    public static void assertNotEquals(char actual, char expected) {
        assertNotEquals(actual, expected, null);
    }
}
