package io.joshatron.jenkins;

import io.joshatron.jenkins.operations.Arguments;

public class App
{
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    private Arguments getArgumentsFromCliArgs(String[] args) {
        Arguments arguments = new Arguments();

        String currentName = "";
        StringBuilder currentValue = new StringBuilder();
        for(int i = 0; i < args.length; i++) {
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
