package org.openmrs.module.dhis.web;

import aggregatequeryservice.aqstask.AQSTask;
import aggregatequeryservice.dblog;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.bahmni.test.web.controller.BaseWebControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.dhis.DhisConstants;
import org.openmrs.module.dhis.db.JDBCConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportControllerTest extends BaseWebControllerTest {
    private static final GlobalProperty AQS_CONFIG = new GlobalProperty(DhisConstants.AQS_CONFIG_GLOBAL_PROPERTY_KEY, "test_aqs_config.json");
    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @Autowired
    private JDBCConnectionProvider jdbcConnectionProvider;

    @Before
    public void setUp() throws Exception {
        executeDataSet("aqs_setup.xml");
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(jdbcConnectionProvider.getConnection()));
        Liquibase liquibase = new Liquibase("testLiquibase.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update(null);
        administrationService.saveGlobalProperty(AQS_CONFIG);
    }


    @Test
    public void should_trigger_aqs_fire_queries() throws Exception {
        String postJson = "{ \"params\": [" +
                "{\"name\": \"firstGender\", \"value\":\"F\"}," +
                "{\"name\": \"secondGender\", \"value\":\"M\"}," +
                "{\"name\": \"firstCount\", \"value\":\"1999\"}," +
                "{\"name\": \"secondCount\", \"value\":\"1999\"}" +
                "]}";
        Integer taskId = Integer.valueOf(handle(newPostRequest("/rest/v1/dhis/fireQueries", postJson)).getContentAsString());
        dblog dblog = new aggregatequeryservice.dblog();
        AQSTask executedTask = (AQSTask) dblog.getTaskById(jdbcConnectionProvider, taskId);

        while (!executedTask.taskStatus.equals("DONE")) {
            executedTask = (AQSTask) dblog.getTaskById(jdbcConnectionProvider, taskId);
        }
        assertEquals(taskId.intValue(), ((Long) executedTask.taskId).longValue());
        assertTrue(((String) executedTask.results).contains("number_of_males"));
        assertTrue(((String) executedTask.results).contains("number_of_females"));
    }
}

