package com.myunit.log.gui;

import com.myunit.log.Logger;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>This class saves all the logging calls executed on the enclosing
 * GuiLogger instance. It then provides the ability to execute the same
 * calls, with the same arguments and in the same order, on an arbitrary
 * Logger.</p>
 *
 * <p>The main use case of this class is to provide exporting functionality
 * to GuiLogger: the user will be able to select a file format, backed by an
 * appropriate Logger class, and the ReplayLogger will use the saved logs
 * to execute the calls, despite the {@link com.myunit.test.TestRunner} having
 * finished its execution.</p>
 *
 * <p>This class doesn't extend {@link com.myunit.log.LoggerAdapter} as it
 * needs to give compilation errors on the event that more logging methods
 * are added to the Logger interface. Extending {@code LoggerAdapter} could
 * result in an incomplete logging replay.</p>
 */
class ReplayLogger implements Logger {
    private enum LogMethod {
        LOG,
        LOG_CLASS_NUM_TESTS,
        LOG_TOTAL_NUM_TESTS,
        LOG_EXCEPTION_RAISED,
        LOG_EXECUTING_METHOD,
        LOG_TEST_CASE_SUCCESS,
        LOG_TEST_CASE_FAIL,
        LOG_SKIP_WHOLE_TEST,
        LOG_TEST_BEGIN,
        LOG_TEST_END,
        LOG_SUITE_RESULTS,
        END_LOG
    }
    private static class LogCall {
        public LogMethod logMethod;
        public Object[] params;

        public LogCall(LogMethod logMethod, Object... params) {
            this.logMethod = logMethod;
            this.params = params;
        }
    }

    private final List<LogCall> logCalls = new ArrayList<>();

    /**
     * Replays the logging calls this object received onto the
     * passed Logger, in the same order and with the same arguments
     *
     * @param logger The logger onto which replay the calls
     */
    public void replay(Logger logger) {
        for (LogCall logCall : logCalls) {
            Object[] params = logCall.params;
            switch (logCall.logMethod) {
                case LOG:
                    logger.log((String)params[0]); break;
                case LOG_CLASS_NUM_TESTS:
                    logger.logClassNumTests((Integer)params[0]); break;
                case LOG_TOTAL_NUM_TESTS:
                    logger.logTotalNumTests((Integer)params[0]); break;
                case LOG_EXCEPTION_RAISED:
                    logger.logExceptionRaised((Class)params[0], (Method)params[1], (Throwable)params[2]); break;
                case LOG_EXECUTING_METHOD:
                    logger.logExecutingMethod((Method)params[0]); break;
                case LOG_TEST_CASE_SUCCESS:
                    logger.logTestCaseSuccess(); break;
                case LOG_TEST_CASE_FAIL:
                    logger.logTestCaseFail((Throwable)params[0]); break;
                case LOG_SKIP_WHOLE_TEST:
                    logger.logSkipWholeTest((Class)params[0], (Throwable)params[1]); break;
                case LOG_TEST_BEGIN:
                    logger.logTestBegin((Class)params[0]); break;
                case LOG_TEST_END:
                    logger.logTestEnd(); break;
                case LOG_SUITE_RESULTS:
                    logger.logSuiteResults((Integer)params[0], (Integer)params[1]); break;
                case END_LOG:
                    logger.endLog((Boolean)params[0]); break;
                default:
                    throw new IllegalStateException("Invalid log method");
            }
        }
    }

    private void addCall(LogMethod logMethod, Object... params) {
        logCalls.add(new LogCall(logMethod, params));
    }

    @Override
    public void log(String message) {
        addCall(LogMethod.LOG, message);
    }

    @Override
    public void logClassNumTests(int numTests) {
        addCall(LogMethod.LOG_CLASS_NUM_TESTS, numTests);
    }

    @Override
    public void logTotalNumTests(int numTests) {
        addCall(LogMethod.LOG_TOTAL_NUM_TESTS, numTests);
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        addCall(LogMethod.LOG_EXCEPTION_RAISED, testClass, method, errorCause);
    }

    @Override
    public void logExecutingMethod(Method method) {
        addCall(LogMethod.LOG_EXECUTING_METHOD, method);
    }

    @Override
    public void logTestCaseSuccess() {
        addCall(LogMethod.LOG_TEST_CASE_SUCCESS);
    }

    @Override
    public void logTestCaseFail(Throwable error) {
        addCall(LogMethod.LOG_TEST_CASE_FAIL, error);
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {
        addCall(LogMethod.LOG_SKIP_WHOLE_TEST, testClass, throwable);
    }

    @Override
    public void logTestBegin(Class testClass) {
        addCall(LogMethod.LOG_TEST_BEGIN, testClass);
    }

    @Override
    public void logTestEnd() {
        addCall(LogMethod.LOG_TEST_END);
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        addCall(LogMethod.LOG_SUITE_RESULTS, passedTests, failedTests);
    }

    @Override
    public void endLog(boolean interrupted) {
        addCall(LogMethod.END_LOG, interrupted);
    }
}
