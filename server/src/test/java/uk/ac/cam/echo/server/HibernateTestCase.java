package uk.ac.cam.echo.server;

import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public abstract class HibernateTestCase extends TestCase {
    protected Session session;

    public abstract String getFixtureFile();

    @Override
    public void setUp() throws Exception {
        HibernateUtil.configureSessionFactory(new HibernateConfigurator() {
            @Override
            public void configure(Configuration config) {
                config.setProperty("hibernate.hbm2ddl.import_files", getFixtureFile());
            }
        });
        session = HibernateUtil.getSession();

    }

    public void startTransaction() {
        session.beginTransaction();
    }

    public void endTransaction() {
        Transaction trans = session.getTransaction();
        if (trans.isActive())
            trans.commit();
    }
}
