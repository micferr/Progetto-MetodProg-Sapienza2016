package com.myunit.log.gui;

import com.myunit.log.*;
import com.myunit.test.TestRunner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

/**
 * Displays the test results in a JavaFX GUI.
 *
 * This object is a Singleton, reset() has to be called in order
 * for the object to be reused. No guarantees concerning
 * thread-safety are given.
 */
public class GuiLogger extends Application implements Logger {
    private static MultiLogger externalLogger = null;
    private static ReplayLogger logReplayer = null;
    private static Class[] testClasses = new Class[]{};
    private static volatile boolean initialized = false;
    /**
     * Signal whether the test execution has been interrupted,
     * e.g. by closing the test window.
     * Methods acting on the window have to check this flag
     * before doing so.
     */
    private static volatile boolean interrupted;

    /**
     * Total number of test methods
     */
    private int numTests;
    /**
     * Tests contained in current class
     */
    private int expectedClassTests;
    /**
     * Processed tests, not considering current class's tests
     */
    private int currentTestCount;
    /**
     * Processed tests, only considers current class's tests
     */
    private int currentClassTestCount;

    private ObservableList<TableRowData> tableRows;
    private TableView<TableRowData> resultTable;
    private TextArea textArea;
    private ProgressBar progressBar;
    private Stage primaryStage;
    private Thread testThread;

    private static volatile boolean timeoutClose = false;
    private static volatile long timeoutCloseMillis = 0;

    private ConcurrentLinkedQueue<String> methodNamesQueue = new ConcurrentLinkedQueue<>();

    public GuiLogger() {
        this(null, (Class[])null);
    }

    /**
     * @param testClasses The classes with the @Test-annotated methods to be tested
     */
    public GuiLogger(
            Class... testClasses
    ) {
        this(null,testClasses);
    }

    /**
     * @param loggers Loggers receiving the task of logging the test
     *                suite execution
     * @param classes The classes with the @Test-annotated methods to be tested
     */
    private GuiLogger(
            List<Logger> loggers,
            Class... classes
    ) {
        if (!initialized) {
            logReplayer = new ReplayLogger();
            List<Logger> allLoggers = new ArrayList<>();
            if (loggers != null) {
                allLoggers.addAll(loggers);
            }
            allLoggers.add(logReplayer);
            externalLogger = new MultiLogger(allLoggers);
            testClasses = classes != null ? classes : new Class[]{};
            initialized = true;
            interrupted = false;
            numTests = 0;
            expectedClassTests = 0;
            currentTestCount = 0;
            currentClassTestCount = 0;
        }
    }

    /**
     * Reset the Singleton GuiLogger, making it available
     * for reinitialization
     */
    public void reset() {
        externalLogger = null;
        testClasses = null;
        initialized = false;
        interrupted = false;
        try {
            testThread.join();
        } catch (InterruptedException e) {}
        testThread = null;
    }

    /**
     * Sets the external loggers
     *
     * @param loggers Loggers receiving the task of logging the test
     *                suite execution
     * @return the caller
     */
    public GuiLogger setLoggers(Logger... loggers) {
        if (initialized) {
            try {
                for (Logger l : loggers) {
                    Objects.requireNonNull(l);
                }
                List<Logger> allLoggers = new ArrayList<>(Arrays.asList(loggers));
                allLoggers.add(logReplayer);
                externalLogger = new MultiLogger(allLoggers);
                return this;
            } catch (NullPointerException e) {
                throw new NullPointerException("Null logger set to a GuiLogger");
            }
        } else {
            throw new IllegalStateException("GuiLogger not initialized");
        }
    }

    /**
     * Make the window close after specified timeout
     *
     * @param millis Number of milliseconds to wait. Time is considered to start
     *               after all test have finished.
     */
    public void setAutocloseTimeout(long millis) {
        if (millis > 0) {
            timeoutClose = true;
            timeoutCloseMillis = millis;
        } else {
            timeoutClose = false;
            timeoutCloseMillis = 0;
        }
    }

    /**
     * Starts the GUI logger and runs the tests
     * <p>
     * Calling this method modifies "controls" Logger's
     * log level from INFO to WARNING to avoid unnecessary INFO level
     * logging (refer bug RT-40654)
     * </p>
     *
     * @param args Command-line application arguments
     */
    public void run(String[] args) {
        if (initialized)
            launch(args);
        else
            throw new IllegalStateException("GuiLogger not initialized");
    }

