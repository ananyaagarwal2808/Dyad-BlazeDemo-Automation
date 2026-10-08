package com.dyad.parabank.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class TestData {
    private TestData() {}
    public static int transferAmount() { return Integer.parseInt(System.getProperty("transferAmount", "100")); }
    public static String uniqueUsername() {
        return "dyad" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
    public static RegistrationData registration() {
        String username = uniqueUsername();
        return new RegistrationData("Ananya", "Dyad", "1 Test Street", "Noida", "UP", "201301", "9999999999", "12345", username, "Test@123");
    }

    public record RegistrationData(String firstName, String lastName, String address, String city, String state,
                                   String zipCode, String phone, String ssn, String username, String password) {}
}
