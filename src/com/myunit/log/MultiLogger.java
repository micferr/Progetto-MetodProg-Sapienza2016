package com.myunit.log;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MultiLogger implements Logger {
    private List<Logger> loggers;

    public MultiLogger(Logger... loggers) {
        this(Arrays.asList(loggers));
    }

    public MultiLogger(List<Logger> loggers) {
        this.loggers = loggers;
        this.loggers.forEach(logger -> {
            if (logger == null) {
                throw new NullPointerException("Null logger used to construct a MultiLogger");
            }
        });
    }

    @Override
    public void log(String message) {
        loggers.forEach(logger -> logger.log(message));
    }

    @Override
    public void logClassNumTests(int numTests) {
        loggers.forEach(logger -> logger.logClassNumTests(numTests));
    }

    @Override
    public void logTotalNumTests(int numTests) {
        loggers.forEach(logger -> logger.logTotalNumTests(numTests));
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        loggers.forEach(logger -> logger.logExceptionRaised(testClass, method, errorCause));
    }

    @Override
    public void logExecutingMethod(Method method) {
        loggers.forEach(logger -> logger.logExecutingMethod(method));
    }

    @Override
    public void logTestCaseSuccess() {
        loggers.forEach(Logger::logTestCaseSuccess);
    }

    @Override
    public void logTestCaseFail(Throwable error) {
        loggers.forEach(logger -> logger.logTestCaseFail(error));
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {
        loggers.forEach(logger -> logger.logSkipWholeTest(testClass, throwable));
    }

    @Override
    public void logTestBegin(Class testClass) {
        loggers.forEach(logger -> logger.logTestBegin(testClass));
    }

    @Override
    public void logTestEnd() {
        loggers.forEach(Logger::logTestEnd);
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        loggers.forEach(logger -> logger.logSuiteResults(passedTests, failedTests));
    }

    @Override
    public void endLog(boolean interrupted) {
        loggers.forEach(logger -> logger.endLog(interrupted));
    }
}
