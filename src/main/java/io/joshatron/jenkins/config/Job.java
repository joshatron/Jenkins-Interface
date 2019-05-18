package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Job {
    private String baseUrl;
    private String username;
    private String token;
    private ArrayList<String> tags;
    private ArrayList<Parameter> parameters;
}
