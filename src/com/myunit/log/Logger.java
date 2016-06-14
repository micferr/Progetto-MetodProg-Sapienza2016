package com.myunit.log;

import java.lang.reflect.Method;

/**
 * <p>
 * Class responsible for logging during the tests execution.
 * </p><p>
 * Contains several methods, each logging different information.
 * If the implementing class doesn't need all of them, it can
 * extend {@link LoggerAdapter} instead of implementing this
 * interface.
 * </p>
 */
public interface Logger {
    /**
     * <p>Logs a generic info message. The messages are pre-specified
     * in {@link com.myunit.test.TestRunner} and not configurable,
     * and are only intended to represent info about the {@code TestRunner}'s
     * execution rather than a customizable, general-purpose messaging
     * system.</p>
     *
     * <p>Most implementing classes do not need to more than an empty
     * implementation of this method, or will at most send the message
     * to a stream.</p>
     *
     * @param message The message to be logged
     */
    void log(String message);

    /**
     * Logs the number of methods marked with the {@link com.myunit.test.Test}
     * annotation in the current test class.
     *
     * @param numTests The number of tests in the class
     */
    void logClassNumTests(int numTests);

    /**
     * Logs the number of methods marked with the {@link com.myunit.test.Test}
     * annotation in all classes in the test suite.
     *
     * @param numTests The number of tests in the whole suite
     */
    void logTotalNumTests(int numTests);

    /**
     * Logs the occurrence of an unexpected exception in the tested method.
     * (Currently not used by the {@code TestRunner}.
     *
     * @param testClass The class being tested
     * @param method The test method that threw the exception
     * @param errorCause The thrown exception
     */
    void logExceptionRaised(Class testClass, Method method, Throwable errorCause);

    /**
     * Logs the start of a test method's execution.
     *
     * @param method The test method being started
     */
    void logExecutingMethod(Method method);

    /**
     * Logs the successful execution of a test method.
     */
    void logTestCaseSuccess();

    /**
     * Logs the failed execution of a test method.
     *
     * @param error The exception that caused the test to fail, either an
     *              unexpected exception or a {@link com.myunit.assertion.TestFailedError}.
     */
    void logTestCaseFail(Throwable error);

    /**
     * Logs the occurrence of a critical error while setting up, testing, or
     * tearing down a test method, which resulted in terminating the execution
     * of the current class's tests.
     *
     * @param testClass The class being tested
     * @param throwable The thrown error
     */
    void logSkipWholeTest(Class testClass, Throwable throwable);

    /**
     * Logs the start of a test class's execution.
     *
     * @param testClass The class to be tested
     */
    void logTestBegin(Class testClass);

    /**
     * <p>Logs the end of a test class's execution.</p>
     * <p>The test class is the same passed to the last {@code logTestBegin}
     * invocation.</p>
     */
    void logTestEnd();

    /**
     * Logs the end of the whole test suite's execution.
     *
     * @param passedTests Number of successful tests
     * @param failedTests Number of failed tests
     */
    void logSuiteResults(int passedTests, int failedTests);

    /**
     * Ends the logging. To be used for releasing resources,
     * closing streams, etc.
     *
     * @param interrupted If the TestRunner was prematurely interrupted
     *                    from its caller
     */
    void endLog(boolean interrupted);
}
