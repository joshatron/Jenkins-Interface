package io.joshatron.jenkins.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class JenkinsJob {
    private String name;
    private String url;
    private ArrayList<String> tags;
    private ArrayList<Parameter> parameters;

    public JenkinsJob(String name, String url) {
        this.name = name;
        this.url = url;
        this.tags = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        for(String currentTag : tags) {
            if(currentTag.equalsIgnoreCase(tag)) {
                tags.remove(currentTag);
                break;
            }
        }
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public void removeParameter(String parameterName) {
        for(Parameter parameter : parameters) {
            if(parameter.getParameterName().equalsIgnoreCase(parameterName)) {
                tags.remove(parameter);
                break;
            }
        }
    }
}
