package com.netflix.explorers.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.netflix.explorers.annotations.ExplorerEntity;

@XmlRootElement(name = "map")
@ExplorerEntity
public class MapEntity {
    private Map<Object, Object> map;

    public MapEntity() {
        
    }
    
    public MapEntity(Map<Object, Object> map) {
        super();
        this.map = map;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }
    
    
}
