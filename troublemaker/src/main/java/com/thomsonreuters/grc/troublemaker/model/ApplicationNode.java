package com.thomsonreuters.grc.troublemaker.model;

import com.thomsonreuters.grc.tools.cmdb.model.ServerProfileTargetAppNode;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationNode {

    private ServerProfileTargetAppNode node;

    public ApplicationNode(ServerProfileTargetAppNode node) {
        this.node = node;
    }

    public int getId() {
        return Integer.parseInt(getProperty("nodeId"));
    }

    public int getPort() {
        return Integer.parseInt(getProperty("applicationPort"));
    }

    public int getManagementPort() {
        return Integer.parseInt(getProperty("mgmtPort"));
    }

    public List<ServerProfile> getServerProfile() {
        return node.getServerProfiles().stream().map(ServerProfile::new).collect(Collectors.toList());
    }

    protected String getServerProfileProperty(String name) {
        return getProperty("serverProfile"+ StringUtils.capitalize(name));
    }


    protected String getProperty(String name) {
        return node.getProperty(name).toString();
    }

}
