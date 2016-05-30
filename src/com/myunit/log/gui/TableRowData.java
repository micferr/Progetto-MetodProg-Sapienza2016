package com.myunit.log.gui;

/**
 * Model object for the data displayed in the output table
 */
class TableRowData {
    public TableRowData() {}

    /**
     * Construct the object explictly initializing its values
     *
     * @param methodName The tested method (simple name, without return
     *                   type nor parameter list)
     * @param testResult String representing the result, "Success" on test
     *                   pass, "Fail" otherwise
     * @param notes Notes and debugging information to display on test
     *              failure
     */
    public TableRowData(String methodName, String testResult, String notes) {
        this.methodName = methodName;
        this.testResult = testResult;
        this.notes = notes;
    }

    /**
     * The method's name (simple name, without return type nor
     * parameter list)
     */
    private String methodName = "";

    public String getMethodName() { return methodName; }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * The result, "Success" on test pass, "Fail" otherwise
     */
    private String testResult = "";

    public String getTestResult() { return testResult; }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    /**
     * Test notes (e.g. thrown exceptions or debugging information)
     */
    private String notes = "";

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
