package com.example.testurl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebsiteMonitor implements WebsiteObservable {

    public enum WebsiteStatus {
        CONNECTION_OK_CONTENT_OK,
        CONNECTION_OK_CONTENT_PROBLEM,
        CONNECTION_PROBLEM
    }

    private List<String> websiteUrls;
    private List<String> contents;
    private long totalTime;
    private List<WebsiteObserver> observers = new ArrayList<WebsiteObserver>();
    private int checkingIntervalInSeconds = 60; // Default interval

    public WebsiteMonitor(String configFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            websiteUrls = new ArrayList<>();
            contents = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    websiteUrls.add(parts[0]);
                    contents.add(parts[1]);
                    System.out.println("websiteUrls - " + parts[0] + ", contents - " + parts[1]);
                } else if (parts.length == 2 && parts[0].equalsIgnoreCase("interval")) {
                    checkingIntervalInSeconds = Integer.parseInt(parts[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToLogFile(String url, String content, WebsiteStatus websiteStatus) {
        String logFileName = "website_monitor_log.txt";

        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            Date currentDate = new Date();
            String status;

            switch (websiteStatus) {
                case CONNECTION_OK_CONTENT_OK:
                    status = "Connection OK, Content OK";
                    break;
                case CONNECTION_OK_CONTENT_PROBLEM:
                    status = "Connection OK, Content Problem";
                    break;
                case CONNECTION_PROBLEM:
                    status = "Connection Problem";
                    break;
                default:
                    status = "Unknown Status";
                    break;
            }

            printWriter.printf("[%s] URL: %s, Content: %s, %s%n", currentDate, url, content, status);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkWebsites() {
        for (int i = 0; i < websiteUrls.size(); i++) {
            String url = websiteUrls.get(i);
            String content = contents.get(i);
            WebsiteStatus websiteStatus = websiteIsUp(url, content);

            // Notify observers based on the websiteStatus
            switch (websiteStatus) {
                case CONNECTION_OK_CONTENT_OK:
                    notifyObserversWebsiteUp(url);
                    break;
                case CONNECTION_OK_CONTENT_PROBLEM:
                    notifyObserversWebsiteDown(url);
                    break;
                case CONNECTION_PROBLEM:
                    notifyObserversWebsiteDown(url);
                    break;
            }

            // Write to the log file
            writeToLogFile(url, content, websiteStatus);
        }
    }

     public WebsiteStatus websiteIsUp(String url, String content) {
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            long startTime = System.currentTimeMillis();

            int code = connection.getResponseCode();
            long endTime = System.currentTimeMillis();
            this.totalTime = endTime - startTime;
            System.out.println("Total time taken: " + this.totalTime + " milliseconds");

            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                if (response.toString().contains(content)) {
                    System.out.println("Connection OK, Content OK");
                    return WebsiteStatus.CONNECTION_OK_CONTENT_OK;
                } else {
                    System.out.println("Connection OK, Content Problem");
                    return WebsiteStatus.CONNECTION_OK_CONTENT_PROBLEM;
                }
            } else {
                System.out.println("Connection Problem");
                return WebsiteStatus.CONNECTION_PROBLEM;
            }
        } catch (IOException e) {
            System.out.println("Connection Problem");
            return WebsiteStatus.CONNECTION_PROBLEM;
        }
    }



    private void notifyObserversWebsiteUp(String url) {
        for (WebsiteObserver observer : observers) {
            observer.websiteUp(url);
        }
    }

    private void notifyObserversWebsiteDown(String url) {
        for (WebsiteObserver observer : observers) {
            observer.websiteDown(url);
        }
    }

    public void periodicCheckWebsites(int intervalInSeconds) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Checking websites...");
            checkWebsites();
        }, 0, intervalInSeconds, TimeUnit.SECONDS);
    }

    // Implement methods from WebsiteObservable interface
    public void addObserver(WebsiteObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WebsiteObserver observer) {
        observers.remove(observer);
    }

    public int getCheckingIntervalInSeconds() {
        return checkingIntervalInSeconds;
    }
}