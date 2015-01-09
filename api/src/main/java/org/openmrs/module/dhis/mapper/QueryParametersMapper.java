package org.openmrs.module.dhis.mapper;


import org.openmrs.module.dhis.model.QueryParameter;
import org.openmrs.module.dhis.model.QueryParameters;

import java.util.HashMap;

public class QueryParametersMapper {
    public static HashMap<String, String> map(QueryParameters queryParameters) {
        HashMap<String, String> queryParamsMap = new HashMap<String, String>();
        for (QueryParameter queryParameter : queryParameters.getParams()) {
            queryParamsMap.put(queryParameter.getName(), queryParameter.getValue());
        }
        return queryParamsMap;
    }
}
