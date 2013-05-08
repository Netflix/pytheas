package com.netflix.explorers.model;

public class CrossLink {
    private String href;
    private String title;
    private String region;
    private String env;
    
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getEnv() {
        return env;
    }
    public void setEnv(String env) {
        this.env = env;
    }
    
    @Override
    public String toString() {
        return "CrossLink [href=" + href + ", title=" + title + ", region=" + region + ", env=" + env + "]";
    }
    
}
