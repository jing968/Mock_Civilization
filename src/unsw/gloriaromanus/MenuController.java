package unsw.gloriaromanus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MenuController {
    @FXML
    private Button new_btn;
    @FXML
    private Button resume_btn;
    private Stage primary_stage;
    private Scene menu;
    private GameState game;

    public MenuController(Stage primary_stage) {
        this.primary_stage = primary_stage;
        this.menu = null;
    }

    public void setMenu(Scene menu) {
        this.menu = menu;
    }

    @FXML
    public void handle_newgame(ActionEvent event) throws Exception {
        game = new GameState();
        game.newgame();
        menu_to_game();        
        
    }


    @FXML
    public void handle_load(ActionEvent event) throws IOException, ClassNotFoundException {
        GameState temp = new GameState();
        game = temp.load();
        menu_to_game();
        
    }

    /**
     * change scene from menu to game
     * @throws IOException
     */
    private void menu_to_game() throws IOException {
        GloriaRomanusController game_controller = new GloriaRomanusController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        //GloriaRomanusController game_controller = loader.getController();
        loader.setController(game_controller);
        game_controller.setGame(this.game);
        Parent game_view = loader.load();
        Scene game_scene = new Scene(game_view);
        game_controller.setGame_scene(game_scene);
        game_controller.setMenu(menu);
        game_controller.setPrimary(primary_stage);
        primary_stage.setScene(game_scene);
        primary_stage.setWidth(800);
        primary_stage.setHeight(700);
        primary_stage.setTitle("Gloira Romanus");
    }
    
    @FXML
    public void initialize(){
        
    }


}
