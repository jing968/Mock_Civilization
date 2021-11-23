package unsw.gloriaromanus;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SubMenuController {
    @FXML
    private Label output;
    @FXML
    private Button btn_menu;
    @FXML
    private Button resume_btn;
    @FXML
    private Button exit_bth;
    @FXML
    private Button save_bth;
    private Stage primary;
    private Stage secondary;
    private Scene menu;
    private Scene game_scene;
    private GameState game;

    public void setGame(GameState game) {
        this.game = game;
    }

    public SubMenuController(Stage p, Stage s, Scene menu){
        primary = p;
        secondary = s;
        this.menu = menu;
    }

    public void setGame_scene(Scene game_scene) {
        this.game_scene = game_scene;
    }

    @FXML
    private void handle_resume(ActionEvent event){
        secondary.close();
    }

    @FXML 
    private void handle_save(ActionEvent event) throws IOException {
        game.save();
        output.setText("Your progess has been saved !");
    }
    
    @FXML
    private void handle_menu(ActionEvent event) {
        secondary.close();
        primary.setTitle("Gloira Romanus Menu");
        primary.setWidth(600);
        primary.setHeight(379);
        primary.setScene(menu);
    }

    @FXML
    private void handle_exit(ActionEvent event){
        primary.close();
        secondary.close();
    }
}
