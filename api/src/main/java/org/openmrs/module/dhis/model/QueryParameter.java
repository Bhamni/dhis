package org.openmrs.module.dhis.model;

public class QueryParameter {
    private String name;
    private String value;

    public QueryParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public QueryParameter() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
