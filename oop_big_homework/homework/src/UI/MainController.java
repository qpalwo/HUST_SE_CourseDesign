package UI;

import data.DataModel;
import data.bean.Path;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;


public class MainController {
    private DataModel dataModel;

    @FXML
    private ListView<String> station_list;

    @FXML
    private TextArea station_text;

    @FXML
    private ListView<String> line_list;

    @FXML
    private ListView<String> line_station_list;

    @FXML
    private ListView<String> line_end_list;

    @FXML
    private ListView<String> start_station;

    @FXML
    private ListView<String> end_station;

    @FXML
    private TextArea ending_show;

    @FXML
    private TextField normal_price;

    @FXML
    private TextField daily_price;

    @FXML
    private TextField wuhancard_price;

    public MainController() {
        dataModel = new DataModel();
    }

    @FXML
    protected void initStationView() {
        ObservableList<String> strings = FXCollections
                .observableArrayList(dataModel.getAllStation());
        station_list.setItems(strings);
        ObservableValue<String> station = station_list.getSelectionModel().selectedItemProperty();
        station.addListener((observable, oldValue, newValue) -> {
            List<String> lines = dataModel.linesNameOfStation(newValue);
            StringBuffer stringBuffer = new StringBuffer();
            for (String temp : lines) {
                stringBuffer.append(temp)
                        .append("\n");
            }
            station_text.setText(stringBuffer.toString());
        });
    }

    @FXML
    protected void initLineList() {
        ObservableList<String> lines = FXCollections
                .observableArrayList(dataModel.getAllLineName());
        line_list.setItems(lines);
        ObservableValue<String> lineSelected = line_list.getSelectionModel().selectedItemProperty();
        lineSelected.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> startStation = FXCollections
                    .observableArrayList(dataModel.getLineStartAndEnd(newValue));
            line_end_list.setItems(startStation);
            ObservableValue<String> station = line_end_list.getSelectionModel().selectedItemProperty();
            station.addListener((observable1, oldValue1, newValue1) -> {
                ObservableList<String> stations = FXCollections
                        .observableArrayList(dataModel.getStationsOfLine(newValue, newValue1));
                line_station_list.setItems(stations);
            });
        });

    }

    @FXML
    protected void initGuideList() {
        StartAndEnd startAndEnd = new StartAndEnd();
        List<String> stations = dataModel.getAllStation();
        ObservableList<String> stationsData = FXCollections
                .observableArrayList(stations);
        ObservableList<String> endData = FXCollections
                .observableArrayList(stations);
        start_station.setItems(stationsData);
        end_station.setItems(endData);
        start_station.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    ending_show.setText("");
                    normal_price.setText("");
                    daily_price.setText("");
                    wuhancard_price.setText("");
                    startAndEnd.setStart(newValue);
                });

        end_station.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    startAndEnd.setEnd(newValue);
                    if (startAndEnd.getStart() != null) {
                        Path p = dataModel.getPath(startAndEnd.getStart(), newValue);
                        ending_show.setText(p.toString());
                        normal_price.setText(p.price(Path.CALCU_NORMAL) + "");
                        daily_price.setText(p.price(Path.CALCU_DAILY) + "");
                        wuhancard_price.setText(p.price(Path.CALCU_WUHAN) + "");
                    }
                });
    }

    private class StartAndEnd {
        private String start;

        private String end;

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }
}
