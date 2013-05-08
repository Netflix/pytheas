package com.netflix.explorers.model;

public class EntityNotFoundException extends Exception {
    private static final long serialVersionUID = -3983735082048371603L;
    private String entityType;
    private String entityName;
    private String error;
    
    public EntityNotFoundException(String entityType, String entityName) {
        this.entityName = entityName;
        this.entityType = entityType;
    }
    
    public String getEntityName() {
        return entityName;
    }
    public EntityNotFoundException setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
    public String getEntityType() {
        return entityType;
    }
    public EntityNotFoundException setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    public String getError() {
        return error;
    }
    public EntityNotFoundException setError(String error) {
        this.error = error;
        return this;
    }
    
    @Override
    public String getMessage() {
        return toString();
    }
    
    @Override
    public String toString() {
        return "ExceptionEntity [error=" + error + ", entityType=" + entityType+ ", entityName=" + entityName + "]";
    }
    

}
