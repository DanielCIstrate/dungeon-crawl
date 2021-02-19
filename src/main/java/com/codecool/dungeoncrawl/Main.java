package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.impex.DataModelSave;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.ui.GameLog;
import com.codecool.dungeoncrawl.ui.Tiles;
import com.codecool.dungeoncrawl.logic.MapExportImport;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.security.InvalidKeyException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Main extends Application {
    List<GameMap> levels = new LinkedList<>();
    GameMap mapOfLevel1 = MapLoader.loadMap("/map.txt");
    GameMap mapOfLevel2 = MapLoader.loadMap("/map2.txt");
    GameMap map = mapOfLevel1;




    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    GameDatabaseManager dbManager;

    Label damageLabel = new Label();

    GridPane uiDashboard = new GridPane();


    GameLog gameLog = GameLog.getGameLog();
    ScrollPane scrollForLogArea = gameLog.getScrollForText();
    TextArea logArea = gameLog.getTextArea();

    Button pickUp = new Button("Pick Up");

    Inventory inventoryObject = Inventory.getInventory();
    List<Item> inventoryList = inventoryObject.getList();
    Button inventoryButton = new Button("Inventory");
    List<Actor> actorsOnMap;
    List<Item> itemsOnMap;
    private boolean hasSucceededConnection = false;
    private boolean registerDefaultsInDb = false;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        levels.add(mapOfLevel1);
        levels.add(mapOfLevel2);
        map = levels.get(0);


        setupDbManager();
        if (hasSucceededConnection) {
            if (registerDefaultsInDb) {
                levels.forEach(level -> {
                    itemsOnMap = level.getItemList();
                    itemsOnMap.forEach(item -> dbManager.registerDefaultItem(item));
                });
            }
        }


        //TODO - add a UI element to set name
        Player player = map.getPlayer();
        player.setName("Zorro");

        gameLog.pushInLog("Good luck " + player.getName());

        refresh();
        uiDashboard.setPrefWidth(200);
        uiDashboard.setPadding(new Insets(2));
        uiDashboard.setVgap(10);
        uiDashboard.add(new Label("Health: "), 0, 0);
        uiDashboard.add(healthLabel, 2, 0);

        uiDashboard.add(new Label("Damage: "), 0, 1);
        uiDashboard.add(damageLabel, 2, 1);





        pickUp.setVisible(false);
        pickUp.managedProperty().bind(pickUp.visibleProperty());
        pickUp.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent ->{
            Item pickedUpItem = map.getPlayer().getCell().getItem();
            pickedUpItem.isInInventory = true;
            pickedUpItem.setCell(null);
            inventoryList.add(pickedUpItem);
            gameLog.pushInLog("Items: ");
            for (Item item: inventoryList) {
                gameLog.pushInLog(item.getClass().getSimpleName());
                map.getPlayer().getCell().setItem(null);
            }
            refresh();
        });


        this.uiDashboard.add(pickUp,0,2);


        GridPane uiBottomPane = new GridPane();



        logArea.setPrefWidth(canvas.getWidth());    // the size of the child determines the size
                                                    // the parent (scrollLogArea)
//        uiBottomPane.setStyle("-fx-background-color: BLUE;");
        uiBottomPane.getChildren().add(scrollForLogArea);

//
        inventoryButton.setDisable(true);
        inventoryButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        final Stage dialog = new Stage();
                        //se the window not to obstruct the main window
                        dialog.initModality(Modality.NONE);
                        dialog.initOwner(primaryStage);
                        dialog.setTitle("Inventory");
                        VBox dialogBox = new VBox(20);
                        System.out.println(inventoryObject.toString());
                        Text inventoryContents = new Text (inventoryObject.toString());
                        inventoryContents.setLineSpacing(2.5);
                        dialogBox.getChildren().add(inventoryContents);
                        Scene dialogScene = new Scene(dialogBox, 300,200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                        actionEvent.consume();
                    }
                }
        );
        uiDashboard.add(inventoryButton,0,3);

