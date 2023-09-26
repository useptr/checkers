package com.example.checkers;

import com.example.checkers.enums.PieceColor;
import com.example.checkers.enums.PieceType;
import com.example.checkers.models.Board;
import com.example.checkers.models.Piece;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class MainScreen {
    private double width=0;
    private double height=0;
    private Stage stage = null;
    private Scene scene = null;
    private VBox rootVBox = new VBox();
    private MenuBar menuBar = new MenuBar();
    private Menu gameMenu = new Menu("Игра");
    private MenuItem resetMenuItem = new MenuItem("Сбросить");
   private StackPane fieldStackPane = new StackPane();
    private GridPane fieldGridPane = new GridPane();
    private final Image crownImage = new Image(getClass().getResource("/assets/crown.png").toString(), 50, 50, false, false);
    public MainScreen(Stage stage, double width, double height) {
        this.width = width;
        this.height = height;
        this.stage = stage;

        gameMenu.getItems().add(resetMenuItem);
        menuBar.getMenus().add(gameMenu);
        rootVBox.getChildren().add(menuBar);

        fieldGridPane.setAlignment(Pos.CENTER);
        fieldStackPane.getChildren().add(fieldGridPane);
        rootVBox.getChildren().add(fieldStackPane);
        fieldStackPane.prefHeightProperty().bind(rootVBox.heightProperty().subtract(menuBar.heightProperty()));

        scene = new Scene(rootVBox, 600, 500);
        this.stage.setTitle("Checkers");
        this.stage.setScene(scene);
        this.stage.show();
    }
    public void drawPieces(Board board) {
        Piece[][] field = board.getField();
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
        pane.getChildren().add(imageView);
    }
    private void drawPiece(StackPane pane, PieceColor color, PieceType type) {
        if (type == PieceType.CHECKER) {
           drawChecker( pane,  color);
        } else if (type == PieceType.KING) {
           drawKing( pane,  color);
        }
    }
    public void initField(final int width, final int height, EventHandler<MouseEvent> mouseHandle) {
        int count = 0;
        for (int y = 0; y< width; y++) {
            for (int x = 0; x < height; x++) {
                StackPane pane = new StackPane();
                pane.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseHandle);
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
}
