package com.myunit.log;

import java.lang.reflect.Method;

public class LoggerAdapter implements Logger {
    @Override
    public void log(String message) {}

    @Override
    public void logClassNumTests(int numTests) {}

    @Override
    public void logTotalNumTests(int numTests) {}

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {}

    @Override
    public void logExecutingMethod(Method method) {}

    @Override
    public void logTestCaseSuccess() {}

    @Override
    public void logTestCaseFail(Throwable error) {}

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {}

    @Override
    public void logTestBegin(Class testClass) {}

    @Override
    public void logTestEnd() {}

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {}

    @Override
    public void endLog(boolean interrupted) {}
}
