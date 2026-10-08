package com.dyad.parabank.tests;

import com.aventstack.extentreports.Status;
import com.dyad.parabank.pages.AccountActivityPage;
import com.dyad.parabank.pages.AccountsOverviewPage;
import com.dyad.parabank.pages.HomePage;
import com.dyad.parabank.pages.OpenAccountPage;
import com.dyad.parabank.pages.RegistrationPage;
import com.dyad.parabank.pages.TransferFundsPage;
import com.dyad.parabank.utils.BaseTest;
import com.dyad.parabank.utils.DriverFactory;
import com.dyad.parabank.utils.ReportManager;
import com.dyad.parabank.utils.TestData;
import com.dyad.parabank.utils.TestData.RegistrationData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class FundTransferTest extends BaseTest {

    @Test(description = "Register customer, open account, transfer funds and verify balance and transaction history")
    public void verifyFundTransferEndToEnd() {
        RegistrationData customer = TestData.registration();
        log.info("Registering unique customer: {}", customer.username());
        ReportManager.log("Registering unique customer: " + customer.username());

        HomePage home = new HomePage(DriverFactory.get());
        RegistrationPage registration = home.clickRegister();
        AccountsOverviewPage overview = registration.register(customer);

        String sourceAccount = overview.getFirstAccountId();
        Assert.assertFalse(sourceAccount.isBlank(), "Registration should create an initial account");
        log.info("Initial account discovered dynamically: {}", sourceAccount);
        ReportManager.log("Initial account discovered: " + sourceAccount);

        OpenAccountPage openAccount = overview.openNewAccount();
        String destinationAccount = openAccount.openSavingsAccount(sourceAccount);
        Assert.assertFalse(destinationAccount.isBlank(), "A new account ID should be generated");
        Assert.assertNotEquals(destinationAccount, sourceAccount, "New account must differ from source account");
        log.info("New savings account opened dynamically: {}", destinationAccount);
        ReportManager.log("New savings account opened: " + destinationAccount);

        overview = new AccountsOverviewPage(DriverFactory.get());
        BigDecimal balanceBefore = overview.getBalance(sourceAccount);
        Assert.assertTrue(balanceBefore.compareTo(BigDecimal.ZERO) >= 0, "Balance before transfer must be readable");
        log.info("Balance before transfer for {}: {}", sourceAccount, balanceBefore);
        ReportManager.log("Balance before transfer: $" + balanceBefore);

        BigDecimal transferAmount = BigDecimal.valueOf(TestData.transferAmount());
        Assert.assertTrue(balanceBefore.compareTo(transferAmount) >= 0,
                "Source account must have sufficient balance for the transfer");

        TransferFundsPage transferFunds = overview.openTransferFunds();
        transferFunds.transfer(transferAmount.toPlainString(), sourceAccount, destinationAccount);
        Assert.assertTrue(transferFunds.isTransferSuccessful(), "ParaBank should confirm the transfer as complete");
        log.info("Transfer completed: {} -> {} amount {}", sourceAccount, destinationAccount, transferAmount);
        ReportManager.log("Transfer completed: $" + transferAmount);

        overview = new AccountsOverviewPage(DriverFactory.get());
        BigDecimal balanceAfter = overview.getBalance(sourceAccount);
        BigDecimal expectedBalance = balanceBefore.subtract(transferAmount);
        log.info("Balance after transfer: {}, expected: {}", balanceAfter, expectedBalance);
        ReportManager.log("Balance after transfer: $" + balanceAfter + "; expected: $" + expectedBalance);

        Assert.assertEquals(balanceAfter.compareTo(expectedBalance), 0,
                "Source account balance must decrease exactly by the transferred amount");

        AccountActivityPage sourceActivity = overview.openAccountActivity(sourceAccount);
        Assert.assertTrue(sourceActivity.hasTransferTransaction(transferAmount),
                "Source account transaction history must contain the transfer amount");
        ReportManager.log("Source transaction history contains the transfer");

        overview = new AccountsOverviewPage(DriverFactory.get());
        AccountActivityPage destinationActivity = overview.openAccountActivity(destinationAccount);
        Assert.assertTrue(destinationActivity.hasTransferTransaction(transferAmount),
                "Destination account transaction history must contain the transfer amount");
        ReportManager.log("Destination transaction history contains the transfer");
    }
}
