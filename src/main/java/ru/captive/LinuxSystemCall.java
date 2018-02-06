package ru.captive;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LinuxSystemCall {

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
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = b.readLine()) != null) {
                lineToReturn = line;
            }

            b.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lineToReturn;
    }
}
