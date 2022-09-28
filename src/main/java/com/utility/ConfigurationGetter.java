package com.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationGetter {

    Properties prop = new Properties();
    InputStream inputStream = null;

    public Properties getPropValues() throws IOException {

        try {
            String fileName = "xray.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if (inputStream != null) {
                prop.load(inputStream);
                for (String propertyName : prop.stringPropertyNames()) {
                    String systemPropertyValue = System.getProperty(propertyName);

                    if (systemPropertyValue != null) {
                    	prop.setProperty(propertyName, systemPropertyValue);
                    }
                }
            } else {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }

        return prop;

    }
}
