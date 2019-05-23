package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class JenkinsConfig {
    private ArrayList<JenkinsServer> servers;

    public JenkinsConfig() {
        servers = new ArrayList<>();
    }

    public JenkinsConfig getJobsWithTag(String tag) {
        ArrayList<String> tags = new ArrayList<>();
        tags.add(tag);
        return getJobsWithTags(tags);
    }

    public JenkinsConfig getJobsWithTags(List<String> tags) {
        JenkinsConfig jobs = new JenkinsConfig();

        for(JenkinsServer server : servers) {
            boolean added = false;
            for(JenkinsJob job : server.getJobs()) {
                boolean toAdd = true;
                for(String tag : tags) {
                    if (!job.getTags().contains(tag)) {
                        toAdd = false;
                        break;
                    }
                }

                if(toAdd) {
                    if (!added) {
                        jobs.addServer(new JenkinsServer(server.getName(), server.getBaseUrl(), server.getAuth()));
                        added = true;
                    }
                    jobs.addJob(server.getName(), job);
                }
            }
        }

        return jobs;
    }

    public void addServer(JenkinsServer server) {
        servers.add(server);
    }

    public void removeServer(String name) {
        for(JenkinsServer server : servers) {
            if(server.getName().equalsIgnoreCase(name)) {
                servers.remove(server);
                break;
            }
        }
    }

    public void addJob(String serverName, JenkinsJob job) {
        for(JenkinsServer server : servers) {
            if(server.getName().equalsIgnoreCase(serverName)) {
                server.addJob(job);
                break;
            }
        }
    }

    public void removeJob(String serverName, String jobName) {
        for(JenkinsServer server : servers) {
            if(server.getName().equalsIgnoreCase(serverName)) {
                server.removeJob(jobName);
                break;
            }
        }
    }
}
