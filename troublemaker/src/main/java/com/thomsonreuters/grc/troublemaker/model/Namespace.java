package com.thomsonreuters.grc.troublemaker.model;

public class Namespace {

    private String name;

    public Namespace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getOrganisation() {
        return name.split("-")[0];
    }

    public String getProduct() {
        return name.split("-")[1];
    }

    public String getEnvironment() {
        return name.split("-")[2];
    }

}
