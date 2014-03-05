package uk.ac.cam.echo.server.analysis.internal;

import org.hibernate.criterion.Restrictions;
import uk.ac.cam.echo.server.HibernateUtil;
import uk.ac.cam.echo.server.models.NodeModel;

import java.util.*;

/**
 Author: Petar 'PetarV' Veličković

 A utility class designed to simplify graph generation
 and conversion into various JSON forms.
*/
public class GraphUtil2
{
    public static class Node
    {
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    private static Map<String, Node> T = new HashMap<String, Node>();
    private static Map<Node, Set<Node>> G = new HashMap<Node, Set<Node>>();
    public static long lastTS = 0L;

    private static Node getNode(String val)
    {
        String tString = val;
        if (T.containsKey(tString)) return T.get(tString);
        Node model = new Node();
        model.setName(val);
        T.put(tString, model);
        G.put(model, new HashSet<Node>());
        return model;
    }

    public static void addNode(String name)
    {
        getNode(name);
    }

    public static void addEdge(String uName, String vName)
    {
        Node u = getNode(uName);
        Node v = getNode(vName);
        G.get(u).add(v);
    }

    public static void flush()
    {
        T.clear();
        G.clear();
        lastTS = 0L;
    }

    public static String getJSONGraph()
    {
        int idd = 1;
        StringBuilder jsonGraph = new StringBuilder();
        boolean headStart = false;
        jsonGraph.append('[');
        for (Node N : G.keySet())
        {
            if (headStart) jsonGraph.append(",\n");
            else jsonGraph.append('\n');
            jsonGraph.append('{');
            jsonGraph.append("\"name\":\"").append(N.getName()).append("\",");
            jsonGraph.append("\"size\":").append(idd++).append(',');
            jsonGraph.append("\"imports\":");
            jsonGraph.append('[');
            Set<Node> EE = G.get(N);
            boolean start = false;
            for (Node M : EE)
            {
                if (start) jsonGraph.append(',');
                jsonGraph.append('\"').append(M.getName()).append('\"');
                start = true;
            }
            jsonGraph.append("]}");
            headStart = true;
        }
        jsonGraph.append("\n]\n");

        return jsonGraph.toString();
    }
}
