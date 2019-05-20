package io.joshatron.jenkins.operations;

import io.joshatron.jenkins.config.JenkinsConfig;
import io.joshatron.jenkins.config.JenkinsJob;
import io.joshatron.jenkins.config.JenkinsServer;

import java.util.List;

public class Builder {

    public Builder() {
        throw new IllegalStateException("This is a utility class");
    }

    public static void buildJob(JenkinsServer server, JenkinsJob job, Arguments arguments) {
    }

    public static void buildJobs(JenkinsConfig config, Arguments arguments) {
        for(JenkinsServer server : config.getServers()) {
            for(JenkinsJob job : server.getJobs()) {
                buildJob(server, job, arguments);
            }
        }
    }

    public static void buildJobs(JenkinsConfig config, List<String> tags, Arguments arguments) {
        buildJobs(config.getJobsWithTags(tags), arguments);
    }

    public static void buildJobs(JenkinsConfig config, String tag, Arguments arguments) {
        buildJobs(config.getJobsWithTag(tag), arguments);
    }
}
