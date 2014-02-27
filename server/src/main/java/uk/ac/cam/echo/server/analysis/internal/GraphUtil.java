package uk.ac.cam.echo.server.analysis.internal;

import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.NodeModel;

import java.util.List;
import java.util.Set;

/**
 Author: Petar 'PetarV' Veličković

 A utility class designed to simplify graph generation
 and conversion into various JSON forms.
*/
public class GraphUtil
{
    public static List<NodeModel> getAll()
    {
        return HibernateUtil.getTransaction().createCriteria(NodeModel.class).list();
    }

    private static NodeModel createNode(String val)
    {
        NodeModel model = new NodeModel();
        model.setName(val);
        model.save();
        return model;
    }

    public static NodeModel getNode(String val)
    {
        List<NodeModel> res = HibernateUtil.getTransaction().createCriteria(NodeModel.class).add(Restrictions.eq("name", val)).list();
        if (res == null) return createNode(val);
        if (res.isEmpty()) return createNode(val);
        return res.get(0);
    }

    public static void addEdge(String from, String to)
    {
        NodeModel u = getNode(from);
        NodeModel v = getNode(to);
        u.addAdjacentNode(v);
        u.save();
    }

    private static void flush()
    {
        // kill the database. NOP for now.
    }

    public static String getJSONGraph()
    {
        StringBuilder jsonGraph = new StringBuilder();

        jsonGraph.append("[\n");
        List<NodeModel> V = getAll();
        for (NodeModel N : V)
        {
            jsonGraph.append('{');
            jsonGraph.append("\"name\":\"").append(N.getName()).append("\",");
            jsonGraph.append("\"size\":").append(N.getId()).append(",");
            jsonGraph.append("\"imports\":");
            jsonGraph.append('[');
            Set<NodeModel> adj = N.getAdjacent();
            boolean start = false;
            for (NodeModel M : adj)
            {
                if (start) jsonGraph.append(',');
                jsonGraph.append("\"").append(M.getName()).append("\"");
                start = true;
            }
            jsonGraph.append("]},\n");
        }
        jsonGraph.append("]\n");

        return jsonGraph.toString();
    }
}
