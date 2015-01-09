package org.openmrs.module.dhis.model;

import java.util.ArrayList;
import java.util.List;

public class QueryParameters {
    private List<QueryParameter> params;

    public QueryParameters() {
        params = new ArrayList<QueryParameter>();
    }

    public QueryParameters(List<QueryParameter> queryParameters) {
        params = queryParameters;
    }

    public List<QueryParameter> getParams() {
        return params;
    }

    public void setParams(List<QueryParameter> params) {
        this.params = params;
    }

    public void addParam(QueryParameter queryParameter) {
        this.getParams().add(queryParameter);
    }
}