//
//        unlockButton.setDisable(true);
//        unlockButton.setVisible(false);
//        unlockButton.managedProperty().bind(unlockButton.visibleProperty());
//



        BorderPane borderPane = new BorderPane();

        BorderPane dashboardPane = new BorderPane();
        dashboardPane.setTop(this.uiDashboard);

        borderPane.setCenter(canvas);
        borderPane.setBottom(uiBottomPane);
        borderPane.setRight(dashboardPane);

        // Save, Load, Export, Import interfaces
        VBox vbMenuOptions = new VBox();
        HBox hbExportOptions = new HBox();
        dashboardPane.setBottom(vbMenuOptions);
        //Export Button
        Button exportButton = new Button("Export");
        exportButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                          Stage dialog =  new Stage();
                          dialog.initModality(Modality.APPLICATION_MODAL);
                          dialog.initOwner(primaryStage);
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setInitialDirectory(new File("src/main/resources/exports"));


                        fileChooser.setTitle("Exporting game...");
                        fileChooser.setInitialFileName("my-fantastic-game");
                        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON file","*.json"),
                                new FileChooser.ExtensionFilter("All Files", "*.*"));
                        File file = fileChooser.showSaveDialog(dialog);

                        if (file != null) {
                            try {
                                fileChooser.setInitialDirectory(file.getParentFile());//save to initial directory
                                String filenameTmp = file.getName();
                                //removing if json exists in filenameTmp remove it
                                String[] ArrayOfFilename = filenameTmp.split("\\.");
                                List<String> ListOfFilename = new ArrayList<String>(Arrays.asList(ArrayOfFilename));
                                ListOfFilename.remove("json");
                                ArrayOfFilename = ListOfFilename.toArray(new String[0]);

                                StringBuilder buildString = new StringBuilder(ArrayOfFilename[0]);
                                if (ArrayOfFilename.length > 1) {
                                    for (int i = 1; i < ArrayOfFilename.length;i++) {
                                        buildString.append(".");
                                        buildString.append(ArrayOfFilename[i]);
                                    }
                                }
                                String filename = buildString.toString();
                                System.out.println(Arrays.toString(filename.split("\\.")));
                                MapExportImport.writeState(map,Inventory.getInventory(),Common.filePath + filename + ".json" );
                                gameLog.pushInLog("Game has been exported as " + filename);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        event.consume();



                    }
                }
        );
        //Import Button
        Button importButton = new Button("Import");
        importButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(final ActionEvent event) {
                 Stage dialog = new Stage();
                 dialog.initModality(Modality.APPLICATION_MODAL);
                 dialog.initOwner(primaryStage);
                 FileChooser fileChooser = new FileChooser();
                 fileChooser.setInitialDirectory(new File("src/main/resources/exports"));


                 fileChooser.setTitle("Importing game...");
                 fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
                 File file = fileChooser.showOpenDialog(dialog);

                 try {
                     fileChooser.setInitialDirectory(file.getParentFile());//save to initial directory
                     String filename = file.getName();
                     if (!filename.equals("")) {
                         //checks if extension is json
                         String[] filenameSplit = filename.split("\\.");
                         String extension = filenameSplit[filenameSplit.length-1];
                         if (extension.equals("json")) {
                             String path = "src/main/resources/exports/";
                             map = MapExportImport.readExport(path + filename).getExportedMapState();
                             inventoryList.clear();
                             inventoryList.addAll(MapExportImport.readExport(path + filename).getExportedInventory().getList());
                             gameLog.pushInLog("File " + filename + " has been imported");
                             System.out.println("import successful");
                             refresh();
                         }
            //                                dialog.setTitle(file.getName());
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }


            //                        Scene scene = new Scene(new VBox(), 300, 250);
            //                        dialog.setScene(scene);
            //                        dialog.show();
                 event.consume();


             }
            }
        );

        // Export Import Hbox
        hbExportOptions.setSpacing(5);
        hbExportOptions.getChildren().addAll(exportButton,importButton);

        HBox hbSaveOptions = new HBox();
        hbSaveOptions.setManaged(false);
        vbMenuOptions.getChildren().addAll(hbExportOptions,hbSaveOptions);




        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
