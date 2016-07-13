package com.thomsonreuters.grc.troublemaker.model;

import com.thomsonreuters.grc.tools.cmdb.model.Target;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.StringUtils;

public class Application {

    private Target target;

    public Application(Target t) {
        target = t;
    }


    public String getName() {
        return getTargetProperty("name");
    }

    public String getVersion() {
        return getTargetProperty("version");
    }

    public String getType() {
        return getTargetProperty("type");
    }

    public String getDescription() {
        return getProperty("description");
    }

    public boolean isReady() {
        return BooleanUtils.toBoolean(Integer.parseInt(getProperty("isReady")));
    }

    protected String getTargetProperty(String name) {
        return getProperty("target"+ StringUtils.capitalize(name));
    }

    protected String getProperty(String name) {
        return (String) target.getProperty(name);
    }
}
