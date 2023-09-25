package com.example.testurl;

public class WebsiteMonitorApp {
    public static void main(String[] args) {
        WebsiteMonitor websiteMonitor = new WebsiteMonitor("config.txt");
        WebsiteObserver emailAlert = new EmailAlert();
        websiteMonitor.addObserver(emailAlert);
        websiteMonitor.periodicCheckWebsites(websiteMonitor.getCheckingIntervalInSeconds());
    }
}
