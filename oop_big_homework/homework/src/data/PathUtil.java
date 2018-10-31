package data;

import com.sun.istack.internal.NotNull;
import data.bean.Edge;
import data.bean.Path;
import data.bean.SubwayData;

import java.util.List;
import java.util.Map;

public class PathUtil {

    private SubwayData mSubwayData;

    public PathUtil(SubwayData subwayData) {
        this.mSubwayData = subwayData;
    }

    Path stationsToPath(@NotNull List<String> stations, List<Edge> edges) {
        Path path = new Path();
        if (edges == null) {
            path.addLine(" ");
            path.addStation(stations.get(0), 0);
            path.addStation(stations.get(1), 0);
            return path;
        }
        int[] flags = handleFlag(edges);
        for (int i = 0; i < stations.size() - 1; i++) {
            String station = stations.get(i);
            Edge edge = edges.get(i);
            if (i == 0) {
                List<String> lines;
                if(flags[0] == 0) {
                    lines = mSubwayData.flags2Name(edge.getFlag());
                } else {
                    lines = mSubwayData.flags2Name(flags[0]);
                }
                path.addLine(lines.get(0));
                path.addStation(station, edge.getWeight());
            } else {
                if (flags[i - 1] == 0) {
                    path.addStation(station, edge.getWeight());
                    List<String> lines = mSubwayData.flags2Name(edge.getFlag());
                    path.addLine(lines.get(0));
                    path.addStation(station, 0);
                } else {
                    path.addStation(station, edge.getWeight());
                }
            }
        }
        path.addStation(stations.get(stations.size() - 1), 0);
        return path;
    }

    private int[] handleFlag(List<Edge> edges) {
        int[] result = new int[edges.size()];
        for (int i = 0; i < edges.size() - 1; i++) {
            result[i] = edges.get(i).getFlag() & edges.get(i + 1).getFlag();
        }
        result[edges.size() - 1] = result[edges.size() - 2];
        return result;
    }
}
