# Rideze Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.20.0-green.svg)](https://www.selenium.dev/)
[![Appium](https://img.shields.io/badge/Appium-9.2.2-purple.svg)](https://appium.io/)
[![REST-Assured](https://img.shields.io/badge/REST--Assured-5.4.0-blue.svg)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red.svg)](https://testng.org/)

An enterprise-grade, cross-platform test automation framework built for the
**Rideze** mobility ecosystem. It automates testing across **Web Portals (Admin
& Customer)**, the **Driver Mobile App (Android / Expo)**, and core **Backend
REST APIs & Security Rules**.

---

## 🎯 Tech Stack & Architecture

- **Language**: Java 17
- **Web UI Engine**: Selenium WebDriver 4.20.0 (Page Object Model)
- **Mobile Engine**: Appium Java Client 9.2.2 (UiAutomator2, Android, Expo)
- **API Engine**: REST Assured 5.4.0
- **Test Runner**: TestNG 7.9.0
- **Build Management**: Apache Maven 3.8+
- **Reporting**: TestNG & ExtentReports with automated failure screenshots

---

## 🛠 Project Structure

```text
rideze-automation/
├── src/
│   ├── main/
│   │   ├── java/com/rideze/
│   │   │   ├── driver/             # ThreadSafe WebDriver & Appium Manager
│   │   │   ├── pages/              # Web & Mobile Page Objects (POM)
│   │   │   └── utils/              # ADB helpers, wait utilities, property loaders
│   │   └── resources/
│   │       └── qa.properties       # Environment configuration & test data
│   └── test/
│       ├── java/com/rideze/
│       │   ├── mobile/             # Mobile launch checks
│       │   └── tests/              # UI, Mobile Lifecycle, API, and Security suites
│       └── resources/
├── testng.xml                      # Full end-to-end regression runner (18 tests)
├── testng-mobile.xml               # Dedicated mobile automation suite
└── pom.xml                         # Maven dependencies & build configuration
```

Prerequisites JDK: OpenJDK 17 or higher (java -version)

Build Tool: Apache Maven 3.8+ (mvn -version)

Browser: Google Chrome Stable

Mobile Engine (for Driver App tests):

Node.js v18+ & Appium 2.x

UiAutomator2 driver (appium driver install uiautomator2)

Android SDK & running Emulator / Device via ADB

Configuration Test properties are managed via src/main/resources/qa.properties:

# Web Application URLs

baseUrl=http://localhost:3000/admin/login customerUrl=http://localhost:3000

# Mobile Device & App Capabilities

deviceName=emulator-5554 platformName=Android automationName=UiAutomator2
appPackage=host.exp.exponent appActivity=.LauncherActivity

Test Execution

1. Run Complete Master Regression (Web + Mobile + API + Security) mvn clean test
   -DsuiteXmlFile=testng.xml

2. Run Headless Web Execution (CI/CD Ready) mvn clean test
   -DsuiteXmlFile=testng.xml -Dheadless=true

3. Run Dedicated Mobile Suite mvn clean test -DsuiteXmlFile=testng-mobile.xml

4. Run via IDE (Eclipse / IntelliJ) Right-click testng.xml $\rightarrow$ Run As
   $\rightarrow$ TestNG Suite.

Test Coverage Summary (18/18 Tests Passing) Admin Authentication (LoginTest)

Valid login session creation, invalid credentials rejection, login page
rendering.

Fleet Management (VehiclesTest)

Vehicle addition, vehicle search filtering, empty field validation, license
registration format checks.

Customer & Driver Reviews (ReviewsTest)

Review list rendering, keyword filtering, content moderation and review
flagging.

Customer Booking Operations (CustomerBookingTest)

Customer dashboard navigation, Airport Transfer route entry, Outstation route
setup, City Sightseeing package selection.

Driver Mobile Lifecycle (DriverDutyStatusTest / DriverShiftStatusTest)

Duty status toggling, check-out required modal handling, 4-angle vehicle
inspection verification (Front, Rear, Left, Right), odometer entry, and fuel
percentage logging.

Toll Operations API (TollApiTests)

GET /api/tolls/pending endpoint verification and payload schema validation.

Security & Multi-Tenancy (SecurityTests)

Cross-tenant booking authorization (IDOR protection), tampered Razorpay payment
signature rejection.

Author Darshan Vishwakarma

GitHub: @DarshanVishu

Project: rideze-automation Author Darshan Vishwakarma

GitHub: @DarshanVishu That older `README.md` is outdated because it misses the
major capabilities you just built and stabilized:

- **Missing Mobile Suite:** It has no mention of Appium, UiAutomator2, or the
  Android Expo driver app.
- **Outdated Test Count:** It lists 15 tests, but your regression suite now runs
  **18/18 tests**.
- **Missing Security Coverage:** It omits IDOR multi-tenant booking isolation
  and Razorpay signature tampering verification.
- **Missing Vehicle Lifecycle:** It skips the entire vehicle check-out workflow
  (4-angle inspection, odometer reading, fuel telemetry).
- **Copy-Paste Artifacts:** The bottom contains instructions meant for the chat
  (`3. Press Ctrl + S to save the file...`) rather than documentation.

Replace the contents of `README.md` with this updated, comprehensive version:

````markdown
# Rideze Test Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.20.0-green.svg)](https://www.selenium.dev/)
[![Appium](https://img.shields.io/badge/Appium-9.2.2-purple.svg)](https://appium.io/)
[![REST-Assured](https://img.shields.io/badge/REST--Assured-5.4.0-blue.svg)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red.svg)](https://testng.org/)

An enterprise-grade, cross-platform test automation framework built for the
**Rideze** mobility ecosystem. It automates testing across **Web Portals (Admin
& Customer)**, the **Driver Mobile App (Android / Expo)**, and core **Backend
REST APIs & Security Rules**.

---

## 🎯 Tech Stack & Architecture

- **Language**: Java 17
- **Web UI Engine**: Selenium WebDriver 4.20.0 (Page Object Model)
- **Mobile Engine**: Appium Java Client 9.2.2 (UiAutomator2, Android, Expo)
- **API Engine**: REST Assured 5.4.0
- **Test Runner**: TestNG 7.9.0
- **Build Management**: Apache Maven 3.8+
- **Reporting**: TestNG & ExtentReports with automated failure screenshots

---

## 🛠 Project Structure

```text
rideze-automation/
├── src/
│   ├── main/
│   │   ├── java/com/rideze/
│   │   │   ├── driver/             # ThreadSafe WebDriver & Appium Manager
│   │   │   ├── pages/              # Web & Mobile Page Objects (POM)
│   │   │   └── utils/              # ADB helpers, wait utilities, property loaders
│   │   └── resources/
│   │       └── qa.properties       # Environment configuration & test data
│   └── test/
│       ├── java/com/rideze/
│       │   ├── mobile/             # Mobile launch checks
│       │   └── tests/              # UI, Mobile Lifecycle, API, and Security suites
│       └── resources/
├── testng.xml                      # Full end-to-end regression runner (18 tests)
├── testng-mobile.xml               # Dedicated mobile automation suite
└── pom.xml                         # Maven dependencies & build configuration
```
````

---

## 📋 Prerequisites

- **JDK**: OpenJDK 17 or higher (`java -version`)
- **Build Tool**: Apache Maven 3.8+ (`mvn -version`)
- **Browser**: Google Chrome Stable
- **Mobile Engine (for Driver App tests)**:
- Node.js v18+ & Appium 2.x
- UiAutomator2 driver (`appium driver install uiautomator2`)
- Android SDK & running Emulator / Device via ADB

---

## ⚙️ Configuration

Test properties are managed via `src/main/resources/qa.properties`:

```properties
# Web Application URLs
baseUrl=http://localhost:3000/admin/login
customerUrl=http://localhost:3000

# Mobile Device & App Capabilities
deviceName=emulator-5554
platformName=Android
automationName=UiAutomator2
appPackage=host.exp.exponent
appActivity=.LauncherActivity
```

---

## 🚀 Test Execution

### 1. Run Complete Master Regression (Web + Mobile + API + Security)

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### 2. Run Headless Web Execution (CI/CD Ready)

```bash
mvn clean test -DsuiteXmlFile=testng.xml -Dheadless=true
```

### 3. Run Dedicated Mobile Suite

```bash
mvn clean test -DsuiteXmlFile=testng-mobile.xml
```

### 4. Run via IDE (Eclipse / IntelliJ)

Right-click `testng.xml` $\rightarrow$ **Run As** $\rightarrow$ **TestNG
Suite**.

---

## 📊 Test Coverage Summary (18/18 Tests Passing)

- **Admin Authentication** (`LoginTest`)
- Valid login session creation, invalid credentials rejection, login page
  rendering.

- **Fleet Management** (`VehiclesTest`)
- Vehicle addition, vehicle search filtering, empty field validation, license
  registration format checks.

- **Customer & Driver Reviews** (`ReviewsTest`)
- Review list rendering, keyword filtering, content moderation and review
  flagging.

- **Customer Booking Operations** (`CustomerBookingTest`)
- Customer dashboard navigation, Airport Transfer route entry, Outstation route
  setup, City Sightseeing package selection.

- **Driver Mobile Lifecycle** (`DriverDutyStatusTest` / `DriverShiftStatusTest`)
- Duty status toggling, check-out required modal handling, 4-angle vehicle
  inspection verification (Front, Rear, Left, Right), odometer entry, and fuel
  percentage logging.

- **Toll Operations API** (`TollApiTests`)
- `GET /api/tolls/pending` endpoint verification and payload schema validation.

- **Security & Multi-Tenancy** (`SecurityTests`)
- Cross-tenant booking authorization (IDOR protection), tampered Razorpay
  payment signature rejection.

---

## 👤 Author

**Darshan Vishwakarma**

- GitHub:
  [@DarshanVishu](https://www.google.com/search?q=https://github.com/DarshanVishu)
- Project:
  [rideze-automation](https://www.google.com/search?q=https://github.com/DarshanVishu/rideze-automation)

````
Save the file, then push the changes to both repositories:

```powershell
git add README.md
git commit -m "docs: modernize README with mobile appium, security tests, and architecture"
git push personal qa-automation-suite:main
git push origin qa-automation-suite
````
