package org.openmrs.module.dhis.web;

import aggregatequeryservice.dblog;
import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.PropertyVetoException;

@Controller
public class ReportController extends BaseRestController {
    private final String baseUrl = "/rest/v1/dhis/";

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "tasks")
    @ResponseBody
    public String getAllTasks() throws PropertyVetoException {
        dblog dblog = new aggregatequeryservice.dblog();
        // TODO : Mujir - a better way to get datasource without relying on C3P0.
        PooledDataSource dataSource = (PooledDataSource) (C3P0Registry.getPooledDataSources().toArray()[0]);
        Object allTasks = dblog.getAllTasks(dataSource);
        return allTasks.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "task/{taskId}")
    @ResponseBody
    public String getTaskById(@PathVariable("taskId") int taskId) throws PropertyVetoException {
        dblog dblog = new aggregatequeryservice.dblog();
        // TODO : Mujir - a better way to get datasource without relying on C3P0.
        PooledDataSource dataSource = (PooledDataSource) (C3P0Registry.getPooledDataSources().toArray()[0]);
        Object allTasks = dblog.getTaskById(dataSource, taskId);
        return allTasks.toString();
    }
}