# Coding Platform Backend

A servlet-based Java EE backend for a coding platform that integrates with Judge0 API and uses PostgreSQL.

## Setup

1. **Prerequisites**:
    - Java 17
    - Maven
    - Tomcat 10.1.x
    - PostgreSQL (local instance)
    - Judge0 API key (replace `YOUR_JUDGE0_API_KEY` in `Judge0Service.java`)

2. **PostgreSQL Setup**:
    - Install PostgreSQL and ensure it's running on `localhost:5432`.
    - Create a database:
      ```sql
      CREATE DATABASE codingplatform;