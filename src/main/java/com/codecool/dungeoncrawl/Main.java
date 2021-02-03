package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class    Main extends Application {
    public static final Integer MAX_LOG_LENGTH = 1000;
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    GridPane uiDashboard = new GridPane();

    StringBuilder logStringBuilder = new StringBuilder(MAX_LOG_LENGTH / 10);


    ScrollPane scrollForLogArea = new ScrollPane();
    TextArea logArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        pushInLog("Log me... \tRight about now\tThe funk soul brother!");
        uiDashboard.setPrefWidth(200);
        uiDashboard.setPadding(new Insets(10));
        uiDashboard.setVgap(10);

        uiDashboard.add(new Label("Health: "), 0, 0);
        uiDashboard.add(healthLabel, 1, 0);

        Button pickUp = new Button("Pick Up");
        pickUp.setVisible(false);


        uiDashboard.add(pickUp,0,1);


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

        BorderPane borderPane = new BorderPane();


        borderPane.setCenter(canvas);
        borderPane.setBottom(uiBottomPane);
        borderPane.setRight(uiDashboard);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

// Example of getting elements from parent Panes, Scene, etc (in this case uiDashboard)

    private Button getPickUpButton() {
        ObservableList<Node> nodeElementsList;
        nodeElementsList = uiDashboard.getChildren();
        for (Node element : nodeElementsList ) {
            if (element instanceof Button) {
                Button elementAsButton = (Button) element;
                if (elementAsButton.getText() == "Pick Up") {
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
                    .toString() ;
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
        if (getPickUpButton() != null) {
            getPickUpButton().setVisible(map.getPlayer().getCell().getItem() != null);
        }
        pushInLog("Refresh happened!");
        healthLabel.setText("" + map.getPlayer().getHealth());
    }
}
