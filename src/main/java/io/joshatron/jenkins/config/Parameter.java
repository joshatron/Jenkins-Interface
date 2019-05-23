package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter {
    private String name;
    private ParameterType type;
    private String defaultValue;
    private String argName;
}
