package com.myunit.assertion;

public class TestFailedError extends Error {
    public TestFailedError() {
        super();
    }

    public TestFailedError(String message) {
        super(message);
    }
}
