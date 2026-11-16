# Rideze Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven)
![REST Assured](https://img.shields.io/badge/REST_Assured-API_Testing-009688)
![TestNG](https://img.shields.io/badge/TestNG-7.x-red)
![Mobile Testing](https://img.shields.io/badge/Mobile_Testing-Automated-662D91)

A modular, multi-layer **Hybrid Test Automation Framework** built for the **Rideze** mobility and fleet booking platform (`rideze-automation`)[cite: 3]. Engineered with **Java 17** and **Maven**[cite: 3], the framework validates **Web UI**, **Mobile**, **REST APIs**, and **Security** across booking, pricing, fleet management, and customer review workflows using a structured Page Object Model (POM) and TestNG suite orchestration[cite: 3, 4].

---

## Key Highlights

- **Multi-Channel Coverage (Web, Mobile & API):** Combines desktop web UI automation (`com.rideze.pages`, `com.rideze.tests`)[cite: 3], dedicated mobile test execution (`com.rideze.mobile`, `testng-mobile.xml`)[cite: 3, 4], and backend API validation (`com.rideze.api`)[cite: 3].
- **Domain-Driven Page Object Architecture:** Modular POM packages segmented by business capability—**Booking** (`pages.booking`), **Fleet** (`pages.fleet`), and **Reviews** (`pages.reviews`)[cite: 3].
- **Pricing Engine & Security Validation:** Dedicated test coverage for ride fare/pricing rules, booking flows (`tests.booking`), fleet operations (`tests.fleet`), and security/authorization boundaries (`tests.security`)[cite: 3].
- **Automated Reporting & Failure Artifacts:** Captures execution reports (`reports/`), failure screenshots (`screenshots/`), and TestNG output logs (`test-output/`) for rapid defect triage[cite: 3, 4].

---

## Project Architecture

```text
rideze-automation/
├── reports/                               # Generated HTML & execution test reports[cite: 3, 4]
├── screenshots/                           # Failure & verification screenshots[cite: 3, 4]
├── src/
│   ├── main/
│   │   ├── java/com/rideze/
│   │   │   ├── api/                       # REST API clients, endpoints & payload models[cite: 3]
│   │   │   ├── config/                    # Environment & property configuration loader[cite: 3]
│   │   │   ├── driver/                    # WebDriver & mobile driver lifecycle factory[cite: 3]
│   │   │   ├── pages/
│   │   │   │   ├── booking/               # Ride booking, fare calculation & checkout Page Objects[cite: 3]
│   │   │   │   ├── fleet/                 # Vehicle & fleet management Page Objects[cite: 3]
│   │   │   │   └── reviews/               # Customer ratings & review Page Objects[cite: 3]
│   │   │   └── utils/                     # Data readers, wait helpers & screenshot utilities[cite: 3]
│   │   └── resources/
│   │       └── qa.properties              # QA environment URLs, credentials & runtime configs[cite: 3]
│   └── test/
│       └── java/com/rideze/
│           ├── api/                       # REST API contract & pricing engine tests[cite: 3]
│           ├── mobile/                    # Mobile web/app functional test suites[cite: 3]
│           └── tests/
│               ├── booking/               # Ride booking & fare calculation UI tests[cite: 3]
│               ├── fleet/                 # Fleet listing, availability & management tests[cite: 3]
│               └── security/              # Authentication, session & security validation tests[cite: 3]
├── test-output/                           # TestNG execution logs & XML results[cite: 3, 4]
├── pom.xml                                # Maven dependencies & build configuration[cite: 3, 4]
├── testng.xml                             # Primary Web & API TestNG regression suite[cite: 4]
├── testng-mobile.xml                      # Dedicated Mobile TestNG suite[cite: 4]
└── README.md

```

---

## Automated Test Coverage

* **Ride Booking & Pricing (`tests.booking` & `api`):** End-to-end trip booking flows across airport transfers, outstation rides, city tours, daily allowances, loyalty rewards, and dynamic pricing calculations.


* **Fleet Operations (`tests.fleet`):** Vehicle category filtering, availability checks, driver/fleet assignment, and status updates.


* **Customer Reviews (`pages.reviews`):** Post-ride rating submissions, feedback validation, and review display checks.


* **Security & Access Control (`tests.security`):** Role permissions, session integrity, input sanitization, and unauthorized endpoint protection.


* **Mobile Execution (`mobile` & `testng-mobile.xml`):** Responsive and mobile-specific user journeys executed independently via `testng-mobile.xml`.



---

## Prerequisites & Local Setup

* **Java JDK 17** (`JavaSE-17`) installed and configured in `JAVA_HOME`.


* **Apache Maven 3.8+** installed.

### Clone & Build

```bash
git clone [https://github.com/DarshanVishu/rideze-automation.git](https://github.com/DarshanVishu/rideze-automation.git)
cd rideze-automation
mvn clean compile test-compile

```

---

## Running Tests Locally

### 1. Run Primary Regression Suite (`testng.xml`)

```bash
mvn clean test -DsuiteXmlFile=testng.xml

```

### 2. Run Mobile Test Suite (`testng-mobile.xml`)

```bash
mvn clean test -DsuiteXmlFile=testng-mobile.xml

```

### 3. Run Specific Package or Test Suites

```bash
# Run Booking tests
mvn test -Dtest="com.rideze.tests.booking.*"

# Run Fleet tests
mvn test -Dtest="com.rideze.tests.fleet.*"

# Run Security tests
mvn test -Dtest="com.rideze.tests.security.*"

# Run API tests
mvn test -Dtest="com.rideze.api.*"

```

---

## Author

**Darshan Vishu** — QA Automation | Software Development Engineer in Test - SDET

* GitHub: [@DarshanVishu](https://github.com/DarshanVishu)

```

```
