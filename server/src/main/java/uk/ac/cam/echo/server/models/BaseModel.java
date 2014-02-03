package uk.ac.cam.echo.server.models;

import uk.ac.cam.echo.server.HibernateUtil;

public class BaseModel {
    public void delete() {
        HibernateUtil.getTransaction().delete(this);
    }
}