    /**
     * Starts the GuiLogger and test execution. Not to be explicitly
     * called - run has to be used instead
     *
     * @param primaryStage The main window
     */
    @Override
    public void start(Stage primaryStage) {
        //Todo: check initialized and called from run

        Menu fileMenu = new Menu("File");
        Menu exportMenu = new Menu("Export results");
        MenuItem exportJUnit = new MenuItem("As JUnit XML");
        //todo custom file names
        //todo wait log end for activation
        exportJUnit.setOnAction(e ->
            logReplayer.replay(new JUnitXMLLogger("log.xml").openLogAfterTests(false))
        );
        //exportJUnit.setDisable(true);
        MenuItem exportHTML = new MenuItem("As HTML page");
        exportHTML.setOnAction(e -> {
            String absPath = getSaveFileLocation();
            if (absPath != null) logReplayer.replay((new HTMLLogger(absPath).openLogAfterTests(false)));
        });
        //exportHTML.setDisable(true);
        exportMenu.getItems().addAll(exportJUnit, exportHTML);
        fileMenu.getItems().add(exportMenu);
        MenuBar menuBar = new MenuBar(fileMenu);

        makeOutputTable();

        TabPane classTabs = new TabPane(); //todo add support for splitting the suite view in tabs (one for each test class)

        textArea = new TextArea();
        textArea.setEditable(false);

        progressBar = new ProgressBar();
        progressBar.setMinHeight(20);
        progressBar.setPrefWidth(800);

        TestRunner testRunner = startTestThread(); //Todo - startTT -> makeTT, launch after primaryStage.show()

        VBox mainLayout = new VBox(menuBar, resultTable, textArea, progressBar);
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.widthProperty().addListener( (observable, oldValue, newValue) -> {
            if (isRunning()) {
                progressBar.setPrefWidth(newValue.doubleValue());
            }
        });
        primaryStage.heightProperty().addListener( (observable, oldValue, newValue) -> {
            if (isRunning()) {
                resultTable.setPrefHeight(
                        newValue.doubleValue() -
                                textArea.getHeight() -
                                progressBar.getHeight()
                );
            }
        });
        primaryStage.setOnCloseRequest(event -> {
            testThread.interrupt();
            testRunner.interrupt();
            interrupted = true;
        });
        this.primaryStage = primaryStage;
        primaryStage.show();

        /**
         * No "controls" Logger exists before call to Stage.show()
         */
        java.util.logging.Logger l = java.util.logging.LogManager.getLogManager().getLogger("controls");
        if (l!=null) {
            l.setLevel(Level.WARNING);
        }
    }

