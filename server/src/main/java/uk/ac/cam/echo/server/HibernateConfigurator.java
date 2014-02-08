package uk.ac.cam.echo.server;


import org.hibernate.cfg.Configuration;

public interface HibernateConfigurator {
    public void configure(Configuration config);
}
