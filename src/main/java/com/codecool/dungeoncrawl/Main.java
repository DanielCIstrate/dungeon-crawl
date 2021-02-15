package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.ui.GameLog;
import com.codecool.dungeoncrawl.ui.Tiles;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.util.LinkedList;
import java.util.List;






public class    Main extends Application {

    GameMap map = MapLoader.loadMap("/map.txt");
    GameMap mapOfLevel2 = MapLoader.loadMap("/map2.txt");
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


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();


        gameLog.pushInLog("Good luck Angus!");
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
            inventoryList.add(map.getPlayer().getCell().getItem());
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


        borderPane.setCenter(canvas);
        borderPane.setBottom(uiBottomPane);
        borderPane.setRight(this.uiDashboard);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            onKeyPressed(keyEvent);
            if (map.getPlayer().getHealth() <=0)
                scene.setRoot(isOver());
            keyEvent.consume();
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (map.getPlayer().getCell().getType().equals(CellType.GATE)) {
                map = mapOfLevel2;
                refresh();
                keyEvent.consume();
                }
        });



        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if(map.getPlayer().getCell().getType().equals(CellType.GATE_FINAL)) {
                System.out.println("Final");
                Parent over = isOver();
                scene.setRoot(over);
                refresh();
                keyEvent.consume();


            }
        });
                primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
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
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
                refresh();
                break;
            case S:
                Player player = map.getPlayer();
                dbManager.savePlayer(player);
                break;
        }
    }


    private void refresh() {

        pickUp.visibleProperty().set(map.getPlayer().getCell().getItem() != null);
        inventoryButton.setDisable(inventoryList.isEmpty());
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    if (cell.getItem() != null) {
                        Tiles.drawTile(context, cell.getItem(), x, y);
                    } else {
                        Tiles.drawTile(context, cell, x, y);
                    }
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
        } catch (SQLException ex) {
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
