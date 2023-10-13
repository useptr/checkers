package com.example.checkers;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;
import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainScreen {
    private double width=0;
    private double height=0;
    private Stage stage = null;
    private Scene scene = null;
    private VBox rootVBox = new VBox();
    private MenuBar menuBar = new MenuBar();
    private Menu gameMenu = new Menu("Игра");
    private MenuItem restartMenuItem = new MenuItem("Сбросить Игру");
    private MenuItem resetMenuItem = new MenuItem("Сбросить Поле");
    private MenuItem importMenuItem = new MenuItem("Импорт");
    private MenuItem exportMenuItem = new MenuItem("Экспорт");
    private VBox infoVBox = new VBox();
    private Label isTurnLabel = new Label("");
    private Label scoreLabel = new Label("0 : 0");
   private StackPane fieldStackPane = new StackPane();
    private GridPane fieldGridPane = new GridPane();
    private final Image crownImage = new Image(getClass().getResource("/assets/crown.png").toString(), 50, 50, true, false);

    public void setBoardMouseHandler(EventHandler<MouseEvent> boardMouseHandler) {
        this.boardMouseHandler = boardMouseHandler;
    }

    private EventHandler<MouseEvent> boardMouseHandler = null;
    public MainScreen(Stage stage, double width, double height) {
        this.width = width;
        this.height = height;
        this.stage = stage;

        gameMenu.getItems().add(restartMenuItem);
        gameMenu.getItems().add(resetMenuItem);
        gameMenu.getItems().add(importMenuItem);
        gameMenu.getItems().add(exportMenuItem);
        menuBar.getMenus().add(gameMenu);
        rootVBox.getChildren().add(menuBar);

        infoVBox.setAlignment(Pos.CENTER);
        infoVBox.getChildren().add(isTurnLabel);
        infoVBox.getChildren().add(scoreLabel);
        rootVBox.getChildren().add(infoVBox);

        fieldGridPane.setAlignment(Pos.CENTER);
        fieldStackPane.getChildren().add(fieldGridPane);
        rootVBox.getChildren().add(fieldStackPane);
        fieldStackPane.prefHeightProperty().bind(rootVBox.heightProperty().subtract(menuBar.heightProperty()));

        scene = new Scene(rootVBox, this.width, this.height);
        this.stage.setTitle("Checkers");
        this.stage.setScene(scene);
        this.stage.show();
    }
    public Stage getStage() {
        return stage;
    }
    public void drawPieces(Board board) {
        Piece[][] field = board.field();
        for (Node node : fieldGridPane.getChildren()) {
             int x = fieldGridPane.getColumnIndex(node);
             int y = fieldGridPane.getRowIndex(node); 
             StackPane pane = (StackPane)node;
             if (field[y][x] == null) {
                 pane.getChildren().clear();
             } else {
                 drawPiece(pane, field[y][x].getColor(), field[y][x].getType());
             }
        }
    }
    private String getBgColor(int count) {
        String color = "#40352e";
        if (count%2==0)
            color = "d6cabf";
        return color;
    }
    private String getPieceColor(PieceColor color) {
        String pieceColor = "white";
        if (color == PieceColor.BLACK)
            pieceColor = "black";
        return pieceColor;
    }
    private void drawChecker(StackPane pane, PieceColor pieceColor) {
        pane.getChildren().clear();
        Circle circle = new Circle();
        circle.radiusProperty().bind(Bindings.min(pane.widthProperty(), pane.heightProperty()).divide(2.2));
        String color = getPieceColor(pieceColor);  
        circle.setStyle("-fx-fill: " + color + ";");
        pane.getChildren().add(circle);
    }
    private void drawKing(StackPane pane, PieceColor pieceColor) {
        pane.getChildren().clear();
        Circle circle = new Circle();
        circle.radiusProperty().bind(Bindings.min(pane.widthProperty(), pane.heightProperty()).divide(2.2));
        String color = getPieceColor(pieceColor);
        circle.setStyle("-fx-fill: " + color + ";");
        pane.getChildren().add(circle);
        ImageView imageView = new ImageView(crownImage);

        imageView.fitWidthProperty().bind(pane.widthProperty().divide(1.5));
        imageView.fitWidthProperty().bind(pane.heightProperty().divide(1.5));
        pane.getChildren().add(imageView);
    }
    private void drawPiece(StackPane pane, PieceColor color, PieceType type) {
        if (type == PieceType.CHECKER) {
           drawChecker( pane,  color);
        } else if (type == PieceType.KING) {
           drawKing( pane,  color);
        }
    }
    public void setImportMenuItemEvent(EventHandler<ActionEvent> event) {
        importMenuItem.setOnAction(event);
    }
    public void setExportMenuItem(EventHandler<ActionEvent> event) {
        exportMenuItem.setOnAction(event);
    }
    public void setResetMenuItem(EventHandler<ActionEvent> event) {
        resetMenuItem.setOnAction(event);
    }
    public void setRestartMenuItem(EventHandler<ActionEvent> event) {
        restartMenuItem.setOnAction(event);
    }
    public void initField(final int width, final int height) {
        int count = 0;

        for (int y = 0; y< width; y++) {
            for (int x = 0; x < height; x++) {
                StackPane pane = new StackPane();
//                pane.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseHandle);
                pane.addEventFilter(MouseEvent.MOUSE_PRESSED,boardMouseHandler);
                pane.setAlignment(Pos.CENTER);
                String color = getBgColor(count);

                pane.setStyle("-fx-background-color: " + color + ";");
                count++;
                fieldGridPane.add(pane, y, x);
                pane.prefWidthProperty().bind(Bindings.min(fieldStackPane.widthProperty(), fieldStackPane.heightProperty()).divide(width));
                pane.prefHeightProperty().bind(Bindings.min(fieldStackPane.widthProperty(), fieldStackPane.heightProperty()).divide(height));
            }
            count++;
        }
    }

    public GridPane getFieldGridPane() {
        return fieldGridPane;
    }

    public void setScoreLabel(String score) {
        scoreLabel.setText(score);
    }
    public void setIsTurnLabel(String score) {
        isTurnLabel.setText(score);
    }
}
