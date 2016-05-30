package com.myunit.log;

import java.lang.reflect.Method;

public class XMLLogger extends LoggerAdapter implements Logger {
    private FileLogger fileLogger;

    public XMLLogger(String file) {
        fileLogger = new FileLogger(file);
        fileLogger.log("<testSuite>\n");
    }

    public XMLLogger openLogAfterTests(boolean autoOpenLog) {
        fileLogger.openLogAfterTests(autoOpenLog);
        return this;
    }

    public XMLLogger writeEventLog(boolean writeEventLog) {
        fileLogger.writeEventLog(writeEventLog);
        return this;
    }

    @Override
    public void log(String message) {
        fileLogger.logEvent(message + "\n");
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        fileLogger.logEvent("<exceptionmessage>"+testClass.getName() + "#" + method.getName() +
                "raised an exception of type " + errorCause.getClass().getName() +
                (errorCause.getMessage() != null ? "with message: " + errorCause.getMessage() : "") +
                "</exceptionmessage>");
    }

    @Override
    public void logExecutingMethod(Method method) {
        fileLogger.log("<method><name>"+method.getName()+"</name>");
    }

    @Override
    public void logTestCaseSuccess() {
        fileLogger.log("<result>Success</result></method>\n");
    }

    @Override
    public void logTestCaseFail(Throwable throwable) {
        fileLogger.log("<result>Fail</result><failcause>"+throwable.getClass().getName()+"</failcause>");
        String message = throwable.getMessage();
        if (message != null) {
            fileLogger.log("<failmessage>" + message + "</failmessage>");
        }
        fileLogger.log("</method>\n");
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {

    }

    @Override
    public void logTestBegin(Class testClass) {
        fileLogger.log("<testclass><name>"+testClass.getName()+"</name>\n");
    }

    @Override
    public void logTestEnd() {
        fileLogger.log("</testclass>\n");
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        fileLogger.log("<executedtests>" + (passedTests + failedTests) + "</executedtests>\n"
                + "<passedtests>" + passedTests + "</passedtests>\n"
                + "<failedtests>" + failedTests + "</failedtests></testSuite>");
        fileLogger.end();
    }
}
