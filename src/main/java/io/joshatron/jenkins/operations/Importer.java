package io.joshatron.jenkins.operations;

import io.joshatron.jenkins.config.JenkinsConfig;
import io.joshatron.jenkins.config.JenkinsJob;
import io.joshatron.jenkins.config.JenkinsServer;
import io.joshatron.jenkins.config.Parameter;
import io.joshatron.jenkins.config.ParameterType;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Importer {

    public Importer() {
        throw new IllegalStateException("This is a utility class");
    }

    public static void importJobsFromFolder(JenkinsServer server, String url, List<String> tags, List<Parameter> parameters) {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            System.out.println(server.getBaseUrl() + url + "/api/json");
            HttpGet request = new HttpGet(server.getBaseUrl() + url + "/api/json");
            request.setHeader("Authorization", server.getAuth());

            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                for(int i = 0; i < json.getJSONArray("jobs").length(); i++) {
                    JSONObject job = json.getJSONArray("jobs").getJSONObject(i);

                    server.addJob(new JenkinsJob(job.getString("name"), job.getString("url").substring(server.getBaseUrl().length()), tags, parameters));
                }
            }
            else {
                System.out.println("No jobs found");
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            System.out.println("Exception while importing jobs");
            e.printStackTrace();
        }
    }

    public static void importJobsFromFolder(JenkinsConfig config, Arguments arguments) {
        List<String> tags = Arrays.asList(arguments.getArgument("tags").split(" "));
        List<Parameter> parameters = new ArrayList<>();
        String[] params = arguments.getArgument("params").split(" ");
        for(int i = 0; i < params.length; i += 4) {
            parameters.add(new Parameter(params[i], ParameterType.valueOf(params[i + 1]), params[i + 2], params[i + 3]));
        }
        JenkinsServer server = null;
        for(JenkinsServer s : config.getServers()) {
            if(arguments.getArgument("url").startsWith(s.getBaseUrl())) {
                server = s;
            }
        }
        String url = arguments.getArgument("url").substring(server.getBaseUrl().length());

        importJobsFromFolder(server, url, tags, parameters);
    }
}
