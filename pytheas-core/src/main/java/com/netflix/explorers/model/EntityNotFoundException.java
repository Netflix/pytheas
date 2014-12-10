/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
