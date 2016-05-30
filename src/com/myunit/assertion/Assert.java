package com.myunit.assertion;

public class Assert {

    public static void fail() {
        throw new TestFailedError();
    }

    public static void fail(String message) {
        throw new TestFailedError(message);
    }

    // Assert True / False

    public static void assertTrue(boolean expression, String message) {
        AssertTrueFalse.assertTrue(expression, message);
    }

    public static void assertTrue(boolean expression) {
        AssertTrueFalse.assertTrue(expression);
    }

    public static void assertFalse(boolean expression, String message) {
        AssertTrueFalse.assertFalse(expression, message);
    }

    public static void assertFalse(boolean expression) {
        AssertTrueFalse.assertFalse(expression);
    }

    // Assert Equals / NotEquals

    public static void assertEquals(Object actual, Object expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(Object actual, Object expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(byte actual, byte expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(byte actual, byte expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(short actual, short expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(short actual, short expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(int actual, int expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(int actual, int expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(long actual, long expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(long actual, long expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(float actual, float expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(float actual, float expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(float actual, float expected, float tolerance, String message) {
        AssertEquals.assertEquals(actual, expected, tolerance, message);
    }

    public static void assertEquals(float actual, float expected, float tolerance) {
        AssertEquals.assertEquals(actual, expected, tolerance);
    }

    public static void assertEquals(double actual, double expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(double actual, double expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertEquals(double actual, double expected, double tolerance, String message) {
        AssertEquals.assertEquals(actual, expected, tolerance, message);
    }

    public static void assertEquals(double actual, double expected, double tolerance) {
        AssertEquals.assertEquals(actual, expected, tolerance);
    }

    public static void assertEquals(char actual, char expected, String message) {
        AssertEquals.assertEquals(actual, expected, message);
    }

    public static void assertEquals(char actual, char expected) {
        AssertEquals.assertEquals(actual, expected);
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(Object actual, Object expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(byte actual, byte expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(byte actual, byte expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(short actual, short expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(short actual, short expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(int actual, int expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(int actual, int expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(long actual, long expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(long actual, long expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(float actual, float expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(float actual, float expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(float actual, float expected, float tolerance, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, tolerance, message);
    }

    public static void assertNotEquals(float actual, float expected, float tolerance) {
        AssertNotEquals.assertNotEquals(actual, expected, tolerance);
    }

    public static void assertNotEquals(double actual, double expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(double actual, double expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    public static void assertNotEquals(double actual, double expected, double tolerance, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, tolerance, message);
    }

    public static void assertNotEquals(double actual, double expected, double tolerance) {
        AssertNotEquals.assertNotEquals(actual, expected, tolerance);
    }

    public static void assertNotEquals(char actual, char expected, String message) {
        AssertNotEquals.assertNotEquals(actual, expected, message);
    }

    public static void assertNotEquals(char actual, char expected) {
        AssertNotEquals.assertNotEquals(actual, expected);
    }

    // Assert Greater Than

    public static void assertGreaterThan(byte actual, byte expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(byte actual, byte expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(short actual, short expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(short actual, short expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(int actual, int expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(int actual, int expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(long actual, long expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(long actual, long expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(float actual, float expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(float actual, float expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(double actual, double expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(double actual, double expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    public static void assertGreaterThan(char actual, char expected, String message) {
        AssertGreaterThan.assertGreaterThan(actual, expected, message);
    }

    public static void assertGreaterThan(char actual, char expected) {
        AssertGreaterThan.assertGreaterThan(actual, expected);
    }

    // Assert Less Than

    public static void assertLessThan(byte actual, byte expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(byte actual, byte expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(short actual, short expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(short actual, short expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(int actual, int expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(int actual, int expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(long actual, long expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(long actual, long expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(float actual, float expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(float actual, float expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(double actual, double expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(double actual, double expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    public static void assertLessThan(char actual, char expected, String message) {
        AssertLessThan.assertLessThan(actual, expected, message);
    }

    public static void assertLessThan(char actual, char expected) {
         AssertLessThan.assertLessThan(actual, expected);
    }

    // Assert Greater Than Or Equals

    public static void assertGreaterThanOrEquals(byte actual, byte expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(byte actual, byte expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(short actual, short expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(short actual, short expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(int actual, int expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(int actual, int expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(long actual, long expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(long actual, long expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(float actual, float expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(float actual, float expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(double actual, double expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(double actual, double expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    public static void assertGreaterThanOrEquals(char actual, char expected, String message) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected, message);
    }

    public static void assertGreaterThanOrEquals(char actual, char expected) {
        AssertGreaterThanOrEquals.assertGreaterThanOrEquals(actual, expected);
    }

    // Assert Less Than Or Equals

    public static void assertLessThanOrEquals(byte actual, byte expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(byte actual, byte expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(short actual, short expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(short actual, short expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(int actual, int expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(int actual, int expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(long actual, long expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(long actual, long expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(float actual, float expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(float actual, float expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(double actual, double expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(double actual, double expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }

    public static void assertLessThanOrEquals(char actual, char expected, String message) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected, message);
    }

    public static void assertLessThanOrEquals(char actual, char expected) {
        AssertLessThanOrEquals.assertLessThanOrEquals(actual, expected);
    }
    
    // Assert Null / Not Null

    public static void assertNull(Object o, String message) {
        AssertNullNotNull.assertNull(o, message);
    }

    public static void assertNull(Object o) {
        AssertNullNotNull.assertNull(o);
    }

    public static void assertNotNull(Object o, String message) {
        AssertNullNotNull.assertNotNull(o, message);
    }

    public static void assertNotNull(Object o) {
        AssertNullNotNull.assertNotNull(o);
    }
}
