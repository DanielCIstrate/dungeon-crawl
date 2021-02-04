package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class    Main extends Application {
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Button pickUp = new Button("Pick Up");
    List<Item> inventory = new LinkedList<>();
    Button inventoryButton = new Button("Inventory");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        ui.setVgap(10);
        //set minimum width for first column
        ColumnConstraints constraintCol1 = new ColumnConstraints();
        constraintCol1.setMinWidth(70);
        ui.getColumnConstraints().addAll(constraintCol1);

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 2, 0);



        pickUp.setVisible(false);
        pickUp.managedProperty().bind(pickUp.visibleProperty());
        pickUp.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent ->{
            inventory.add(map.getPlayer().getCell().getItem());
            System.out.println("Items: ");
            for (Item item: inventory) {
                System.out.println(item.getClass().getSimpleName());
                map.getPlayer().getCell().setItem(null);
            }
            refresh();
        });

        ui.add(pickUp,0,1);

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
                        VBox dialogBox = new VBox(20);
//                        System.out.println(inventoryContents);
                        System.out.println(inventoryToString());
                        Text inventoryContents = new Text (inventoryToString());
                        inventoryContents.setLineSpacing(2.5);
                        dialogBox.getChildren().add(inventoryContents);
                        Scene dialogScene = new Scene(dialogBox, 300,200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                }
        );
        ui.add(inventoryButton,0,2);



        BorderPane borderPane = new BorderPane();


        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            onKeyPressed(keyEvent);
            keyEvent.consume();
        });





        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
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

    private void refresh() {
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
