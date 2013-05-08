package com.netflix.explorers.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.netflix.explorers.annotations.ExplorerEntity;

@ExplorerEntity
@XmlRootElement(name="explorer_list")
public class ExplorerInfoListEntity  {
    private List<ExplorerInfoEntity> explorers;

    public List<ExplorerInfoEntity> getExplorers() {
        return explorers;
    }

    public void setExplorers(List<ExplorerInfoEntity> explorers) {
        this.explorers = explorers;
    }
    
}
