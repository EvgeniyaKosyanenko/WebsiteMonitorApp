package com.example.testurl;

public interface WebsiteObservable {
    void addObserver(WebsiteObserver observer);
    void removeObserver(WebsiteObserver observer);
}