//        scene.setOnKeyPressed(this::onKeyPressed);
//        scene.setOnKeyReleased(this::onKeyReleased);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            onKeyPressed(keyEvent);
            if (map.getPlayer().getHealth() <=0) {
                scene.setRoot(isOver());
            }
            if (map.getPlayer().getCell().getType().equals(CellType.GATE)) {
                map = fromLevelAtoLevelB(levels,map,Common.level,Common.level + 1);
                Common.level++;
                updateMaximumLevel(Common.level);
            } else if (map.getPlayer().getCell().getType().equals(CellType.GATE_UP)) {
                map = fromLevelAtoLevelB(levels,map,Common.level,Common.level - 1);
                Common.level--;
            }
            if(map.getPlayer().getCell().getType().equals(CellType.GATE_FINAL)) {
                System.out.println("Final");
                Parent over = isOver();
                scene.setRoot(over);
            }
            keyEvent.consume();
            refresh();
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            onKeyReleased(keyEvent);
            keyEvent.consume();
        });
        ;


                primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void updateMaximumLevel(int level) {
        if (level > Common.maxLevel) {
            Common.maxLevel = level;
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        KeyCombination saveCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (saveCombination.match(keyEvent)) {
            gameLog.pushInLog("Ctrl+S was pressed!");
            doGameSaveLogic();
        }
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void doGameSaveLogic() {
        GameState modelToSave = DataModelSave.saveGameState(
                map.getPlayer(),
                map.getItemList(),
                Common.maxLevel,
                Common.level
        );
        dbManager.dumpGameState(modelToSave);
    }

    public GameMap fromLevelAtoLevelB(List<GameMap> levels ,GameMap currentMap, int a, int b) {
        GameMap newMap;
        levels.set(a,currentMap);
        newMap = levels.get(b);
        levels.get(a).getPlayer().copyAttributesTo(newMap.getPlayer());
        return newMap;
    }

// Example of getting elements from parent Panes, Scene, etc (in this case uiDashboard)

    public Parent isOver(){
        Pane over_screen = new Pane();

        VBox elements = new VBox();
        elements.setPrefSize(1280, 720);
        elements.setAlignment(Pos.CENTER);
        if (map.getPlayer().getHealth() <= 0) {
            Text gameOver = new Text("You died! Please try again!");
            elements.getChildren().addAll(gameOver);

        } else {
            Text gameOver = new Text("Well Done!\nYou have finished the game!");
            elements.getChildren().addAll(gameOver);
        }

        over_screen.getChildren().add(elements);


        return over_screen;

    }

    private Button getPickUpButton() {
        ObservableList<Node> nodeElementsList;
        nodeElementsList = uiDashboard.getChildren();
        for (Node element : nodeElementsList ) {
            if (element instanceof Button) {
                Button elementAsButton = (Button) element;
                if (elementAsButton.getText().equals("Pick Up")) {
                    return elementAsButton;
                }
            }
        }
        return null;
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                Common.turnCounter++;
                map.getPlayer().move(0, -1);
                break;
            case DOWN:
                Common.turnCounter++;
                map.getPlayer().move(0, 1);
                break;
            case LEFT:
                Common.turnCounter++;
                map.getPlayer().move(-1, 0);
                break;
            case RIGHT:
                Common.turnCounter++;
                map.getPlayer().move(1, 0);
                break;
//            case S:
//                Player player = map.getPlayer();
//                dbManager.savePlayer(player);
//                break;
        }
    }



    private void refresh() {
        actorsOnMap = map.getActorList();
        for (Actor someActor : actorsOnMap) {

            someActor.doMoveLogic();
        }
        pickUp.visibleProperty().set(map.getPlayer().getCell().getItem() != null);
        inventoryButton.setDisable(inventoryList.isEmpty());
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }


//        gameLog.pushInLog("Refresh happened!");
        healthLabel.setText("" + map.getPlayer().getHealth());
        damageLabel.setText("" + map.getPlayer().getCalculatedDamageString());

    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
            hasSucceededConnection = true;
        } catch (SQLException | InvalidKeyException ex) {
            hasSucceededConnection = false;
            System.out.println(ex.getMessage());
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
