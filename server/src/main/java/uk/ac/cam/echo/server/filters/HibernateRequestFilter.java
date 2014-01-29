package uk.ac.cam.echo.server.filters;


import uk.ac.cam.echo.server.HibernateUtil;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

public class HibernateRequestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        HibernateUtil.getTransaction();
    }
}
