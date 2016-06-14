package com.myunit.test;

import com.myunit.assertion.TestFailedError;
import com.myunit.log.Logger;
import com.myunit.log.StreamLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for running the test classes and logging test info.
 */
public class TestRunner {
    private Logger out;
    private int failedTests;
    private int executedTests;
    private boolean runOptionalTests;
    private boolean interrupted;

    /**
     * Builds a TestRunner which outputs logging information on
     * System.out.
     */
    public TestRunner() {
        this(new StreamLogger(System.out));
    }

    /**
     * Builds a TestRunner which uses the {@code logger} parameter for
     * logging test information.
     *
     * @param logger The bound logger
     */
    public TestRunner(Logger logger) {
        out = logger;
        failedTests = 0;
        executedTests = 0;
        runOptionalTests = true;
        interrupted = false;
    }

    /**
     * @return The bound Logger
     */
    public Logger getLogger() {
        return out;
    }

    /**
     * @param logger The Logger to bind
     */
    public void setLogger(Logger logger) {
        out = logger;
    }

    /**
     * @param runOptTests Whether optional tests have to be executed
     * @return this
     *
     * @see Test#optional()
     */
    public TestRunner runOptionalTests(boolean runOptTests) {
        this.runOptionalTests = runOptTests;
        return this;
    }

    /**
     * <p>Interrupts testing.</p>
     * <p>Testing is interrupted after the current test method has
     * finished. Using {@link Thread#interrupt()} is not recommended
     * as it could interfere with the test methods.</p>
     */
    public void interrupt() {
        this.interrupted = true;
    }

    /**
     * Run the tests.
     *
     * @param testClasses The classes with the test methods
     */
    public void run(Class... testClasses) {
        if (testClasses != null) {
            out.logTotalNumTests(countTotalTests(testClasses));
            failedTests = 0;
            executedTests = 0;
            for (Class testClass : testClasses) {
                out.logTestBegin(testClass);
                out.logClassNumTests(countClassTests(testClass));
                try {
                    Object test = buildTest(testClass);
                    setUp(test);
                    executeTests(test);
                    tearDown(test);
                } catch (Throwable exception) {
                    out.logSkipWholeTest(testClass, exception);
                }
                out.logTestEnd();
                if (this.interrupted) {
                    out.endLog(true);
                    return;
                }
            }
            out.logSuiteResults(executedTests - failedTests, failedTests);
            out.endLog(false);
        } else {
            throw new NullPointerException("Null array passed to run");
        }
    }

    /**
     * @param testClasses The classes whose test methods are to be counted
     * @return The number of test methods
     */
    private int countTotalTests(Class... testClasses) {
        int count = 0;
        for (Class c : testClasses) {
            count += countClassTests(c);
        }
        return count;
    }

