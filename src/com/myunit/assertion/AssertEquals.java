package com.myunit.assertion;

import java.util.Objects;

import static com.myunit.assertion.Assert.assertTrue;
import static com.myunit.assertion.Assert.fail;

public class AssertEquals {
    public static void assertEquals(Object actual, Object expected, String message) {
        assertTrue(Objects.equals(actual, expected), message);
    }

    public static void assertEquals(Object actual, Object expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(byte actual, byte expected, String message) {
        assertTrue(actual == expected, message);
    }

    public static void assertEquals(byte actual, byte expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(short actual, short expected, String message) {
        assertTrue(actual == expected, message);
    }

    public static void assertEquals(short actual, short expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(int actual, int expected, String message) {
        assertTrue(actual == expected, message);
    }

    public static void assertEquals(int actual, int expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(long actual, long expected, String message) {
        assertTrue(actual == expected, message);
    }

    public static void assertEquals(long actual, long expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(float actual, float expected, String message) {
        fail("Float equality not a good test. Use assertEquals(float,float,float[,String]) instead");
    }

    public static void assertEquals(float actual, float expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(float actual, float expected, float tolerance, String message) {
        assertTrue(Math.abs(actual - expected) <= tolerance, message);
    }

    public static void assertEquals(float actual, float expected, float tolerance) {
        assertEquals(actual, expected, tolerance, null);
    }

    public static void assertEquals(double actual, double expected, String message) {
        fail("Double equality not a good test. Use assertEquals(double,double,double[,String]) instead");
    }

    public static void assertEquals(double actual, double expected) {
        assertEquals(actual, expected, null);
    }

    public static void assertEquals(double actual, double expected, double tolerance, String message) {
        assertTrue(Math.abs(actual - expected) <= tolerance, message);
    }

    public static void assertEquals(double actual, double expected, double tolerance) {
        assertEquals(actual, expected, tolerance, null);
    }

    public static void assertEquals(char actual, char expected, String message) {
        assertTrue(actual == expected, message);
    }

    public static void assertEquals(char actual, char expected) {
        assertEquals(actual, expected, null);
    }
}
