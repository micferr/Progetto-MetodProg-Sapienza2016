package com.myunit.log;

import java.lang.reflect.Method;

public class JUnitXMLLogger extends LoggerAdapter implements Logger {
    FileLogger fileLogger;
    String buffer;

    private int totalFailures = 0;

    private String currentTestBuffer;
    private Class currentTestClass;
    private String currentTestMethodName;
    private int currentTestClassNumTests;
    private int currentTestClassNumFailures;

    public JUnitXMLLogger(String file) {
        fileLogger = new FileLogger(file);
        buffer = "";
    }

    public JUnitXMLLogger openLogAfterTests(boolean autoOpenLog) {
        fileLogger.openLogAfterTests(autoOpenLog);
        return this;
    }

    public JUnitXMLLogger writeEventLog(boolean writeEventLog) {
        fileLogger.writeEventLog(writeEventLog);
        return this;
    }

    @Override
    public void log(String message) {}

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {}

    @Override
    public void logExecutingMethod(Method method) {
        currentTestMethodName = method.getName();
        currentTestClassNumTests++;
    }

    @Override
    public void logTestCaseSuccess() {
        currentTestBuffer += "\t\t<testcase \n" +
                "\t\t\tname=\"" + currentTestMethodName + "\"\n" +
                "\t\t\tclassname=\"" + currentTestClass.getName() + "\"\n" +
                "\t\t/>\n";
    }

    @Override
    public void logTestCaseFail(Throwable error) {
        currentTestBuffer += "\t\t<testcase " +
                "\t\t\tname=\"" + currentTestMethodName + "\"\n" +
                "\t\t\tclassname=\"" + currentTestClass.getName() + "\"\n" +
                "\t\t>\n" +
                "\t\t\t<failure message=\"" + error.getCause() + "\" />\n" +
                "\t\t</testcase>\n";
        currentTestClassNumFailures++;
        totalFailures++;
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {

    }

    @Override
    public void logTestBegin(Class testClass) {
        currentTestClass = testClass;
        currentTestClassNumTests = 0;
        currentTestClassNumFailures = 0;
        currentTestBuffer = "";
    }

    @Override
    public void logTestEnd() {
        buffer += "\t<testsuite \n" +
                "\t\t\tname = \"" + currentTestClass.getSimpleName() + "\"\n" +
                "\t\t\ttests = \"" + currentTestClassNumTests + "\"\n" +
                "\t\t\tfailures = \"" + currentTestClassNumFailures +  "\" >\n" +
                currentTestBuffer +
                "\t</testsuite>\n\n";
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        buffer = "<testsuites failures = \"" + totalFailures + "\" >\n" +
                "\n" +
                buffer +
                "</testsuites>";
        fileLogger.log(buffer);
        fileLogger.end();
    }
}
