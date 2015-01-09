package org.openmrs.module.dhis.web;

import aggregatequeryservice.dblog;
import org.hibernate.SessionFactory;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

@Controller
public class ReportController extends BaseRestController {

    @Autowired
    private SessionFactory sessionFactory;

    private final String baseUrl = "/rest/v1/dhis/";

    public ReportController() {
    }

    @RequestMapping(method = RequestMethod.GET, value = baseUrl + "tasks")
    @ResponseBody
    public String getAllTasks() {
        DataSource dataSource = SessionFactoryUtils.getDataSource(sessionFactory);
        return (String) new dblog().getAllTasks(dataSource);
    }
}
