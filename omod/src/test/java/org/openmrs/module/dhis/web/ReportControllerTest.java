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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportControllerTest extends BaseWebControllerTest {
    private static final GlobalProperty AQS_CONFIG = new GlobalProperty(DhisConstants.AQS_CONFIG_GLOBAL_PROPERTY_KEY, "test_aqs_config.json");
    @Autowired
    @Qualifier("adminService")
    private AdministrationService administrationService;

    @Autowired
    private JDBCConnectionProvider jdbcConnectionProvider;
    private String postJson;

    private static Boolean haveMigrationsRun = false;

    @Before
    public void setUp() throws Exception {
        executeDataSet("aqs_setup.xml");
        synchronized (haveMigrationsRun) {
            if (!haveMigrationsRun) {
                haveMigrationsRun = true;
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(jdbcConnectionProvider.getConnection()));
                Liquibase liquibase = new Liquibase("testLiquibase.xml", new ClassLoaderResourceAccessor(), database);
                liquibase.update("test");
            }
        }

        administrationService.saveGlobalProperty(AQS_CONFIG);
        postJson = "{ \"params\": [" +
                "{\"name\": \"firstGender\", \"value\":\"F\"}," +
                "{\"name\": \"secondGender\", \"value\":\"M\"}," +
                "{\"name\": \"firstCount\", \"value\":\"1999\"}," +
                "{\"name\": \"secondCount\", \"value\":\"1999\"}" +
                "]}";
    }

    @Test
    public void trigger_aqs_fire_queries() throws Exception {
        Long taskId = Long.valueOf(handle(newPostRequest("/rest/v1/dhis/fireQueries", postJson)).getContentAsString());

        dblog dblog = new aggregatequeryservice.dblog();
        AQSTask executedTask = (AQSTask) dblog.getTaskById(jdbcConnectionProvider, taskId.intValue());

        while (!executedTask.getTaskStatus().equals("DONE")) {
            executedTask = (AQSTask) dblog.getTaskById(jdbcConnectionProvider, taskId.intValue());
        }
        assertEquals(taskId, executedTask.getTaskId());
        assertTrue(((String) executedTask.getResults()).contains("number_of_males"));
        assertTrue(((String) executedTask.getResults()).contains("number_of_females"));
    }

    @Test
    public void get_task_by_id() throws Exception {
        Integer taskId = Integer.valueOf(handle(newPostRequest("/rest/v1/dhis/fireQueries", postJson)).getContentAsString());
        AQSTask task = deserialize(handle(newGetRequest("/rest/v1/dhis/task/" + String.valueOf(taskId))), AQSTask.class);
        assertEquals(taskId.intValue(), task.getTaskId().intValue());
        assertEquals(String.valueOf(AQS_CONFIG.getValue()), task.getAqsConfigPath());
    }

    @Test
    public void do_no_run_queries_if_one_job_already_in_progress() throws Exception {
        createAnInProgressTask();

        MockHttpServletResponse httpResponse = handle(newPostRequest("/rest/v1/dhis/fireQueries", postJson));
        assertEquals(HttpStatus.CONFLICT.value(), httpResponse.getStatus());
    }

    private void createAnInProgressTask() throws SQLException {
        Connection connection = jdbcConnectionProvider.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into aqs_task(aqs_task_id, aqs_config_path, task_status, date_created) values(999, 'aqs_config', 'IN PROGRESS', now())");
        preparedStatement.execute();
    }
}