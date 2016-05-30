package com.myunit.selftest;

import com.myunit.log.JUnitXMLLogger;
import com.myunit.log.Logger;
import com.myunit.log.gui.GuiLogger;
import com.myunit.test.*;

/**
 * The class in charge of the framework self-testing. Executing its main method starts the testing.
 * When the testing is finished, a report is saved in JUnit XML format, such as to be compatible
 * with test reporting frameworks such as Jenkins
 *
 * @see TestRunner
 * @see com.myunit.log.Logger
 */
public class SelfTestSuite {
    /**
     * Specifies SelfTestSuite's running mode
     */
    private enum TestMode {
        STANDARD,
        GUI
    }

    /**
     * The main method to execute to start testing
     *
     * @param args args[0] - Optional parameter, number of milliseconds
     *             to wait for the GUI to close after all the tests have
     *             finished.
     */
    public static void main(String[] args){
        Class[] testClasses = new Class[] {
                TestAssertMiscellaneous.class,
                TestAssertTrueFalse.class,
                TestAssertEquals.class,
                TestAssertNotEquals.class,
                TestAssertGreaterThan.class,
                TestAssertLessThan.class,
                TestAssertGreaterThanOrEquals.class,
                TestAssertLessThanOrEquals.class,
                TestNullNotNull.class
        };
        TestMode testMode = TestMode.GUI;
        Logger junitLogger = new JUnitXMLLogger("log.xml").openLogAfterTests(false);
        switch (testMode) {
            case STANDARD:
                new TestRunner(junitLogger).run(testClasses);
                break;
            case GUI:
                GuiLogger guiLogger = new GuiLogger(testClasses);
                guiLogger.setLoggers(junitLogger);
                guiLogger.setAutocloseTimeout(args.length > 0 ? Long.parseLong(args[0]) : 0);
                guiLogger.run(args);
                break;
            default: break;
        }
    }
}
