package io.joshatron.jenkins;

import io.joshatron.jenkins.config.ConfigManager;
import io.joshatron.jenkins.config.JenkinsConfig;
import io.joshatron.jenkins.operations.Arguments;
import io.joshatron.jenkins.operations.Builder;

import java.util.Arrays;

public class App
{
    public static void main(String[] args) {
        try {
            String command = args[0];
            Arguments arguments = getArgumentsFromCliArgs(args);
            JenkinsConfig config = ConfigManager.importConfig();

            switch(command) {
                case "build":
                    config = reduceConfig(config, arguments);
                    Builder.buildJobs(config, arguments);
                    break;
            }
        } catch (Exception e) {
            System.out.println("An exception occurred, exiting");
            e.printStackTrace();
        }
    }

    private static Arguments getArgumentsFromCliArgs(String[] args) {
        Arguments arguments = new Arguments();

        String currentName = "";
        StringBuilder currentValue = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("--")) {
                if(!currentName.isEmpty()) {
                    arguments.addArgument(currentName, currentValue.toString());
                }

                currentName = args[i].substring(2);
                currentValue = new StringBuilder();
            }
            else {
                if(currentValue.length() > 0) {
                    currentValue.append(" ");
                }
                currentValue.append(args[i]);
            }
        }

        if(!currentName.isEmpty()) {
            arguments.addArgument(currentName, currentValue.toString());
        }

        return arguments;
    }

    private static JenkinsConfig reduceConfig(JenkinsConfig config, Arguments arguments) {
        if(arguments.hasArgument("tags")) {
            config = config.getJobsWithTags(Arrays.asList(arguments.getArgument("tags").split(" ")));
            arguments.removeArgument("tags");
        }

        return config;
    }
}
