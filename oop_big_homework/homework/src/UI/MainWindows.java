package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


public class MainWindows extends Application {
    private ListView listView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.
                load(getClass().getResource("layout/main_windows.fxml"));
        listView = (ListView) root.lookup("#station_list");
        primaryStage.setTitle("WuHanSubway");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
