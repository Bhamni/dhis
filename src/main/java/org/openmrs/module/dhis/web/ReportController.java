package org.openmrs.module.dhis.web;

import org.hibernate.SessionFactory;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ReportController extends BaseRestController {

    @Autowired
    private SessionFactory sessionFactory;

    private final String baseUrl = "/rest/v1/dhis/";

    public ReportController() {
    }

    @RequestMapping(method = RequestMethod.POST, value = baseUrl + "")
    @ResponseBody
    public Boolean get() {

    }

}
