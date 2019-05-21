package io.joshatron.jenkins.operations;

import io.joshatron.jenkins.config.JenkinsConfig;
import io.joshatron.jenkins.config.JenkinsJob;
import io.joshatron.jenkins.config.JenkinsServer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class Builder {

    public Builder() {
        throw new IllegalStateException("This is a utility class");
    }

    public static void buildJob(JenkinsServer server, JenkinsJob job, Arguments arguments) throws IOException {
        HttpClient client = HttpClients.createDefault();

        String url = server.getBaseUrl() + job.getUrl() + "/build";
        String auth = server.getUsername() + ":" + server.getPassword();
        String payload = createPayload(job, arguments);

        HttpPost request = new HttpPost(url);
        request.setHeader("Authorization", auth);
        StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
        request.setEntity(entity);

        client.execute(request);
    }

    private static String createPayload(JenkinsJob job, Arguments arguments) {
        return "";
    }

    public static void buildJobs(JenkinsConfig config, Arguments arguments) throws IOException {
        for(JenkinsServer server : config.getServers()) {
            for(JenkinsJob job : server.getJobs()) {
                buildJob(server, job, arguments);
            }
        }
    }

    public static void buildJobs(JenkinsConfig config, List<String> tags, Arguments arguments) throws IOException {
        buildJobs(config.getJobsWithTags(tags), arguments);
    }

    public static void buildJobs(JenkinsConfig config, String tag, Arguments arguments) throws IOException {
        buildJobs(config.getJobsWithTag(tag), arguments);
    }
}
