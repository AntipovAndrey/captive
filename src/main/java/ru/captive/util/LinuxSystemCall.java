package ru.captive.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.captive.service.SystemCall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class LinuxSystemCall implements SystemCall {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxSystemCall.class);

    public String call(String query) {
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
