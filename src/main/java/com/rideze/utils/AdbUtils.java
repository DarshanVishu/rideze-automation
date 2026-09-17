package com.rideze.utils;

import java.io.File;
import java.io.IOException;

public class AdbUtils {

    private static final String ADB_PATH = resolveAdbPath();

    private static String resolveAdbPath() {
        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData != null) {
            String defaultSdkAdb = localAppData + "\\Android\\Sdk\\platform-tools\\adb.exe";
            if (new File(defaultSdkAdb).exists()) {
                return defaultSdkAdb;
            }
        }
        return "adb";
    }

    public static void executeCommand(String commandArgs) {
        try {
            String fullCommand = "\"" + ADB_PATH + "\" " + commandArgs;
            Process process = Runtime.getRuntime().exec(fullCommand);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing ADB command: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void inputText(String text) {
        executeCommand("shell input text " + text);
    }
}