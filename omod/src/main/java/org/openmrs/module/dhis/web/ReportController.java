package org.openmrs.module.dhis.web;

import aggregatequeryservice.dblog;
import com.mchange.v2.c3p0.C3P0Registry;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@Controller
public class ReportController extends BaseRestController {
    private final String baseUrl = "/rest/v1/dhis/";

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "tasks")
    @ResponseBody
    public String getAllTasks() {
        dblog dblog = new aggregatequeryservice.dblog();
        // TODO : Mujir - a better way to get datasource without relying on C3P0.
        Object allTasks = dblog.getAllTasks((DataSource) (C3P0Registry.getPooledDataSources().toArray()[0]));
        return allTasks.toString();
    }
}