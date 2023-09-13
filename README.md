# Blogging Platform Performance Optimization Demo

## Overview

This project implements a Spring Boot backend for a practice blogging platform, designed as a sandbox to practice
diagnosing and mitigating bottlenecks in an application's performance. The application primarily consists of three
models: Article, Comment, and User.

* Article: Represents blog post with title and content.
* Comment: Represents a comment on an article.
* User: Represents a user of the platform that can author articles and comments.

## Installation and Setup

1. Clone the repository:
    * If you are using IntelliJ IDEA, select `File` | `New` | `Project from Version Control...` and paste the repository
      URL.
    * Alternatively, run `git clone https://github.com/egor-sergeev/blog-performance` from the command line.
2. Install the dependencies:
    * If you are using IntelliJ IDEA, open the project in the IDE, and it will automatically download the dependencies.
    * Alternatively, navigate to the project directory and run: `mvn clean install`.

If you have problems installing dependencies, make sure you have Java SDK 19 (or later) and the latest version of Maven installed.

## Running the Application

* Run the application in IntelliJ IDEA by selecting and running the `BlogPerformanceApplication` run configuration in
  the top right corner of the IDE.
* Alternatively, navigate to the project directory and run: `mvn spring-boot:run`.

This will automatically generate a database with some sample data and start the application on port 8080. You can access
the application at http://localhost:8080.

## API Endpoints

All available endpoints are described in `requests.http` file. You can run them directly from IntelliJ IDEA by clicking
the run icon next to each request.

## Performance Challenges

This application introduces two major performance challenges that require investigation and optimization:

1. Article Similarity Challenge with the `/articles/similar/{articleId}` API endpoint. This endpoint fetches three
   articles most similar to the one provided, but due to inefficient operations, performance is heavily impacted.
2. User Score Challenge with the `/users/score` API endpoint. This endpoint generates user scores based on their
   activity. However, there is a memory leak causing this endpoint to eventually throw an OutOfMemoryException.