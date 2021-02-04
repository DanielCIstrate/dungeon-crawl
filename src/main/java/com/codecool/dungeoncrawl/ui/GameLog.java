package com.codecool.dungeoncrawl.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class GameLog {

    public static final Integer MAX_LOG_LENGTH = 1000;
    private StringBuilder stringBuilder;
    private TextArea textArea;
    private ScrollPane scrollForText;

    private static GameLog gameLogSingleton;

    private GameLog() {
        stringBuilder = new StringBuilder(MAX_LOG_LENGTH / 10);
        textArea = new TextArea();
        scrollForText = new ScrollPane();
        scrollForText.setMinHeight(80);
        scrollForText.setContent(textArea);
//        logArea.textProperty().bind(logTextObject.textProperty());
//        logTextObject.wrappingWidthProperty().bind(scrollForLogArea.widthProperty());




//        scrollForLog.setStyle("-fx-opacity: 1; "+
//                "-fx-background-color: GREEN;");
        textArea.setFocusTraversable(false);
        textArea.setStyle("-fx-opacity: 1;" /* +
                "-fx-background-color: RED;" */
        );
    }

    public static GameLog getGameLog() {
        if (gameLogSingleton == null) {
            gameLogSingleton = new GameLog();
        }
        return gameLogSingleton;
    }

    public TextArea getTextArea() {
        return this.textArea;
    }

    public ScrollPane getScrollForText() {
        return this.scrollForText;
    }

    public Integer getMaxLogLength() {
        return MAX_LOG_LENGTH;
    }

    public void pushInLog(String message)
    {
        Integer messageLength = message.length();
        Integer currentLogLength = stringBuilder.length();

        stringBuilder.append("\n" + message);
        String newLogText;
        Integer newLogLength = messageLength + currentLogLength;
        if (newLogLength > MAX_LOG_LENGTH) {
            newLogText = stringBuilder
                    .subSequence(Math.max(0,newLogLength-MAX_LOG_LENGTH), currentLogLength)
                    .toString();
            stringBuilder.setLength(0);
            stringBuilder.append(newLogText.substring(MAX_LOG_LENGTH/2));
        }
        else {
            newLogText = stringBuilder.toString();
        }
        textArea.setFocusTraversable(true);


        textArea.setText(newLogText);    // can bind this to a Text object
        textArea.selectPositionCaret(textArea.getLength());
        textArea.setFocusTraversable(false);
        textArea.deselect();
    }
}
