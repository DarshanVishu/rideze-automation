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
