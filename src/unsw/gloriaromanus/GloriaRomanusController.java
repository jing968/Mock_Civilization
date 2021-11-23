package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unsw.gloriaromanus.infrastructure.TroopProduction;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;
  // Initial Stuff
  @FXML
  private TextArea txtWinningObjectives;
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextField GameStateTurns;

  // Faction Stuff
  @FXML
  private TextField FactionString;
  @FXML
  private TextField FactionGold;
  @FXML
  private TextField FactionWealth;
  @FXML
  private Button provinces;

  // Faction Provinces
  @FXML
  private VBox FactionProvinceVBox;
    @FXML
    private TextArea FactionProvinceDescription;

  // Your Province Stuff
  @FXML
  private TextField ProvinceString;
  @FXML
  private TextField ProvinceWealth;
  @FXML
  private TextField ProvinceTaxRate;
  
  // Province Structure Details
  @FXML 
  private VBox ProvinceStructureDetails;
    @FXML
    private TextField txtTroopProduction;
    @FXML
    private TextField txtFarm;
    @FXML
    private TextField txtWall;
    @FXML
    private TextField txtRoad;
    @FXML
    private TextField StructureCommand;
    @FXML
    private TextArea StructureQueue;
  
  // Province's Unit/s
  @FXML
  private VBox UnitVBox;
    @FXML
    private TextField txtTroopProductionUnits;
    @FXML
    private TextArea ProvinceUnits;
    @FXML
    private TextField UnitCommand;
    @FXML
    private TextArea ProvinceUnitsQueue;
    @FXML
    private TextField UnitProvinceCommand;

  // Enemy Province Stuff
  @FXML
  private TextField EnemyProvinceString;
  @FXML
  private TextField EnemyWall;
  @FXML
  private TextArea EnemyUnits;

  // Names
  @FXML
  private VBox NamesVBox;



  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private GameState game;
  private Faction faction;
  private Province a;
  private Province b;


  private Stage primary;
  private Scene menu;
  private Scene game_scene;

  public void setGame(GameState game) {
    this.game = game;
  }

  public void setGame_scene(Scene game_scene) {
    this.game_scene = game_scene;
  }

  public void setPrimary(Stage primary) {
    this.primary = primary;
  }

  public void setMenu(Scene menu) {
    this.menu = menu;
  }

  @FXML
  private void handle_menu(ActionEvent event) throws IOException {
    Stage secondary = new Stage();
    secondary.setTitle("Menu");
    secondary.initModality(Modality.APPLICATION_MODAL);
    SubMenuController invcontroller = new SubMenuController(primary,secondary,menu);
    invcontroller.setGame_scene(game_scene);
    invcontroller.setGame(game);
    FXMLLoader invloader = new FXMLLoader(getClass().getResource("submenu.fxml"));           
    invloader.setController(invcontroller);
    Parent inv = (Parent)invloader.load();
    Scene invscene = new Scene(inv);
    inv.requestFocus();
    secondary.setScene(invscene);
    secondary.showAndWait();
  }

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
     hideVBoxes();
     provinceToOwningFactionMap = getProvinceToOwningFactionMap();   

    getFaction();
    updateFaction();
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;
    GameStateTurns.setText("Turns: " + game.getTurns());
    FactionProvinceDescription.setText(faction.toString());

    game.buildCondition();
    WinCondition w = game.getCond();

    txtWinningObjectives.setText(w.toString());
    initializeProvinceLayers();
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      String result = faction.fightProvince(humanProvince, enemyProvince);
      printMessageToTerminal(result);
      if (result.equals("attack")) {
        provinceToOwningFactionMap.put(enemyProvince, humanFaction);
      }

      resetSelections();  // reset selections in UI
      addAllPointGraphics(); // reset graphics

    }
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n", 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        s = retrieveMarkerSymbol(faction);
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete.
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");


                if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  a = game.getProvince(province);
                  setProvinceDetails();
                  setProvinceStructureDetails();
                  setProvinceUnitsDetails();
                  
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  b = game.getProvince(province);
                  setEnemyProvinceDetails();
                }

                featureLayer.selectFeature(f);                
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  public void setProvinceDetails() {
    ProvinceString.setText("Province: " + a.getProvince());
    ProvinceWealth.setText("Wealth: " + a.getWealth());
    ProvinceTaxRate.setText(a.getTaxRate());
  }

  public void setProvinceStructureDetails() {
    Structure s = a.getStructures();
    txtTroopProduction.setText("troopProduction: " + s.troopToString());
    txtFarm.setText("farm: " + s.farmToString());
    txtWall.setText("wall: " + s.wallToString());
    txtRoad.setText("road: " + s.roadToString());
    StructureQueue.setText(s.queueToString());
  }

  public void setProvinceUnitsDetails() {
    ProvinceUnits.setText(a.toStringUnits());
    Structure s = a.getStructures();
    txtTroopProductionUnits.setText("troopProduction: " + s.troopToString());
    TroopProduction t = s.getTroopProduction();
    if (t == null) return;
    ProvinceUnitsQueue.setText(t.queueToString());
  }

  public void setEnemyProvinceDetails() {
    EnemyProvinceString.setText("Province: " + b.getProvince());
    EnemyWall.setText("Wall: " + b.walltype());
    EnemyUnits.setText("Units: " + b.toStringUnits());
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // Create new faction add it to game, and set gameState for faction
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private void resetSelections(){
    if (currentlySelectedEnemyProvince != null) featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince));
    if (currentlySelectedHumanProvince != null) featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    resetEnemy();
    resetStructure();
    resetProvince();
    resetUnits();
  }

  public void resetProvince() {
    ProvinceString.setText("Province: ");
    ProvinceWealth.setText("Wealth: ");
    ProvinceTaxRate.setText("Tax Rate: ");
    ProvinceUnits.setText("");
    UnitCommand.setText("Usage: 'troop a, troop b, troop c, ...'");
  }


  public void resetUnits() {
    ProvinceUnits.setText("");
    txtTroopProductionUnits.setText("TroopProduction");
    UnitCommand.setText("Usage: 'troop a, troop b, troop c, ...'");
    UnitProvinceCommand.setText("Province: (Only for Move)");
    ProvinceUnitsQueue.setText("");
  }

  public void resetStructure() {
    txtTroopProduction.setText("troopProduction: ");
    txtFarm.setText("farm: ");
    txtWall.setText("wall: ");
    txtRoad.setText("road: ");
    StructureCommand.setText("Usage: 'troopProduction, farm...'");
  }

  public void resetEnemy() {
    EnemyProvinceString.setText("Province: ");
    EnemyWall.setText("Wall: ");
    EnemyUnits.setText("Units: ");
  }

  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  /**
   * Go to the next faction
   * If the player has reach the conditions
   * Then end the game
   * 
   * @param event 
   */
  @FXML
  public void handleEndTurn(ActionEvent event) {
    // Check if player has reached the conditions
    if (faction.reachedConditions()) {
      txtWinningObjectives.setText(faction.toString() + "has won the game!");

      try {
        Thread.sleep(5 * 1000);
      } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
      }

      primary.close();
    }

    // Iterate to next player
    game.addIterator();

    // Update turn on next cycle
    if (game.getIterator() == 0) GameStateTurns.setText("Turns: " + game.getTurns());

    // Update Everything
    getFaction();
    updateFaction();
    resetSelections();
    hideVBoxes();
    FactionProvinceDescription.setText(faction.toString());
    output_terminal.setText("");
  }

  public void getFaction() {
    faction = game.getFactionObj();
    humanFaction = faction.getFaction();
  }

  public void updateFaction() {
    FactionString.setText("Faction: " + faction.getFaction());
    FactionGold.setText("Total Gold: " + faction.getGold());
    FactionWealth.setText("Total Wealth: " + faction.getWealth());
  }


  @FXML
  public void handleDisplayStructures() {
    hideVBoxes();
    ProvinceStructureDetails.setVisible(true);

  }

  @FXML 
  public void handleDisplayUnits() {
    hideVBoxes();
    UnitVBox.setVisible(true);
  }

  @FXML
  public void handleTaxRate() {
    String taxRate = ProvinceTaxRate.getText();
    if (currentlySelectedHumanProvince == null) return;
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    printMessageToTerminal(faction.setTaxRate(humanProvince, taxRate));
    updateFaction();
    setProvinceDetails();
  }

  @FXML
  public void handleDisplayProvinces() {
    FactionProvinceVBox.setVisible(true);
  }

  public void hideVBoxes() {
    FactionProvinceVBox.setVisible(false);
    ProvinceStructureDetails.setVisible(false);
    UnitVBox.setVisible(false);
    NamesVBox.setVisible(false);
  }

  @FXML
  public void handleBuildInfrastructure() {
    // Take input from command and run buildInfrastructure in faction
    String command = StructureCommand.getText();
    if (currentlySelectedHumanProvince == null) return;
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    printMessageToTerminal(faction.buildInfrastructure(humanProvince, command));
    updateFaction();
    setProvinceStructureDetails();
  }

  @FXML
  public void handleUpgradeInfrastructure() {
    String command = StructureCommand.getText();
    if (currentlySelectedHumanProvince == null) return;
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    printMessageToTerminal(faction.upgradeInfrastructure(humanProvince, command));
    updateFaction();
    setProvinceStructureDetails();
  }

  @FXML
  public void handleMovement() {
    String units = UnitCommand.getText();
    String dest = UnitProvinceCommand.getText();
    if (currentlySelectedHumanProvince == null) return;
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    printMessageToTerminal(faction.moveTroop(humanProvince, dest, units));
    setProvinceUnitsDetails();
  }

  @FXML
  public void handleTrainUnit() {
    String command = UnitCommand.getText();
    if (currentlySelectedHumanProvince == null) return;
    String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
    printMessageToTerminal(faction.trainUnit(humanProvince, command));
    updateFaction();
    setProvinceUnitsDetails();
    setProvinceStructureDetails();
  }

  @FXML
  public void clearSpace() {
    hideVBoxes();
    resetSelections();
  }

  @FXML
  public void displayNames() {
    NamesVBox.setVisible(true);
  }


  private PictureMarkerSymbol retrieveMarkerSymbol(String faction) {
    switch (faction) {
      case "Gaul":
        return new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
      case "Rome":
        // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
        return new PictureMarkerSymbol("images/legionary.png");
      case "Carthaginians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Carthage/CathageFlag.png");
      case "Seleucid Empire":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Archerman/Archer_Man_NB.png");
      case "Pontus":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Camel/Alone/CamelAlone_NB.png"); 
      case "Celtic Britons":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png");
      case "Macedonians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Chariot/Chariot_NB.png"); 
      case "Parthians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Crossbowman/Crossbowman_NB.png"); 
      case "Spanish":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Spanish/SpanishFlag.png"); 
      case "Numidians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flagbearer/FlagBearer_NB.png"); 
      case "Amenians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Hoplite/Hoplite_NB.png");
      case "Egypians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Egyptian/EgyptianFlag.png"); 
      case "Germanics":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Swordsman/Swordsman_NB.png");
      case "Greek City States":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Slingerman/Slinger_Man_NB.png"); 
      case "Thracians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Spearman/Spearman_NB.png"); 
      case "Dacians":
        return new PictureMarkerSymbol("images/CS2511Sprites_No_Background/NetFighter/NetFighter_NB.png"); 
    }
    return null; // Shouldn't happen.
  }

}
