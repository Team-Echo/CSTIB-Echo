package uk.ac.cam.echo.server;

import org.junit.Test;
import uk.ac.cam.echo.server.models.MessageModel;

public class DemoTest extends HibernateTestCase {
    @Override
    public String getFixtureFile() {
        return "fixtures/default.sql";
    }

    @Test
    public void fixturesTest() {
        startTransaction();
        assertEquals(session.createCriteria(MessageModel.class).list().size(), 5);
        endTransaction();
    }
}
