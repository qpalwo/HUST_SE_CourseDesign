package data;

import data.bean.SubwayData;

import java.io.BufferedReader;
import java.io.FileReader;

public class FileReaderUtil {

    private static final int TYPE_NULL = 0;

    private static final int TYPE_DATE = 1;

    private static final int TYPE_LINE = 2;

    public void readFile(String path, CallBack<SubwayData> callBack) {
        SubwayData subwayData = new SubwayData();
        try {
            boolean isLine = false;
            String endStation = null;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String tempString;
            String lineName = "";
            while ((tempString = bufferedReader.readLine()) != null) {
                switch (checkType(tempString)) {
                    case TYPE_NULL:
                        continue;
                    case TYPE_DATE:
                        StationDataUtil util = handleStationData(tempString);
                        subwayData.addEdge(util.getStation1(),
                                util.getStation2(),
                                util.getDistance(),
                                subwayData.getLineFlag(lineName));
                        endStation = util.station2;
                        if(isLine){
                            subwayData.addStart(lineName, util.station1);
                            isLine = false;
                        }
                        break;
                    case TYPE_LINE:
                        isLine = true;
                        if(endStation != null){
                            subwayData.addEnd(lineName, endStation);
                        }
                        int lineNum = tempString.indexOf("线");
                        lineName = tempString.substring(0, lineNum + 1);
                        break;
                }
            }
            if (endStation != null)
                subwayData.addEnd(lineName, endStation);
            callBack.onSuccess(subwayData);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            callBack.onFailed("init data failed");
        }
    }

    private StationDataUtil handleStationData(String data) {
        return new StationDataUtil(data);
    }

    private int checkType(String data) {
        if (data.length() < 2) {
            return TYPE_NULL;
        } else if (data.contains("站点间距")) {
            return TYPE_LINE;
        } else if (data.charAt(0) == '站') {
            return TYPE_NULL;
        } else {
            return TYPE_DATE;
        }
    }

    private class StationDataUtil {
        private String station1;

        private String station2;

        private float distance;

        public StationDataUtil(String s) {
            String[] data = s.split("\t");
            distance = Float.valueOf(data[1]);
            String[] stations = data[0].split("---");
            station1 = stations[0];
            station2 = stations[1];
        }

        public String getStation1() {
            return station1;
        }

        public String getStation2() {
            return station2;
        }

        public float getDistance() {
            return distance;
        }
    }
}
