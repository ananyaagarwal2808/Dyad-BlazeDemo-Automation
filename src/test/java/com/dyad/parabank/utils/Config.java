package com.dyad.parabank.utils;

public final class Config {
    private Config() {}
    public static String baseUrl() { return value("baseUrl", "https://parabank.parasoft.com/parabank/index.htm"); }
    public static String browser() { return value("browser", "chrome"); }
    public static boolean headless() { return Boolean.parseBoolean(value("headless", "false")); }
    public static int waitSeconds() { return Integer.parseInt(value("waitSeconds", "15")); }
    private static String value(String key, String fallback) {
        String system = System.getProperty(key);
        if (system != null && !system.isBlank()) return system;
        String env = System.getenv(key.toUpperCase());
        return env == null || env.isBlank() ? fallback : env;
    }
}
