package unsw.gloriaromanus;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuApplication extends Application {

    @Override
    public void start(Stage primary_stage) throws Exception {
        FXMLLoader loader = new FXMLLoader (getClass().getResource("menu.fxml"));
        MenuController controller = new MenuController(primary_stage);
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        // pass menu reference to controller
        controller.setMenu(scene);
        primary_stage.setTitle("Gloria Romanus Menu");
        primary_stage.setScene(scene);
        primary_stage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
}
