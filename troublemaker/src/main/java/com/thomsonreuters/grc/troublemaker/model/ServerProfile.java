package com.thomsonreuters.grc.troublemaker.model;

import com.thomsonreuters.grc.tools.cmdb.model.*;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ServerProfile {

    private com.thomsonreuters.grc.tools.cmdb.model.ServerProfile serverProfile;

    public ServerProfile(com.thomsonreuters.grc.tools.cmdb.model.ServerProfile serverProfile) {
        this.serverProfile = serverProfile;
    }

    public String getName() {
        return getServerProfileProperty("name");
    }

    public String getOsPlatform() {
        return getProperty("osPlatform");
    }

    public String getHardwareModel() {
        return getProperty("hardwareModelName");
    }

    public String getNumberOfLogicalCpus() {
        return getProperty("numLogicalCpus");
    }

    public String getMemorySizeInGb() {
        return getProperty("memorySizeGb");
    }

    public List<Server> getServers() {
        return ((Collection<com.thomsonreuters.grc.tools.cmdb.model.Server>)serverProfile.getServers()).stream().map(s -> new Server(s)).collect(Collectors.toList());
    }


    protected String getServerProfileProperty(String name) {
        return getProperty("serverProfile"+ StringUtils.capitalize(name));
    }

    protected String getProperty(String name) {
        return serverProfile.getProperty(name).toString();
    }

}
