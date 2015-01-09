package org.openmrs.module.dhis.web;

import aggregatequeryservice.runqueries;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhis.DhisConstants;
import org.openmrs.module.dhis.mapper.QueryParametersMapper;
import org.openmrs.module.dhis.model.QueryParameters;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.HashMap;

@Controller
public class ReportController extends BaseRestController {

    @Autowired
    private SessionFactory sessionFactory;

    private final String baseUrl = "/rest/v1/dhis/";

    public ReportController() {
    }

    @RequestMapping(method = RequestMethod.POST, value = baseUrl + "fireQueries")
    @ResponseBody
    public Integer run(@RequestBody QueryParameters queryParameters) {
        HashMap<String, String> queryParams = QueryParametersMapper.map(queryParameters);
        DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
        Object result = runqueries.AQS(Context.getAdministrationService().getGlobalProperty(DhisConstants.AQS_CONFIG_GLOBAL_PROPERTY_KEY), dataSource, queryParams);
        HashMap<String, Object> resultFuture = (HashMap<String, Object>) result;
        return (Integer) resultFuture.get("task_id");
    }
}
