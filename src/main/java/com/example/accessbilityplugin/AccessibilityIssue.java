package com.example.accessbilityplugin;

public class AccessibilityIssue {

    public AccessibilityIssue(String inspectionReference, String file, String issue){
        this.fileName = file;
        this.inspectionReference = inspectionReference;
        this.issue = issue;
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

    private String inspectionReference;
    private String fileName;
    private String issue;
}
