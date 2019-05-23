package io.joshatron.jenkins;

import io.joshatron.jenkins.config.ConfigManager;
import io.joshatron.jenkins.config.JenkinsConfig;
import io.joshatron.jenkins.operations.Arguments;
import io.joshatron.jenkins.operations.Builder;
import io.joshatron.jenkins.operations.Importer;

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
                    System.out.println("Building specified jobs");
                    if(arguments.hasArgument("tags")) {
                        Builder.buildJobs(config, Arrays.asList(arguments.getArgument("tags").split(" ")), arguments);
                    }
                    else {
                        Builder.buildJobs(config, arguments);
                    }
                    break;
                case "import":
                    System.out.println("Importing jobs");
                    Importer.importJobsFromFolder(config, arguments);
                    break;
                default:
                    System.out.println("Invalid command");
            }

            ConfigManager.exportConfig(config);
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
}
