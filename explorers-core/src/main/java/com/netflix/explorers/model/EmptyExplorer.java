package com.netflix.explorers.model;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Sets;
import com.netflix.explorers.Explorer;
import com.netflix.explorers.ExplorerManager;

public class EmptyExplorer implements Explorer {
    private static final Set<String> EMPTY_SET = Sets.newHashSet();
    
    private static final EmptyExplorer instance = new EmptyExplorer();
    public static final EmptyExplorer getInstance() {
        return instance;
    }
    
    private EmptyExplorer() {
        
    }
    
    @Override
    public void initialize() {
    }

    @Override
    public void initialize(ExplorerManager manager) {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getTitle() {
        return "Home";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getAlertMessage() {
        return "";
    }

    @Override
    public boolean getIsSecure() {
        return false;
    }

    @Override
    @Deprecated
    public Map<String, String> getMenuLayout() {
        return null;
    }

    @Override
    public MenuItem getMenu() {
        return null;
    }

    @Override
    public String getLayoutName() {
        return "bootstrap";
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }

    @Override
    public Set<String> getRolesAllowed() {
        return EMPTY_SET;
    }

    @Override
    public boolean getCmcEnabled() {
        return false;
    }
}
