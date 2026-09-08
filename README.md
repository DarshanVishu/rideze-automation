# Rideze Test Automation Framework

An enterprise-grade, end-to-end test automation framework built for the
**Rideze** platform. It automates both the Admin Web Portal and the Customer
Booking Portal, paired with backend API verification for toll management.

---

## Tech Stack & Architecture

- **Language**: Java 17
- **Core Automation Engine**: Selenium WebDriver 4.x
- **Test Runner**: TestNG 7.9.0
- **API Engine**: REST Assured
- **Build & Dependency Management**: Apache Maven
- **Design Pattern**: Page Object Model (POM) with thread-safe WebDriver
  management
- **Reporting**: ExtentReports 5 with automatic failure screenshot capture

---

## Project Structure

```text
rideze-automation/
├── src/
│   ├── main/
│   │   ├── java/com/rideze/
│   │   │   ├── base/               # Driver initialization & factory setup
│   │   │   ├── config/             # Configuration property reader
│   │   │   ├── pages/              # Page Object classes
│   │   │   │   ├── admin/          # Admin portal pages (Login, Fleet, Reviews)
│   │   │   │   └── booking/        # Customer pages (Login, Wizard flows)
│   │   │   └── utils/              # TestNG listeners, screenshot utilities
│   │   └── resources/
│   │       └── config.properties   # Environment & credential configuration
│   └── test/
│       └── java/com/rideze/
│           ├── api/                # Backend API tests (Tolls, Bookings)
│           └── tests/              # TestNG test classes (UI & E2E flows)
├── reports/                        # Generated HTML ExtentReports & screenshots
├── testng.xml                      # Master regression test suite runner
└── pom.xml                         # Maven build configuration

## Prerequisites
Ensure the following tools are installed locally and added to your system PATH:

 1.JDK 17 or higher (java -version)
 2.Apache Maven 3.8+ (mvn -version)
 3.Google Chrome (Current stable release)
 4.Rideze Local Dev Server running at http://localhost:3000

Configuration
Update src/main/resources/config.properties to target your local or staging environments:
# Base URLs
adminBaseUrl=http://localhost:3000/admin/login
customerBaseUrl=http://localhost:3000

# Customer Auth (OTP)
customerPhone=9876543210
customerOtp=123456

# Timeouts (in seconds)
implicitWait=3
explicitWait=10
pageLoadTimeout=30

## Test Execution
1. Run Complete Regression Suite via Maven
Execute all 15 regression test cases across UI and API modules in headless mode:
mvn clean test -Dheadless=true

To run with the interactive browser UI visible:
mvn clean test

2. Run from Eclipse or IntelliJ
Open testng.xml located in the root directory.
Right-click the file $\to$ select Run As $\to$ TestNG Suite.

## Test Coverage Summary

* **Admin Authentication** (`LoginTest`)
  Form validation, invalid credentials handling, session persistence

* **Fleet Management** (`VehiclesTest`, `VehiclesNegativeTest`)
  Add vehicle happy path, license plate validation, duplicate plate handling

* **Reviews & Feedback** (`ReviewsTest`)
  Search reviews, keyword filtering, content moderation and flagging

* **Customer Booking** (`CustomerBookingTest`)
  OTP authentication, Airport Transfers, Outstation Trips, City Sightseeing packages, Google Places Autocomplete handling

* **Toll Operations** (`TollApiTests`)
  GET /api/tolls/pending schema contract and response validation

##Test Reports & Failure Artifacts
 1.ExtentReports Dashboard: Found at reports/ExtentReport.html after test execution. Displays execution timelines, step logs, and environment configurations.

 2.Failure Screenshots: Automatically captured upon assertion failure or runtime exception, saved directly to reports/screenshots/ and embedded into the HTML report.

 3. Press **Ctrl + S** to save the file and close it.
 Let me know once you've saved it so we can verify git status and move to the next step.
```
