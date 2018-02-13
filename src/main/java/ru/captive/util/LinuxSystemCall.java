package ru.captive.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxSystemCall {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxSystemCall.class);

    private String query;

    public LinuxSystemCall(String query) {
        this.query = query;
    }

    public String call() {
        String[] bashQuery = {
                "/bin/bash",
                "-c",
                query};

        String line = "";
        String lineToReturn = "";
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(bashQuery);
            p.waitFor();

            try (BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while ((line = b.readLine()) != null) {
                    lineToReturn = line;
                }
            } catch (IOException e) {
                LOGGER.error("Error reading output ", e);
            }

        } catch (Exception e) {
            LOGGER.error("Error execution linux call ", e);
        }
        return lineToReturn;
    }
}
