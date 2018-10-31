package data;

import com.sun.istack.internal.NotNull;
import data.bean.Edge;
import data.bean.Path;
import data.bean.SubwayData;

import java.util.*;

public class FindPathUtil {

    private Map<String, List<Edge>> subwayGraph;

    private SubwayData subwayData;

    private boolean[] mark;

    public FindPathUtil(SubwayData subwayGraph) {
        this.subwayGraph = subwayGraph.getSubWayGraph();
        this.subwayData = subwayGraph;
        this.mark = new boolean[subwayData.getEdgeCount()];
    }

    public Path shortestPath(String start, String end) {
        PathUtil pathUtil = new PathUtil(subwayData);
        if (start.equals(end)) {
            List<String> result = new ArrayList<>(2);
            result.add(start);
            result.add(end);
            return pathUtil.stationsToPath(result, null);
        }
        PriorityQueue<PathNode> startQueue = new PriorityQueue<>();
        PathNode init = new PathNode();
        init.addStation(start, null);
        startQueue.add(init);
        while (startQueue.size() != 0) {
            PathNode nowPath = startQueue.poll();
            List<Edge> edges = subwayGraph.get(nowPath.currentStation());
            if (edges.size() == 1) {
                Edge edge = edges.get(0);
                String other = edge.other(nowPath.currentStation());
                if(!mark[edge.getId()]) {
                    if (nowPath.hasGoHere(other)) {
                        continue;
                    }
                    List<String> result = judge(end, startQueue, nowPath, edge, other);
                    if (result != null) {
                        return pathUtil.stationsToPath(result, nowPath.getEdges());
                    }
                }
            } else {
                boolean isFirst = true;
                boolean needNew = true;
                PathNode path;
                for (Edge e : edges) {
                    if (isFirst) {
                        path = nowPath;
                        isFirst = false;
                    } else {
                        if (needNew) {
                            path = new PathNode(nowPath);
                            nowPath = path;
                        } else {
                            path = nowPath;
                        }
                        needNew = true;
                    }
                    String other = e.other(path.currentStation());
                    if (!mark[e.getId()]) {
                        if (path.hasGoHere(other)) {
                            needNew = false;
                            continue;
                        }
                        List<String> result = judge(end, startQueue, path, e, other);
                        if (result != null) {
                            return pathUtil.stationsToPath(result, path.getEdges());
                        }
                    } else {
                        needNew = false;
                    }
                }
            }
        }
        return null;
    }

    private List<String> judge(String end, PriorityQueue<PathNode> queue, PathNode path, Edge edge, String other) {
        if (!other.equals(end)) {
            path.addStation(other, edge);
            mark[edge.getId()] = true;
            queue.remove(path);
            queue.add(path);
            return null;
        } else {
            path.addStation(other, edge);
            return path.getStations();
        }
    }

    private class PathNode implements Comparable<PathNode> {
        private LinkedList<String> stations;

        private ArrayList<Edge> edges;

        private float length;

        public PathNode() {
            stations = new LinkedList<>();
            edges = new ArrayList<>();
            length = 0;
        }

        public PathNode(@NotNull PathNode path) {
            this.stations = new LinkedList<>(path.getStations());
            this.edges = new ArrayList<>(path.getEdges());
            this.length = path.pathLength();
            stations.removeLast();
            edges.remove(edges.size() - 1);
        }

        public void addStation(String station, Edge edge) {
            if(edge != null) {
                this.length += edge.getWeight();
                edges.add(edge);
            }
            stations.add(station);

        }

        public float pathLength() {
            return length;
        }

        public String currentStation() {
            return stations.getLast();
        }

        public LinkedList<String> getStations() {
            return stations;
        }

        public boolean hasGoHere(String s) {
            return stations.contains(s);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return Float.compare(pathNode.length, length) == 0;
        }

        public ArrayList<Edge> getEdges() {
            return edges;
        }

        @Override
        public int hashCode() {
            return Objects.hash(length);
        }

        @Override
        public int compareTo(PathNode o) {
            if (length > o.length) {
                return 1;
            } else if (length == o.length) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
