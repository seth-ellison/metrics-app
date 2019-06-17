[![Build Status](https://travis-ci.com/seth-ellison/metrics-app.svg?token=YZeWxGsRxyshtrfzLncA&branch=master)](https://travis-ci.com/seth-ellison/metrics-app)
---
# Metric App Overview
![Metric App Overview](https://i.imgur.com/QMuKpzk.png)

This application demonstrates how to gather request/response data from a Spring Boot app without using the built-in Metrics libraries of the framework.

# Installation
To run this application on your local machine, simply clone this repository and use Maven (mvn clean install) from the project's root directory to generate a JAR file.
Once done, you can simply take the generated JAR:

**metrics-app-1.0.0-SNAPSHOT.jar**

and run it in any Java-enabled command line with the command:

**java -jar metrics-app-1.0.0-SNAPSHOT.jar**

Once running, you can visit dashboard for the project by opening your web browser and navigating to [http://localhost:8080](http://localhost:8080)

Simply refresh the page to see the metric-gathering systems at work.