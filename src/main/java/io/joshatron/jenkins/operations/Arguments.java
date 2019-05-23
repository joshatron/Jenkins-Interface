package io.joshatron.jenkins.operations;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class Arguments {
    private HashMap<String, String> args;

    public Arguments() {
        args = new HashMap<>();
    }

    public void addArgument(String argument, String value) {
        args.put(argument, value);
    }

    public void removeArgument(String argument) {
        if(args.containsKey(argument)) {
            args.remove(argument);
        }
    }

    public boolean hasArgument(String name) {
        return args.containsKey(name);
    }

    public String getArgument(String name) {
        return args.get(name);
    }
}
