package data;

import data.bean.Path;
import data.bean.SubwayData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataModel {
    private static final String PATH = "subway.txt";

    private SubwayData mData;

    private FileReaderUtil fileReaderUtil;

    private boolean isDataOk;

    public DataModel() {
        fileReaderUtil = new FileReaderUtil();
        initData();
    }

    private void initData() {
        fileReaderUtil.readFile(PATH, new CallBack<SubwayData>() {
            @Override
            public void onSuccess(SubwayData data) {
                mData = data;
                isDataOk = true;
                System.out.println("init data successful");
            }

            @Override
            public void onFailed(String s) {
                System.out.println(s);
            }
        });
    }

    /**
     * get all stations of a line
     * @param lineName
     * @param endStation
     * @return the station list
     */
    public List<String> getStationsOfLine(String lineName, String endStation) {
        LinkedList<String> result = new LinkedList<>();
        String station = endStation;
        String lastStation = endStation;
        int line = mData.getLineFlag(lineName);
        result.addFirst(endStation);
        while ((station = mData.getNextStationOfLine(station, lastStation, line)) != null) {
            lastStation = result.getFirst();
            result.addFirst(station);
        }
        return result;
    }

    /**
     * get the shortest of two station
     * @param start
     * @param end
     * @return the list of station name
     */
    public Path getPath(String start, String end){
        FindPathUtil findPathUtil = new FindPathUtil(mData);
        return findPathUtil.shortestPath(start, end);
    }

    /**
     * get line name by station name
     * @param station
     * @return
     */
    public List<String> linesNameOfStation(String station){
        return mData.getLineNameOfStation(station);
    }

    /**
     * get all station of the subway system
     * @return
     */
    public List<String> getAllStation(){
        ArrayList<String> list = new ArrayList<>(mData.getmStation());
        Collections.sort(list);
        return list;
    }


    /**
     * all lines name
     * @return
     */
    public List<String> getAllLineName(){
        return mData.getAllLines();
    }


    /**
     * get the line's start station and end station
     * @param lineName
     * @return
     */
    public List<String> getLineStartAndEnd(String lineName){
        return mData.getStartAndEndStation().get(lineName);
    }

}
