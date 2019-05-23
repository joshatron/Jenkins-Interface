package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Base64;

@Data
@AllArgsConstructor
public class JenkinsServer {
    private String name;
    private String baseUrl;
    private String auth;
    private ArrayList<JenkinsJob> jobs;

    public JenkinsServer(String name, String baseUrl, String username, String token) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.setAuth(username, token);
        this.jobs = new ArrayList<>();
    }

    public JenkinsServer(String name, String baseUrl, String auth) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.auth = auth;
        this.jobs = new ArrayList<>();
    }

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

    public JenkinsJob getJob(String jobName) {
        for(JenkinsJob job : jobs) {
            if(job.getName().equalsIgnoreCase(jobName)) {
                return job;
            }
        }

        return null;
    }

    public void setAuth(String username, String token) {
        auth = "Basic " + Base64.getEncoder().encodeToString((username + ":" + token).getBytes());
    }
}
