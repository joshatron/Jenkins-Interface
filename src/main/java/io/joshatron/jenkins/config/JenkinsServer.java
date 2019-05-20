package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class JenkinsServer {
    private String name;
    private String baseUrl;
    private String username;
    private String password;
    private ArrayList<JenkinsJob> jobs;

    public void addJob(JenkinsJob jenkinsJob) {
        jobs.add(jenkinsJob);
    }

    public void removeJob(String jobName) {
        for(JenkinsJob job : jobs) {
            if(job.getName().equalsIgnoreCase(jobName)) {
                jobs.remove(job);
                break;
            }
        }
    }
}
