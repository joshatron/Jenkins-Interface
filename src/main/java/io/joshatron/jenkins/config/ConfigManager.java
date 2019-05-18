package io.joshatron.jenkins.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static final String defaultFile = "jenkins-config.json";

    public ConfigManager() {
        throw new IllegalStateException("This is a utility class");
    }

    public static JenkinsConfig importConfig() throws IOException {
        return importConfig(defaultFile);
    }

    public static JenkinsConfig importConfig(String configFile) throws IOException {
        File file = new File(configFile);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, JenkinsConfig.class);
    }

    public static void exportConfig(JenkinsConfig config) throws IOException {
        exportConfig(config, defaultFile);
    }

    public static void exportConfig(JenkinsConfig config, String configFile) throws IOException {
        File file = new File(configFile);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, config);
    }
}
