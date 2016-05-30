package com.myunit.log;

import java.lang.reflect.Method;

public class HTMLLogger extends LoggerAdapter implements Logger {
    private FileLogger fileLogger;

    public HTMLLogger(String file) {
        fileLogger = new FileLogger(file);
        fileLogger.log("<html><head></head><body>");
    }

    public HTMLLogger openLogAfterTests(boolean autoOpenLog) {
        fileLogger.openLogAfterTests(autoOpenLog);
        return this;
    }

    public HTMLLogger writeEventLog(boolean writeEventLog) {
        fileLogger.writeEventLog(writeEventLog);
        return this;
    }

    @Override
    public void log(String message) {
        fileLogger.logEvent(message + "<br />");
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        fileLogger.logEvent(testClass.getName() + "#" + method.getName() +
                "raised an exception of type " + errorCause.getClass().getName() +
                (errorCause.getMessage() != null ? "with message: " + errorCause.getMessage() : "") +
                "<br />");
    }

    @Override
    public void logExecutingMethod(Method method) {
        fileLogger.log("<tr><td>"+method.getName()+"</td>");
    }

    @Override
    public void logTestCaseSuccess() {
        fileLogger.log("<td><font color=\"green\">Success</font></td><td></td></tr>\n");
    }

    @Override
    public void logTestCaseFail(Throwable throwable) {
        fileLogger.log("<td><font color=\"red\">Fail</font></td><td>"+throwable.getClass().getName()+"</td>");
        String message = throwable.getMessage();
        if (message != null) {
            fileLogger.log("<td>" + message + "</td>");
        }
        fileLogger.log("</tr>\n");
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {

    }

    @Override
    public void logTestBegin(Class testClass) {
        fileLogger.log("<table>\n<tr><td colspan=\"4\">"+testClass.getName()+"</td></tr>\n");
    }

    @Override
    public void logTestEnd() {
        fileLogger.log("</table><br />\n");
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        fileLogger.log("</body></html>\n\n");
        fileLogger.log("Executed tests: " + (passedTests+failedTests) + "<br />\n"
                + "Passed: " + passedTests + "<br />\n"
                + "Failed: " + failedTests + "<br />\n");
        fileLogger.end();
    }
}