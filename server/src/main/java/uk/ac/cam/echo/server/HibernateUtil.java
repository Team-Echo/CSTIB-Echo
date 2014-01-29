package uk.ac.cam.echo.server;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static SessionFactory sf=configureSessionFactory();
    private static SessionFactory configureSessionFactory()
            throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        SessionFactory sessionFactory = configuration
                .buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sf;
    }

    public static Session getSession() {
        return sf.getCurrentSession();
    }
    public static Session getTransaction() {
        Session session = sf.getCurrentSession();
        Transaction transaction = session.getTransaction();
        if (!transaction.isActive())
            session.beginTransaction();
        return session;
    }

}
