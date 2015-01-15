package org.openmrs.module.dhis.db;

import connectionprovider.AQSConnectionProvider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class JDBCConnectionProvider implements AQSConnectionProvider {
    @Autowired
    private SessionFactory sessionFactory;

    private ThreadLocal<Session> session = new ThreadLocal<>();

    @Override
    public Connection getConnection() {
        if (session.get() == null || !session.get().isOpen()) {
            session.set(sessionFactory.openSession());
        }
        return session.get().connection();
    }

    @Override
    public void closeConnection() {
        session.get().close();
    }
}
