package uk.ac.cam.echo.server.filters;

import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.ac.cam.echo.server.HibernateUtil;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class HibernateResponseFilter implements ContainerResponseFilter{
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {

        if (!(containerResponseContext.getEntity() instanceof Response || containerResponseContext.getEntity() == null))
            return ;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();

        if (transaction.isActive()) {
            System.out.println("Commiting Transaction");
            session.getTransaction().commit();
        }
    }
}
