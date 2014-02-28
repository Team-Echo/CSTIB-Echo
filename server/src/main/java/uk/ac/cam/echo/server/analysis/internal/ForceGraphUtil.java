package uk.ac.cam.echo.server.analysis.internal;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.ForceNodeModel;

import java.util.*;

/**
 Author: Petar 'PetarV' Veličković

 A utility class designed to simplify force graph generation
 and conversion into various JSON forms.
*/
public class ForceGraphUtil
{
    public static long lastTS = 0L;

    public static List<ForceNodeModel> getAll()
    {
        return HibernateUtil.getTransaction().createCriteria(ForceNodeModel.class).list();
    }

    private static ForceNodeModel createFNode(String val, long type, long iid)
    {
        ForceNodeModel model = new ForceNodeModel();
        model.setName(val);
        model.setType(type);
        model.setInternalId(iid);
        model.setAdjacent(new HashSet<ForceNodeModel>());
        model.save();
        return model;
    }

    public static ForceNodeModel getFNode(String val, long type, long iid)
    {
        List<ForceNodeModel> res = HibernateUtil.getTransaction().createCriteria(ForceNodeModel.class)
                .add(Restrictions.eq("type", type))
                .add(Restrictions.eq("internalId", iid)).list();
        if (res == null) return createFNode(val, type, iid);
        if (res.isEmpty()) return createFNode(val, type, iid);
        return res.get(0);
    }

    public static void addNode(String name, long type, long iid)
    {
        getFNode(name, type, iid);
    }

    public static void addEdge(String uName, long uType, long uIid, String vName, long vType, long vIid)
    {
        ForceNodeModel u = getFNode(uName, uType, uIid);
        ForceNodeModel v = getFNode(vName, vType, vIid);
        u.addAdjacentNode(v);
        u.save();
    }

    private static void flush()
    {
        // NOP
    }

    public static String getJSONFGraph()
    {
        StringBuilder jsonFGraph = new StringBuilder();
        StringBuilder nodeBuilder = new StringBuilder();
        StringBuilder linkBuilder = new StringBuilder();

        Map<Long, Long> mst = new HashMap<Long, Long>();
        long seqId = 0;

        List<ForceNodeModel> V = getAll();
        nodeBuilder.append('[');
        boolean nodeStart = false;
        for (ForceNodeModel FN : V)
        {
            if (nodeStart) nodeBuilder.append(",\n");
            else nodeBuilder.append('\n');
            nodeBuilder.append('{');
            nodeBuilder.append("\"name\":\"").append(FN.getName()).append("\",");
            nodeBuilder.append("\"group\":").append(FN.getType());
            nodeBuilder.append('}');
            mst.put(FN.getId(), seqId++);
            nodeStart = true;
        }
        nodeBuilder.append("\n]");

        linkBuilder.append('[');
        boolean linkStart = false;
        for (ForceNodeModel FN : V)
        {
            Set<ForceNodeModel> adj = FN.getAdjacent();
            for (ForceNodeModel FM : adj)
            {
                if (linkStart) linkBuilder.append(",\n");
                else linkBuilder.append('\n');
                linkBuilder.append('{');
                linkBuilder.append("\"source\":").append(mst.get(FN.getId())).append(',');
                linkBuilder.append("\"target\":").append(mst.get(FM.getId())).append(',');
                linkBuilder.append("\"value\":").append(1);
                linkBuilder.append('}');
                linkStart = true;
            }
        }
        linkBuilder.append("\n]");

        jsonFGraph.append("{\n");
        jsonFGraph.append("\"nodes\":").append(nodeBuilder).append(",\n");
        jsonFGraph.append("\"links\":").append(linkBuilder).append('\n');
        jsonFGraph.append("}\n");

        return jsonFGraph.toString();
    }
}
