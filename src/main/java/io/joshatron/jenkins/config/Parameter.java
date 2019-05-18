package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Parameter {
    private String parameterName;
    private ParameterType parameterType;
    private String defaultValue;
    private String argName;
}