    /**
     * Displays a Save File window and return the path to the choosen file.
     *
     * @return The absolute path to the file to save, or null, if the operation
     *         is cancelled.
     */
    private String getSaveFileLocation() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(System.getProperty("user.dir")));
        File f = fc.showSaveDialog(primaryStage);
        return f != null ? f.getAbsolutePath() : null;
    }

    /**
     * Sets up the output TableView
     */
    private void makeOutputTable() {
        tableRows = FXCollections.observableArrayList();
        TableColumn<TableRowData, String> methodsColumn = new TableColumn<>("Test method");
        methodsColumn.setCellValueFactory(p -> {
            if (p.getValue() != null) {
                return new SimpleStringProperty(p.getValue().getMethodName());
            } else {
                return new SimpleStringProperty("<no name>");
            }
        });
        methodsColumn.setPrefWidth(350);
        TableColumn<TableRowData, String> resultColumn = new TableColumn<>("Result");
        resultColumn.setCellValueFactory(p -> {
            if (p.getValue() != null) {
                return new SimpleStringProperty(p.getValue().getTestResult());
            } else {
                return new SimpleStringProperty("<no result>");
            }
        });
        resultColumn.setCellFactory(
                col -> new TableCell<TableRowData, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !interrupted) {
                            setText(item);
                            switch (item) {
                                case "Success":
                                    setStyle("-fx-background-color: \"0x00FF00\"");
                                    break;
                                case "Fail":
                                    setStyle("-fx-background-color: \"0xFF0000\"");
                                    break;
                                default:
                                    setStyle(""); // Needed for empty lines
                                    break;
                            }
                        }
                    }
                }
        );
        TableColumn<TableRowData, String> notesColumn = new TableColumn<>("Notes");
        notesColumn.setCellValueFactory(p -> {
            if (p.getValue() != null) {
                return new SimpleStringProperty(p.getValue().getNotes());
            } else {
                return new SimpleStringProperty("");
            }
        });
        notesColumn.setPrefWidth(350);
        resultTable = new TableView<>(tableRows);
        resultTable.getColumns().setAll(methodsColumn, resultColumn, notesColumn);
        resultTable.getItems().addListener((ListChangeListener<TableRowData>) (c -> {
            c.next();
            scrollTableToBottom();
        }));
    }

    /**
     * Launches a test suite in another thread. The TestRunner is
     * returned as soon as it is started
     *
     * @return The TestRunner executing the tests
     */
    private TestRunner startTestThread() {
        TestRunner testRunner = new TestRunner(this) {
            @Override
            protected void testMethod(Object test, Method method) {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {}
                super.testMethod(test, method);
            }
        };
        testThread = new Thread(() -> {
            testRunner.run(testClasses);
        });
        testThread.start();
        return testRunner;
    }

    /**
     * Scrolls the result table to the bottom
     */
    private void scrollTableToBottom() {
        Platform.runLater(()-> {
            if (isRunning()) {
                final int size = resultTable.getItems().size();
                if (size > 1) {
                    resultTable.scrollTo(size - 1);
                }
            }
        });
    }

    private void scrollOutputLogToBottom() {
        Platform.runLater(() -> {
            if (isRunning()) {
                textArea.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    @Override
    public void log(String message) {
        Platform.runLater(() -> {
            if (isRunning()) {
                String logText = textArea.getText();
                textArea.appendText((logText.equals("") ? "" : "\n") + message);
                scrollOutputLogToBottom();
            }
        });
        externalLogger.log(message);
    }

    @Override
    public void logClassNumTests(int numTests) {
        this.expectedClassTests = numTests;
        externalLogger.logClassNumTests(numTests);
    }

    @Override
    public void logTotalNumTests(int numTests) {
        this.numTests = numTests;
        externalLogger.logTotalNumTests(numTests);
    }

    @Override
    public void logExceptionRaised(Class testClass, Method method, Throwable errorCause) {
        externalLogger.logExceptionRaised(testClass, method, errorCause);
    }

    String currentMethodName; //Todo - Use concurrent queue
    @Override
    public void logExecutingMethod(Method method) {
        currentMethodName = method.getName();
        methodNamesQueue.add(method.getName());
        log("Executing " + currentMethodName);
        externalLogger.logExecutingMethod(method);
    }

    @Override
    public void logTestCaseSuccess() {
        putNewResultRow("Success", "");
        externalLogger.logTestCaseSuccess();
    }

    @Override
    public void logTestCaseFail(Throwable error) {
        putNewResultRow(
                "Fail",
                error.getClass().getSimpleName() + " - " + error.getMessage()
        );
        externalLogger.logTestCaseFail(error);
        log("Test failed with exception of type " + error.getClass().getSimpleName() + " thrown at:");
        for (StackTraceElement ste : error.getStackTrace()) {
            log("\t"+ste.toString());
        }
    }

    private void putNewResultRow(String result, String notes) {
        if (isRunning()) {
            Platform.runLater(() -> {
                //tableRows.add(new TableRowData("\t" + new String(currentMethodName), result, notes));
                tableRows.add(new TableRowData("\t" + methodNamesQueue.remove(), result, notes));
                currentClassTestCount++;
                setProgress(currentTestCount + currentClassTestCount, numTests);
            });
        }
    }

    @Override
    public void logSkipWholeTest(Class testClass, Throwable throwable) {
        if (isRunning()) {
            currentTestCount += expectedClassTests;
            expectedClassTests = 0;
            currentClassTestCount = 0;
            setProgress(currentTestCount, numTests);
        }
        externalLogger.logSkipWholeTest(testClass, throwable);
    }

    /**
     * Fills the bar up to (current/total) percent
     *
     * @param current Processed values
     * @param total Total values
     */
    private void setProgress(int current, int total) {
        Platform.runLater(() -> {
            if (isRunning()) {
                progressBar.setProgress(((double) current) / total);
            }
        });
    }

    @Override
    public void logTestBegin(Class testClass) {
        if (isRunning()) {
            Platform.runLater(() -> {
                tableRows.add(new TableRowData(testClass.getName(), "", ""));
            });
        }
        externalLogger.logTestBegin(testClass);
    }

    @Override
    public void logTestEnd() {
        externalLogger.logTestEnd();
    }

    @Override
    public void logSuiteResults(int passedTests, int failedTests) {
        log("\nTest suite finished."+
                "\nPassed: " + passedTests +
                "\nFailed: " + failedTests +
                "\nTotal: " + (passedTests+failedTests));
        externalLogger.logSuiteResults(passedTests, failedTests);
    }

    @Override
    public void endLog(boolean interrupted) {
        if (isRunning()) {
            scrollOutputLogToBottom();
            startAutocloseThread();
        }
        externalLogger.endLog(interrupted);
    }

    /**
     * <p>If auto-closing on timeout has been enabled, launches
     * the thread performing it.</p>
     * <p>The thread is a Daemon Thread so that it doesn't stop
     * program termination when the GUI is manually closed.</p>
     */
    private void startAutocloseThread() {
        if (timeoutClose) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(timeoutCloseMillis);
                } catch (InterruptedException e) {}
                Platform.runLater(() -> primaryStage.close());
            });
            t.setDaemon(true);
            t.start();
        }
    }

    private boolean isRunning() {
        return initialized && !interrupted;
    }
}
