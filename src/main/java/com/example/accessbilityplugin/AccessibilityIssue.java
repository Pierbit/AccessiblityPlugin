package com.example.accessbilityplugin;

public class AccessibilityIssue {

    @Override
    public String toString() {
        return "AccessibilityIssue{" +
                "inspectionReference='" + inspectionReference + '\'' +
                ", fileName='" + fileName + '\'' +
                ", issue='" + issue + '\'' +
                ", severity='" + severity + '\'' +
                '}';
    }

    public AccessibilityIssue(String inspectionReference, String file, String issue){
        this.fileName = file;
        this.inspectionReference = inspectionReference;
        this.issue = issue;
    }
    public AccessibilityIssue(String inspectionReference, String file, String issue, String severity){
        this.fileName = file;
        this.inspectionReference = inspectionReference;
        this.issue = issue;
        this.severity = severity;
    }

    public String getInspectionReference() {
        return inspectionReference;
    }

    public void setInspectionReference(String inspectionReference) {
        this.inspectionReference = inspectionReference;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSeverity() { return this.severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    private String inspectionReference;
    private String fileName;
    private String issue;
    private String severity;
}
