package com.futoshita.coros;

public class AppParameters {

    private static AppParameters instance = null;

    private String username = null;
    private String password = null;
    private Integer startDate = null;
    private Integer endDate = null;
    private String exportType = null;
    private String outputDirectory = null;
    private String accessToken = null;
    private boolean debug = false;

    private AppParameters() {

    }

    public static AppParameters getInstance() {
        if (instance == null) {
            instance = new AppParameters();
        }

        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStartDate() {
        return startDate;
    }

    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void reset() {
        username = null;
        password = null;
        startDate = null;
        endDate = null;
        exportType = null;
        outputDirectory = null;
        accessToken = null;
        debug = false;
    }
}
