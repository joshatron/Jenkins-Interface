package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class JenkinsServer {
    private String baseUrl;
    private String username;
    private String password;
    private ArrayList<Job> jobs;
}
