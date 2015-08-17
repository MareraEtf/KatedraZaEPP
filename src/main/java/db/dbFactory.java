/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 *
 * @author Marko
 */
public class dbFactory {

    private static SessionFactory factory;
    private static ServiceRegistry serviceRegistry;

    static {
        try {

            Configuration configuration = new Configuration();
            configuration.configure();

            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            factory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {

            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() {

        Session session = factory.getCurrentSession();

        return session;

    }

    public static SessionFactory getFactory() {

        return factory;
    }
}
