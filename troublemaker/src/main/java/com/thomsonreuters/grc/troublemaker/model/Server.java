package com.thomsonreuters.grc.troublemaker.model;

import org.springframework.util.StringUtils;

public class Server {

    private com.thomsonreuters.grc.tools.cmdb.model.Server server;

    public Server(com.thomsonreuters.grc.tools.cmdb.model.Server server) {
        this.server = server;
    }

    public String getHostname() {
        return getServerProperty("name");
    }

    public String getProfileName() {
        return getServerProperty("profileName");
    }

    public String getStatus() {
        return getServerProperty("status");
    }

    protected String getServerProperty(String name) {
        return getProperty("server"+ StringUtils.capitalize(name));
    }

    protected String getProperty(String name) {
        return (String) server.getProperty(name);
    }
}
