package data.bean;

import java.util.*;

public class SubwayData {
    private Set<String> mStation;

    private Map<String, List<Edge>> subwayGraph;

    private Map<String, Integer> lines;

    private Map<String, List<String>> startAndEndStation;

    private int edgeCount;

    public SubwayData() {
        mStation = new HashSet<>();
        subwayGraph = new HashMap<>();
        lines = new HashMap<>();
        startAndEndStation = new HashMap<>();
        lines.put("flag", 1);
        edgeCount = 0;
    }

    public void addStart(String lineName, String start) {
        List<String> list = new ArrayList<>(2);
        list.add(start);
        startAndEndStation.put(lineName, list);
    }

    public void addEnd(String lineName, String end) {
        (startAndEndStation.get(lineName)).add(end);
    }

    public void addStation(String station) {
        mStation.add(station);
    }

    public void addEdge(Edge edge) {
        if (subwayGraph.get(edge.getOne()) == null) {
            List<Edge> edges = new ArrayList<>();
            edges.add(edge);
            subwayGraph.put(edge.getOne(), edges);
        } else {
            subwayGraph.get(edge.getOne()).add(edge);
        }
        if (subwayGraph.get(edge.other(edge.getOne())) == null) {
            List<Edge> edges = new ArrayList<>();
            edges.add(edge);
            subwayGraph.put(edge.other(edge.getOne()), edges);
        } else {
            subwayGraph.get(edge.other(edge.getOne())).add(edge);
        }
    }

    public void addEdge(String station1, String station2, float distance, int flag) {
        Edge edge = getEdge(station1, station2);
        if (edge != null) {
            edge.addFlag(flag);
        } else {
            edgeCount++;
            addEdge(new Edge(edgeCount, station1, station2, distance, flag));
            addStation(station1);
            addStation(station2);
        }
    }

    public Edge getEdge(String station1, String station2) {
        List<Edge> edges;
        if ((edges = subwayGraph.get(station1)) != null) {
            for (Edge e : edges) {
                if (e.isMyStation(station2)) {
                    return e;
                }
            }
        }
        return null;
    }

    public String getNextStationOfLine(String station, String lastStation, int flag) {
        for (Edge edge : subwayGraph.get(station)) {
            if (edge.isMyFlag(flag) && (lastStation == null || !edge.other(station).equals(lastStation))) {
                return edge.other(station);
            }
        }
        return null;
    }

    public void addLine(String lineName) {
        if (getLine(lineName) == -1) {
            int flag = lines.get("flag");
            lines.put(lineName, flag);
            flag <<= 1;
            lines.put("flag", flag);
        }
    }

    private int getLine(String lineName) {
        return lines.getOrDefault(lineName, -1);
    }

    public List<String> getAllLines() {
        List<String> result = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : lines.entrySet()) {
            if (!entry.getKey().equals("flag"))
                result.add(entry.getKey());
        }
        Collections.sort(result);
        return result;
    }

    public int getLineFlag(String lineName) {
        if (getLine(lineName) == -1) {
            addLine(lineName);
        }
        return getLine(lineName);
    }

    public List<String> flags2Name(int flag) {
        int mask = 1;
        int temp;
        List<String> result = new LinkedList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (i > 0) {
                mask <<= 1;
            }
            temp = flag & mask;
            if (temp != 0) {
                result.add(flag2Name(temp));
            }
        }
        return result;
    }

    public String flag2Name(int flag) {
        for (Map.Entry<String, Integer> entry : lines.entrySet()) {
            if (entry.getValue() == flag) {
                return entry.getKey();
            }
        }
        return "";
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public Map<String, List<Edge>> getSubWayGraph() {
        return subwayGraph;
    }

    public Set<String> getmStation() {
        return mStation;
    }

    public List<String> getLineNameOfStation(String station) {
        List<Edge> edges = subwayGraph.get(station);
        Set<String> lines = new HashSet<>();
        for (Edge edge : edges) {
            lines.addAll(flags2Name(edge.getFlag()));
        }
        return new ArrayList<>(lines);
    }

    public Map<String, List<String>> getStartAndEndStation() {
        return startAndEndStation;
    }
}
