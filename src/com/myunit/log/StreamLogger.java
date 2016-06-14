package com.myunit.log;

import java.io.PrintStream;
import java.lang.reflect.Method;

public class StreamLogger extends LoggerAdapter implements Logger {
    private PrintStream stream;

    public StreamLogger(PrintStream stream) {
        if (stream == null) {
            throw new NullPointerException("Provided PrintStream must not be null.");
        }
        this.stream = stream;
    }

    @Override
    public void log(String s) {
        stream.println(s);
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        String message = testClass.getName() + "#" + method.getName() + " raised an exception:\n" +
                "Exception type: " + errorCause.getClass().getSimpleName() + "\n" +
                "Exception message: " + errorCause.getMessage() + "\n";
        stream.print(message);
    }

    @Override
    public void logExecutingMethod(Method method) {
        stream.print("Executing " + method.getName() + "... ");
    }

    @Override
    public void logTestCaseSuccess() {
        stream.println("Success");
    }

    @Override
    public void logTestCaseFail(Throwable throwable) {
        stream.println("Fail");
        stream.println("Exception Raised: " + throwable.getClass().getName());
        if (throwable.getMessage() != null) {
            stream.println("Exception Message: " + throwable.getMessage());
        }
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {
        String message = testClass.getName() + " raised a fatal exception of type " +
                throwable.getClass().getName() + ". Testing of this class stopped.";
        stream.print(message);
    }

    @Override
    public void logTestBegin(Class testClass) {}

    @Override
    public void logTestEnd() { stream.println(""); }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        stream.print("Passed tests: " + passedTests + "\n" +
                "Failed tests: " + failedTests + "\n");
    }
}
