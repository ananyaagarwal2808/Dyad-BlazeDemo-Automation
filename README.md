# Dyad Technologies - BlazeDemo QA Automation

## 1. Overview

This project automates the end-to-end BlazeDemo flight-booking scenario requested in the Dyad Technologies QA Automation Practical Task.

Application under test: https://blazedemo.com/

### Automated business flow

1. Open BlazeDemo.
2. Select Boston as departure city.
3. Select London as destination city.
4. Search for flights.
5. Verify that at least one flight is available.
6. Capture airline, flight number, departure time, arrival time and price for every flight.
7. Identify the cheapest flight dynamically.
8. Select the cheapest flight.
9. Enter passenger and dummy payment details.
10. Purchase the flight.
11. Verify the confirmation page.
12. Capture Booking ID, Amount and Card Number.
13. Validate Booking ID is generated.
14. Validate confirmation amount equals the selected flight price.

## 2. Technology Stack

- Java 17
- Selenium WebDriver 4.35.0
- TestNG 7.11.0
- Maven
- ExtentReports 5.1.2
- Log4j2 2.25.2
- Page Object Model (POM)

Selenium Manager is used through Selenium WebDriver, so a separate ChromeDriver executable is not required for normal local execution.

## 3. Framework Design

```text
src/main/java/com/dyad/blazedemo/
├── base/
│   └── BasePage.java
├── driver/
│   └── DriverFactory.java
├── models/
│   └── Flight.java
├── pages/
│   ├── HomePage.java
│   ├── FlightResultsPage.java
│   ├── PurchasePage.java
│   └── ConfirmationPage.java
├── reporting/
│   ├── ExtentManager.java
│   ├── ExtentTestManager.java
│   └── TestListener.java
└── utils/
    ├── ConfigReader.java
    └── TestData.java

src/test/java/com/dyad/blazedemo/tests/
└── FlightBookingTest.java
```

### Why POM?

Page-specific locators and interactions are isolated from business-level test logic. This makes the test easier to read and reduces maintenance when the UI changes.

### Why a Flight model?

A `Flight` record represents the data captured from each dynamic result row. It keeps airline, flight number, times, price and source row together and makes cheapest-flight selection straightforward.

### Why BigDecimal for prices?

Flight prices represent currency. `BigDecimal` avoids the floating-point precision issues that can occur with `double`.

## 4. Dynamic Cheapest-Flight Logic

The framework reads every flight row from the results table and converts each price into a `BigDecimal`. The cheapest flight is selected using:

```java
flights.stream()
       .min(Comparator.comparing(Flight::price))
```

The test also independently calculates the minimum price and asserts that it matches the selected flight's price. The flight is therefore not hardcoded by row number or price.

## 5. Wait Strategy

The framework uses Selenium `WebDriverWait` and expected conditions. No `Thread.sleep()` is used.

Reusable methods are provided by `BasePage` for:

- visibility waits
- clickable waits
- text entry
- clicks

## 6. Configuration

Configuration is in:

`src/test/resources/config.properties`

Default values:

```properties
baseUrl=https://blazedemo.com/
browser=chrome
headless=false
explicitWaitSeconds=15
```

System properties can override these values without changing source code. For example:

```bash
mvn clean test -Dheadless=true
```

## 7. Prerequisites

- JDK 17 or higher
- Maven 3.8+
- Google Chrome
- Internet access

Verify installations:

```bash
java -version
mvn -version
```

## 8. Run the Test

From the project root:

```bash
mvn clean test
```

Headless execution:

```bash
mvn clean test -Dheadless=true
```

Run the TestNG suite explicitly:

```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng.xml
```

## 9. Reports and Logs

After execution:

- Extent report: `reports/ExtentReport.html`
- Failure screenshots: `reports/screenshots/`
- Log file: `logs/automation.log`

The Extent report contains the dynamic flight list, selected cheapest flight, booking ID, booking amount and validation results.

## 10. Validation Criteria Covered

| # | Requirement | Implementation |
|---|---|---|
| 1 | Minimum one flight is available | TestNG assertion against parsed flight list |
| 2 | Cheapest flight identified correctly | Dynamic minimum-price calculation + assertion |
| 3 | Booking ID is generated | Non-blank TestNG assertion |
| 4 | Amount matches selected flight price | `BigDecimal` equality assertion |

## 11. Test Data and Security

Only dummy/demo payment data is used. No real credentials, API keys or personal payment information should be committed to GitHub.

The card number used by this project is test data for the public BlazeDemo demo site. The framework also masks card numbers in execution logs/reports.

## 12. Assumptions

- BlazeDemo remains publicly accessible during execution.
- The Boston-to-London search returns at least one flight.
- The public demo site accepts its standard test payment data.
- The confirmation page exposes booking details in the standard BlazeDemo confirmation table.
- Chrome is the target browser for this assessment.

## 13. AI Usage

AI tools may be used as permitted by the assessment instructions. All submitted code should be reviewed and understood by the candidate before submission. The candidate should be able to explain the framework structure, locators, synchronization strategy, dynamic price calculation, assertions, reporting and test lifecycle.

## 14. Suggested Git Commit History

Use incremental commits rather than one bulk commit:

```text
Initial Maven project setup
Add WebDriver and TestNG configuration
Implement DriverFactory and BasePage
Add BlazeDemo HomePage POM
Add FlightResultsPage POM
Implement dynamic flight extraction and cheapest-flight logic
Add PurchasePage and test data
Add ConfirmationPage validations
Add Extent reporting and failure screenshots
Add Log4j2 structured logging
Add README documentation
Refactor and clean framework
```

## 15. Interview Discussion Points

Be prepared to explain:

- Why POM is used.
- Why `ThreadLocal<WebDriver>` is used.
- Why explicit waits are preferred over hardcoded sleeps.
- Why `BigDecimal` is used for currency.
- How the cheapest flight is found dynamically.
- How the selected flight remains linked to the original result row.
- How booking amount is compared with the selected price.
- How failure screenshots are captured.
- How the framework could be extended for Firefox/Edge and parallel execution.
