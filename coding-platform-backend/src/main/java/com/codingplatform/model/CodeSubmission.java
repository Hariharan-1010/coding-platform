package com.codingplatform.model;

public class CodeSubmission {
    private String sourceCode;
    private String languageId; // Judge0 language ID (e.g., "71" for Python)
    private String stdin;

    // Getters and Setters
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public String getLanguageId() { return languageId; }
    public void setLanguageId(String languageId) { this.languageId = languageId; }
    public String getStdin() { return stdin; }
    public void setStdin(String stdin) { this.stdin = stdin; }
}