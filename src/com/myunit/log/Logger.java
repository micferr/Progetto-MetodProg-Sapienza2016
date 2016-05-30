package com.myunit.log;

import java.lang.reflect.Method;

public interface Logger {
    void log(String message);
    void logClassNumTests(int numTests);
    void logTotalNumTests(int numTests);
    void logExceptionRaised(Class testClass, Method method, Throwable errorCause);
    void logExecutingMethod(Method method);
    void logTestCaseSuccess();
    void logTestCaseFail(Throwable error);
    void logSkipWholeTest(Class testClass, Throwable throwable);
    void logTestBegin(Class testClass);
    void logTestEnd();
    void logSuiteResults(int passedTests, int failedTests);
    void endLog(boolean interrupted);
}
