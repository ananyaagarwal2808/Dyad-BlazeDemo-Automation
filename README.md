# Dyad Technologies – ParaBank Fund Transfer Automation

A runnable end-to-end UI automation project for **Scenario 3: Banking Fund Transfer** from the Dyad Technologies QA Automation practical task.

## Stack

- Java 17
- Selenium WebDriver 4.27
- TestNG 7.10
- Maven
- Page Object Model (POM)
- Log4j2 structured execution logging
- ExtentReports HTML reporting
- Selenium Manager for browser-driver management

## Scenario Automated

1. Register a new ParaBank customer with a dynamically generated username.
2. Discover the account created by registration instead of hardcoding an account ID.
3. Open a new SAVINGS account using the discovered source account.
4. Capture the source account balance **before** transfer.
5. Transfer a configurable test amount (`100.00` by default) between the two discovered accounts.
6. Capture the source account balance **after** transfer.
7. Assert that the transfer completed successfully.
8. Assert that `balanceAfter = balanceBefore - transferAmount`.
9. Assert that the source account transaction history contains the transfer amount.
10. Assert that the destination account transaction history contains the transfer amount.

## Project Structure

```text
src/test/java/com/dyad/parabank/
├── pages/
│   ├── HomePage.java
│   ├── RegistrationPage.java
│   ├── AccountsOverviewPage.java
│   ├── OpenAccountPage.java
│   ├── TransferFundsPage.java
│   └── AccountActivityPage.java
├── tests/
│   └── FundTransferTest.java
└── utils/
    ├── BaseTest.java
    ├── Config.java
    ├── DriverFactory.java
    ├── ReportManager.java
    ├── TestData.java
    └── Waits.java
src/test/resources/log4j2.xml
testng.xml
pom.xml
README.md
.gitignore
```

## Prerequisites

- JDK 17+
- Maven 3.9+
- Google Chrome (default) or Firefox
- Internet access to the public ParaBank demo site

Selenium Manager automatically resolves the compatible browser driver, so no manual `chromedriver.exe` path is required.

## Run from IntelliJ IDEA

1. Open the project as a Maven project.
2. Allow IntelliJ to import dependencies from `pom.xml`.
3. Make sure Project SDK is Java 17 or newer.
4. Right-click `testng.xml` and select **Run**.

You can also right-click `FundTransferTest.java` and run the TestNG test directly.

## Run from terminal

```bash
mvn clean test
```

Headless Chrome:

```bash
mvn clean test -Dheadless=true
```

Firefox:

```bash
mvn clean test -Dbrowser=firefox
```

Custom transfer amount:

```bash
mvn clean test -DtransferAmount=50
```

> Note: the default test amount is defined in `TestData.java`. The project is intentionally using a deterministic small amount instead of relying on a hardcoded expected account balance.

## Reports and Logs

After execution:

- Extent report: `target/reports/ParaBank-Test-Report.html`
- Execution log: `target/logs/test-execution.log`
- Failure screenshots: `target/screenshots/`
- TestNG/Surefire results: `target/surefire-reports/`

## Synchronization Strategy

No `Thread.sleep()` is used. The framework uses Selenium `WebDriverWait` with explicit waits for visibility, clickability and URL state. Dynamic AJAX content is waited on through the relevant UI elements.

## Dynamic Data Strategy

- Username is generated using the current timestamp, reducing collision risk on the shared demo environment.
- Source account ID is discovered from the Accounts Overview page.
- Destination account ID is returned by the Open New Account workflow.
- Before/after balances are read from the live UI and compared mathematically.
- Transaction verification searches the live transaction table for the transferred amount rather than asserting a hardcoded transaction ID.

## Assumptions

- The public ParaBank demo environment is available during execution.
- Registration creates an initial customer account.
- The newly opened savings account can be funded from the initial account.
- The source account has enough funds for the configured transfer amount.
- The public demo environment may occasionally be slower or temporarily unavailable; waits are therefore explicit and configurable.

## Git Submission

Recommended incremental commits:

```text
git init
git add pom.xml testng.xml .gitignore
git commit -m "Initialize Maven TestNG automation framework"
git add src/test/java/com/dyad/parabank/utils src/test/resources
git commit -m "Add WebDriver utilities logging and reporting"
git add src/test/java/com/dyad/parabank/pages
git commit -m "Add ParaBank page objects for banking workflow"
git add src/test/java/com/dyad/parabank/tests
git commit -m "Implement end-to-end fund transfer validation"
git add README.md
git commit -m "Add project documentation and execution instructions"
```

Then connect your GitHub repository and push:

```bash
git branch -M main
git remote add origin <YOUR_GITHUB_REPOSITORY_URL>
git push -u origin main
```

Do not commit real credentials, API keys, or sensitive banking data.
