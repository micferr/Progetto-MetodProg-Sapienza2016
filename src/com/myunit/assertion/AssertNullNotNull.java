package com.myunit.assertion;

import static com.myunit.assertion.Assert.assertTrue;

public class AssertNullNotNull {
    public static void assertNull(Object o, String message) {
        assertTrue(o == null, message);
    }

    public static void assertNull(Object o) {
        assertNull(o, null);
    }

    public static void assertNotNull(Object o, String message) {
        assertTrue(o != null, message);
    }

    public static void assertNotNull(Object o) {
        assertNotNull(o, null);
    }
}
