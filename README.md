# WebsiteMonitorApp
The Website Monitor App is a Java program that allows you to monitor the availability and content of websites. It periodically checks a list of websites and notifies you when there are issues. 

# Configuration
The program requires a configuration file (config.txt) to specify the websites to monitor and their expected content. The configuration file should have the following format:

https://www.example.com,Example Domain
https://www.google.com,Search the world's information
http://nonexistentwebsite.com,test
interval=60

Each line should contain a URL and the expected content separated by a comma.
The interval line specifies the checking interval in seconds (default is 60 seconds).

# To run the program, follow these steps:

Compile the source code:  javac com/example/testurl/*.java
Run the program with the configuration file: java com.example.testurl.WebsiteMonitorApp config.txt
Replace config.txt with your configuration file if it has a different name.

# Understanding the Logs
The program logs its activities to a file named website_monitor_log.txt. The log file contains entries with timestamps, URLs, expected content, and status. The status can be one of the following:

Connection OK, Content OK: The website is accessible, and the expected content was found.
Connection OK, Content Problem: The website is accessible, but the expected content was not found.
Connection Problem: There was a problem connecting to the website.
