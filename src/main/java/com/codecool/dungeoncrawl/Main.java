package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Sword;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;

import com.codecool.dungeoncrawl.logic.items.Item;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;


public class    Main extends Application {
    public static final Integer MAX_LOG_LENGTH = 1000;
    GameMap map = MapLoader.loadMap("/map.txt");
    GameMap mapOfLevel2 = MapLoader.loadMap("/map2.txt");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    GridPane uiDashboard = new GridPane();

    StringBuilder logStringBuilder = new StringBuilder(MAX_LOG_LENGTH / 10);


    ScrollPane scrollForLogArea = new ScrollPane();
    TextArea logArea = new TextArea();

    Button pickUp = new Button("Pick Up");
    static List<Item> inventory = new LinkedList<>();
    Button inventoryButton = new Button("Inventory");

    public static List<Item> getInventory() {
        return inventory;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        pushInLog("Log me... \tRight about now\tThe funk soul brother!");
        uiDashboard.setPrefWidth(200);
        uiDashboard.setPadding(new Insets(10));
        uiDashboard.setVgap(10);
        //set minimum width for first column
        ColumnConstraints constraintCol1 = new ColumnConstraints();
        constraintCol1.setMinWidth(70);
        uiDashboard.getColumnConstraints().addAll(constraintCol1);

        uiDashboard.add(new Label("Health: "), 0, 0);
        uiDashboard.add(healthLabel, 2, 0);




        pickUp.setVisible(false);
        pickUp.managedProperty().bind(pickUp.visibleProperty());
        pickUp.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent ->{
            inventory.add(map.getPlayer().getCell().getItem());
            pushInLog("Items: ");
            for (Item item: inventory) {
                pushInLog(item.getClass().getSimpleName());
                map.getPlayer().getCell().setItem(null);
            }
            refresh();
        });


        this.uiDashboard.add(pickUp,0,1);


        GridPane uiBottomPane = new GridPane();



        scrollForLogArea.setMinHeight(80);
        logArea.setPrefWidth(canvas.getWidth());
        scrollForLogArea.setContent(logArea);
//        logArea.textProperty().bind(logTextObject.textProperty());
//        logTextObject.wrappingWidthProperty().bind(scrollForLogArea.widthProperty());




//        scrollForLog.setStyle("-fx-opacity: 1; "+
//                "-fx-background-color: GREEN;");
        logArea.setFocusTraversable(false);
        logArea.setStyle("-fx-opacity: 1;" /* +
                "-fx-background-color: RED;" */
                );
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
                        System.out.println(inventoryToString());
                        Text inventoryContents = new Text (inventoryToString());
                        inventoryContents.setLineSpacing(2.5);
                        dialogBox.getChildren().add(inventoryContents);
                        Scene dialogScene = new Scene(dialogBox, 300,200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                        actionEvent.consume();
                    }
                }
        );
        uiDashboard.add(inventoryButton,0,2);

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
                map.getPlayer().move(1,0);
                refresh();
                break;
        }
    }

    public void pushInLog(String message)
    {
        Integer messageLength = message.length();
        Integer currentLogLength = logStringBuilder.length();

        logStringBuilder.append("\n" + message);
        String newLogText;
        Integer newLogLength = messageLength + currentLogLength;
        if (newLogLength > MAX_LOG_LENGTH) {
            newLogText = logStringBuilder
                    .subSequence(Math.max(0,newLogLength-MAX_LOG_LENGTH), currentLogLength)
                    .toString();
            logStringBuilder.setLength(0);
            logStringBuilder.append(newLogText.substring(MAX_LOG_LENGTH/2));
        }
        else {
            newLogText = logStringBuilder.toString();
        }
        logArea.setFocusTraversable(true);


        logArea.setText(newLogText);    // can bind this to a Text object
        logArea.selectPositionCaret(logArea.getLength());
        logArea.setFocusTraversable(false);
        logArea.deselect();
    }

    private void refresh() {
        if (Main.getInventory().stream().map(Item::getTileName).anyMatch("sword"::equals)) {
            int damage = map.getPlayer().getDamage();
            map.getPlayer().setDamage(damage+20);
        }
        pickUp.visibleProperty().set(map.getPlayer().getCell().getItem() != null);
        inventoryButton.setDisable(inventory.isEmpty());
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

//        pushInLog("Refresh happened!");
        healthLabel.setText("" + map.getPlayer().getHealth());
    }



    private String inventoryToString() {
        StringBuilder contents = new StringBuilder("Items:\n");
        for (Item item: inventory) {
            contents.append(item.getClass().getSimpleName());
            contents.append("\n");
        }

        return contents.toString();
    }
}
