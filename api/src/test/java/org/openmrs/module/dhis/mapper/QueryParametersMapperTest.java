package mapper;


import org.junit.Test;
import org.openmrs.module.dhis.mapper.QueryParametersMapper;
import org.openmrs.module.dhis.model.QueryParameter;
import org.openmrs.module.dhis.model.QueryParameters;

import java.util.ArrayList;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class QueryParametersMapperTest {
    @Test
    public void empty_query_parameters_return_empty_map() throws Exception {
        QueryParameters queryParameters = new QueryParameters();
        Map<String, String> queryParamsMap = QueryParametersMapper.map(queryParameters);
        assertTrue(queryParamsMap.isEmpty());
    }

    @Test
    public void map_single_parameter_as_a_key_value_pair() throws Exception {
        QueryParameters queryParameters = new QueryParameters(new ArrayList<QueryParameter>());
        QueryParameter queryParameter = new QueryParameter("param1", "1");
        queryParameters.addParam(queryParameter);
        Map<String, String> queryParamsMap = QueryParametersMapper.map(queryParameters);
        assertFalse(queryParamsMap.isEmpty());
    }

    @Test
    public void map_right_values_from_query_params_to_map() throws Exception {
        QueryParameters queryParameters = new QueryParameters(new ArrayList<QueryParameter>());
        queryParameters.addParam(new QueryParameter("param1", "1"));
        queryParameters.addParam(new QueryParameter("param2", "2"));
        queryParameters.addParam(new QueryParameter("param3", "3"));
        queryParameters.addParam(new QueryParameter("param4", "4"));
        Map<String, String> queryParamsMap = QueryParametersMapper.map(queryParameters);
        assertFalse(queryParamsMap.isEmpty());
        assertEquals(4, queryParamsMap.keySet().size());
        assertEquals("1", queryParamsMap.get("param1"));
        assertEquals("2", queryParamsMap.get("param2"));
        assertEquals("3", queryParamsMap.get("param3"));
        assertEquals("4", queryParamsMap.get("param4"));
    }
}