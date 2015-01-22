package org.openmrs.module.dhis.web;

import aggregatequeryservice.aqstask.AQSTask;
import aggregatequeryservice.dblog;
import aggregatequeryservice.runqueries;
import clojure.lang.PersistentArrayMap;
import org.apache.commons.collections.CollectionUtils;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.dhis.DhisConstants;
import org.openmrs.module.dhis.db.JDBCConnectionProvider;
import org.openmrs.module.dhis.mapper.QueryParametersMapper;
import org.openmrs.module.dhis.model.QueryParameters;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public List<AQSTask> getAllTasks() {
        return new aggregatequeryservice.dblog().getAllTasks(jdbcConnectionProvider);
    }

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "task/{taskId}")
    @ResponseBody
    public AQSTask getTaskById(@PathVariable("taskId") int taskId) {
        return (AQSTask) new dblog().getTaskById(jdbcConnectionProvider, taskId);
    }

    @RequestMapping(method = RequestMethod.POST, value = baseUrl + "fireQueries")
    @ResponseBody
    public ResponseEntity<Long> fireQueries(@RequestBody QueryParameters queryParameters) {
        if (!CollectionUtils.isEmpty(getTasksInProgress()))
            return new ResponseEntity<>(DhisConstants.INVALID_TASK_ID, HttpStatus.CONFLICT);

        String config_file = administrationService.getGlobalProperty(DhisConstants.AQS_CONFIG_GLOBAL_PROPERTY_KEY);
        HashMap<String, String> queryParamsMap = QueryParametersMapper.map(queryParameters);
        PersistentArrayMap allTasks = (PersistentArrayMap) runqueries.AQS(config_file, jdbcConnectionProvider, queryParamsMap);
        return new ResponseEntity<>((Long) allTasks.get(DhisConstants.TASK_ID), HttpStatus.OK);
    }

    private ArrayList getTasksInProgress() {
        return new aggregatequeryservice.dblog().getAllTasksInProgress(jdbcConnectionProvider);
    }
}