package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class JenkinsConfig {
    private ArrayList<JenkinsServer> servers;

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