    /**
     * @param c The class whose test methods are to be counted
     * @return The number of test methods
     */
    private int countClassTests(Class c) {
        int count = 0;
        for (Method m : c.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Builds an object of type {@code testClass} using its default
     * constructor (assuming the class has it) and returns it.
     *
     * @param testClass The Class to build
     * @return The built object
     * @throws Throwable Thrown from any error source both from retrieving
     *                   the constructor (e.g. building a class with no
     *                   default constructor) or from running the constructor
     *                   itself (e.g. exceptions thrown in the constructor's
     *                   body)
     */
    private Object buildTest(Class<?> testClass) throws Throwable {
        out.log("Building " + testClass.getSimpleName() + "...");
        return testClass.getConstructor().newInstance();
    }

    /**
     * Sets up {@code test} by running the methods annotated with the
     * {@link Before} annotation.
     *
     * @param test The test object
     * @throws Throwable Thrown from any error source both from retrieving
     *                   the methods or from running them.
     */
    private void setUp(Object test) throws Throwable {
        Class testClass = test.getClass();
        out.log("Setting up " + testClass.getSimpleName() + "...");
        executeAnnotatedMethods(test, Before.class);
    }

    /**
     * Executes the test methods by calling them on {@code test}.
     *
     * @param test The test Object containing the test methods
     */
    private void executeTests(Object test) {
        Class testClass = test.getClass();
        out.log("Testing " + testClass.getSimpleName() + "...");
        getSortedTests(testClass).forEach((method) -> {
            if (method.isAnnotationPresent(Test.class)) {
                testMethod(test, method);
            }
        });
    }

    /**
     * @param testClass The class whose methods are to be retrieved
     * @return The sorted list of retrieved test methods
     */
    private List<Method> getSortedTests(Class testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> tests = new ArrayList<>();
        for (Method method : methods) {
            boolean methodHasTestAnnotation = method.isAnnotationPresent(Test.class),
                    isOptionalTest =
                            methodHasTestAnnotation && method.getDeclaredAnnotation(Test.class).optional();
            if (methodHasTestAnnotation && (!isOptionalTest || runOptionalTests)) {
                tests.add(method);
            }
        }
        tests.sort((o1, o2) -> {
            boolean o1isSorted = o1.isAnnotationPresent(Sorted.class);
            boolean o2isSorted = o2.isAnnotationPresent(Sorted.class);
            if (!o1isSorted && !o2isSorted) {
                return 0;
            } else if (o1isSorted && !o2isSorted){
                return -1;
            } else if (!o1isSorted) {
                return 1;
            } else {
                return o1.getDeclaredAnnotation(Sorted.class).value() -
                        o2.getDeclaredAnnotation(Sorted.class).value();
            }
        });
        return tests;
    }

    /**
     * <p>Runs the test methods and logs the result.</p>
     * <p>It is currently marked as protected so that it can be
     * overridden in GuiLogger with the addition of a
     * {@link Thread#sleep(long)} instruction in order to
     * willingly slow down GUI updates.</p>
     *
     * @param test The test object, caller of the test method
     * @param method The test method to call
     */
    protected void testMethod(Object test, Method method) {
        out.logExecutingMethod(method);
        executedTests++;
        try {
            method.setAccessible(true);
            method.invoke(test);
            if (expectsExceptions(method)) {
                TestFailedError error = new TestFailedError(method.getName() +
                        " returned without throwing an exception, but expected one of type " +
                        method.getDeclaredAnnotation(Test.class).expected().getName());
                onFailedTest(error);
            } else {
                out.logTestCaseSuccess();
            }
        } catch (InvocationTargetException exception) {
            if (isExpectedException(exception, method)) {
                out.logTestCaseSuccess();
                return;
            }
            onFailedTest(exception.getCause());
        } catch (IllegalAccessException exception) {
            out.log("Should never happen.");
            onFailedTest(exception.getCause());
        }
    }

    /**
     * @param method The test method to be inspected (is assumed non-null)
     * @return Whether the test expects an exception.
     */
    private boolean expectsExceptions(Method method) {
        return method.getDeclaredAnnotation(Test.class).expected() != Test.None.class;
    }

    /**
     * @param exception The exception thrown from a {@code method}
     * @param method The executed test method
     * @return Whether the thrown exception is the expected ne
     */
    private boolean isExpectedException(Exception exception, Method method) {
        return exception.getCause().getClass().equals(method.getDeclaredAnnotation(Test.class).expected());
    }

    /**
     * Logs the test failure with the bound {@link Logger}.
     *
     * @param throwable The test failure cause
     */
    private void onFailedTest(Throwable throwable) {
        failedTests++;
        out.logTestCaseFail(throwable);
    }

    /**
     * Executes tear-down methods (e.g. stream closing) marked
     * with {@link After}.
     *
     * @param test The test object
     * @throws Throwable Thrown from any error source both from retrieving
     *                   the methods or from running them.
     */
    private void tearDown(Object test) throws Throwable {
        Class testClass = test.getClass();
        out.log("Tearing down " + testClass.getSimpleName() + "...");
        executeAnnotatedMethods(test, After.class);
    }

    /**
     * Executes all the methods of {@code test} annotated with the
     * {@code annotationClass} annotation.
     *
     * @param test The test object
     * @param annotationClass The selector annotation
     * @throws Throwable Thrown from any error source both from retrieving
     *                   the methods or from running them.
     */
    private void executeAnnotatedMethods(Object test, Class<? extends Annotation> annotationClass) throws Throwable {
        for (Method method : test.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                out.log("Executing method: " + method.getName());
                method.setAccessible(true);
                method.invoke(test);
            }
        }
    }
}
