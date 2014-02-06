package uk.ac.cam.echo.server.filters;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;
import uk.ac.cam.echo.server.HibernateUtil;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class JacksonWithHibernateJsonProvider extends JacksonJaxbJsonProvider {
    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        try {
            HibernateUtil.getTransaction();
            super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
            closeHibernateSession();
        } catch(Throwable plm) {
            plm.printStackTrace();
        }
    }


    private void closeHibernateSession() {
        System.out.println("Closing session");
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();

        if (transaction.isActive()) {
            session.getTransaction().commit();
        }
    }
}
