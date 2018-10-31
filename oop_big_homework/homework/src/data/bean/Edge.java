package data.bean;

import java.util.Objects;

public class Edge {

    private int id;

    private final String v;

    private final String w;

    private final float weight;

    private int flag;

    public Edge(int id, String v, String w, float weight, int flag) {
        this.id = id;
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.flag = flag;
    }

    public String other(String v) {
        if (Objects.equals(v, this.v)) {
            return this.w;
        } else if (Objects.equals(v, this.w)) {
            return this.v;
        } else {
            return null;
        }
    }

    public float getWeight() {
        return weight;
    }

    public String getOne() {
        return v;
    }

    public void addFlag(int flag) {
        this.flag ^= flag;
    }


    public boolean isMyFlag(int flag){
        return (flag & this.flag) != 0;
    }

    public boolean isMyStation(String station) {
        return Objects.equals(v, station) || Objects.equals(w, station);
    }

    public int getFlag() {
        return flag;
    }

    public int getId() {
        return id;
    }
}
