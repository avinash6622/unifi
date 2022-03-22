package com.bcad.application.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties("fee.file.folder")
public class PropertiesUtils {

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);

    public PropertiesConfiguration getProperties(String sFileName) {
        PropertiesConfiguration config = null;

        //final String PROPERTIES_FILE_NAME = System.getenv("DFA_PROPERTIES") + "\\" + sFileName;
        final String PROPERTIES_FILE_NAME = "D:\\unify"+ "\\" + sFileName;
        try {
            config = new PropertiesConfiguration();
            System.out.println("++++++++++");
            config.load(PROPERTIES_FILE_NAME);
            log.info("Success at getProperties no return");

        } catch (Exception e) {
            log.error("Exception at getProperties no return" + e);
        }
        return config;
    }


}
