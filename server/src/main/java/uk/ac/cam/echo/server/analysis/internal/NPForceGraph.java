package uk.ac.cam.echo.server.analysis.internal;

import java.util.*;

/**
 Author: Petar 'PetarV' Veličković
 Non-persistent force graph.
*/
public class NPForceGraph
{
    public static class Node
    {
        private String name;
        private long type;
        private long internalId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public long getType() { return type; }
        public void setType(long type) { this.type = type; }

        public long getIid() { return internalId; }
        public void setIid(long iId) { this.internalId = iId; }
    }

    private static Map<String, Node> T = new HashMap<String, Node>();
    private static Map<Node, Set<Node>> G = new HashMap<Node, Set<Node>>();
    public static long lastTS = 0L;

    private static Node getFNode(String val, long type, long iid)
    {
        String tString = type + "|!@()#$||" + iid;
        if (T.containsKey(tString)) return T.get(tString);
        Node model = new Node();
        model.setName(val);
        model.setType(type);
        model.setIid(iid);
        T.put(tString, model);
        G.put(model, new HashSet<Node>());
        return model;
    }

    public static void addNode(String name, long type, long iid)
    {
        getFNode(name, type, iid);
    }

    public static void addEdge(String uName, long uType, long uIid, String vName, long vType, long vIid)
    {
        Node u = getFNode(uName, uType, uIid);
        Node v = getFNode(vName, vType, vIid);
        G.get(u).add(v);
    }

    public static void flush()
    {
        T.clear();
        G.clear();
        lastTS = 0L;
    }

    public static String getJSONFGraph()
    {
        StringBuilder jsonFGraph = new StringBuilder();
        StringBuilder nodeBuilder = new StringBuilder();
        StringBuilder linkBuilder = new StringBuilder();
        Map<Node, Long> mst = new HashMap<Node, Long>();
        long seqId = 0;

        nodeBuilder.append('[');
        boolean nodeStart = false;
        for (Node FN : G.keySet())
        {
            if (nodeStart) nodeBuilder.append(",\n");
            else nodeBuilder.append('\n');
            nodeBuilder.append('{');
            nodeBuilder.append("\"name\":\"").append(FN.getName()).append("\",");
            nodeBuilder.append("\"group\":").append(FN.getType());
            nodeBuilder.append('}');
            mst.put(FN, seqId++);
            nodeStart = true;
        }
        nodeBuilder.append("\n]");

        linkBuilder.append('[');
        boolean linkStart = false;

        for (Node FN : G.keySet())
        {
            Set<Node> EE = G.get(FN);
            for (Node FM : EE)
            {
                if (linkStart) linkBuilder.append(",\n");
                else linkBuilder.append('\n');
                linkBuilder.append('{');
                linkBuilder.append("\"source\":").append(mst.get(FN)).append(',');
                linkBuilder.append("\"target\":").append(mst.get(FM)).append(',');
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
