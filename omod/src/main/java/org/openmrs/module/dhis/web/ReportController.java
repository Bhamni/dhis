package org.openmrs.module.dhis.web;

import aggregatequeryservice.aqstask.AQSTask;
import aggregatequeryservice.dblog;
import aggregatequeryservice.runqueries;
import clojure.lang.PersistentArrayMap;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.dhis.DhisConstants;
import org.openmrs.module.dhis.db.JDBCConnectionProvider;
import org.openmrs.module.dhis.mapper.QueryParametersMapper;
import org.openmrs.module.dhis.model.QueryParameters;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ReportController extends BaseRestController {
    private final String baseUrl = "/rest/v1/dhis/";

    @Autowired
    private JDBCConnectionProvider jdbcConnectionProvider;

    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "tasks")
    @ResponseBody
    public ArrayList getAllTasks() {
        dblog dblog = new aggregatequeryservice.dblog();
        ArrayList allTasks = (ArrayList) dblog.getAllTasks(jdbcConnectionProvider);
        return allTasks;
    }

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "task/{taskId}")
    @ResponseBody
    public AQSTask getTaskById(@PathVariable("taskId") int taskId) {
        dblog dblog = new aggregatequeryservice.dblog();
        AQSTask task = (AQSTask) dblog.getTaskById(jdbcConnectionProvider, taskId);
        return task;
    }

    @RequestMapping(method = RequestMethod.POST, value = baseUrl + "fireQueries")
    @ResponseBody
    public Long fireQueries(@RequestBody QueryParameters queryParameters) {
        String config_file = administrationService.getGlobalProperty(DhisConstants.AQS_CONFIG_GLOBAL_PROPERTY_KEY);
        HashMap<String, String> queryParamsMap = QueryParametersMapper.map(queryParameters);
        PersistentArrayMap allTasks = (PersistentArrayMap) runqueries.AQS(config_file, jdbcConnectionProvider, queryParamsMap);
        return (Long) allTasks.get("task-id");
    }
}