package uk.ac.cam.echo.server.resources;

import uk.ac.cam.echo.data.Conference;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.analysis.DataAnalyst;
import uk.ac.cam.echo.server.analysis.ServerDataAnalyst;
import uk.ac.cam.echo.server.models.ConferenceModel;

import java.util.concurrent.ConcurrentHashMap;

/**
 Author: Petar 'PetarV' Veličković
*/
public class AnalystFactory
{
    private static ConcurrentHashMap<Long, ServerDataAnalyst> analysts = new ConcurrentHashMap<Long, ServerDataAnalyst>();

    public static ServerDataAnalyst get(long key)
    {
        if (!analysts.containsKey(key))
        {
            analysts.putIfAbsent(key, new DataAnalyst((Conference) HibernateUtil.getTransaction().get(ConferenceModel.class, key)));
        }
        return analysts.get(key);
    }

}
