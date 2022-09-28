package com.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import rp.com.google.common.io.BaseEncoding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoggingUtils {

	public static final Logger LOGGER = LogManager.getLogger("binary_data_logger");

//    private LoggingUtils() {
//        //statics only
//    }

	public void info(String message) {
		LOGGER.info(message);
	}

	public void info(int message) {
		LOGGER.info(message);
	}

	public void info(ArrayList<String> message) {
		LOGGER.info(message);
	}

	public void info(List<String> message) {
		LOGGER.info(message);
	}

	public void error(String message) {
		LOGGER.error(message);
	}

	public void error(Exception message) {
		LOGGER.error(message);
	}

    public void log(byte[] bytes, String message) {
//        LOGGER.info("RP_MESSAGE#BASE64#{}#{}", BaseEncoding.base64().encode(bytes), message);
    }

	public void logBase64(String base64, String message) {
		LOGGER.info(base64, message);
	}

	public void log(File file, String message) {
		LOGGER.info("RP_MESSAGE#FILE#{}#{}", file.getAbsolutePath(), message);
	}
}
