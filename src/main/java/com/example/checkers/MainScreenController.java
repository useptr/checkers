package com.example.checkers;

import com.example.checkers.enums.PieceType;
import com.example.checkers.models.Checkers;
import com.example.checkers.models.Position;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainScreenController {
    private Checkers model = new Checkers();
    private MainScreen view;
    Position prev=null;
    Position next=null;
    public MainScreenController(Stage stage) {
        view = new MainScreen(stage, 600, 500);
        view.initField(model.getBoard().getWidth(), model.getBoard().getHeight(), this::pieceMousePressed);
        view.drawPieces(model.getBoard());
    }
    public StackPane getStackPane(Node node) {
        StackPane stackPane = null;
        if (node instanceof StackPane) {
            stackPane = (StackPane)node;
        }
        else if (node instanceof Circle && node.getParent() instanceof StackPane) {
            stackPane = (StackPane)node.getParent();
        }
        return stackPane;
    }
    public void printPos(Position pos, int num) { // log
        if (pos == null)
            return;
        System.out.println("x"+ num + " " + pos.x + " y" + num + " " + pos.y); // log
    }
    public boolean paneContainChecker(StackPane pane) {
        boolean paneContainChecker = false;
        for (Node node: pane.getChildren()) {
            if (node instanceof Circle) {
                paneContainChecker = true;
                break;
            }
        }
        return paneContainChecker;
    }
    public StackPane getStackPaneFromNode(Node node) {
        StackPane stackPane = null;
        if (node instanceof StackPane) {
            stackPane = (StackPane)node;
        } else if (node instanceof Circle && node.getParent() instanceof StackPane) {
            stackPane = (StackPane)node.getParent();
        }
        return stackPane;
    }
    public Position getPosition(StackPane stackPane) {
        Position pos = null;
        if (stackPane != null) {
            GridPane fieldGridPane = view.getFieldGridPane();
            int y = fieldGridPane.getRowIndex(stackPane);
            int x = fieldGridPane.getColumnIndex(stackPane);
            pos = new Position(x, y);
        }
        return pos;
    }
    public PieceType getType(Node node) {
        PieceType type = PieceType.NONE;
        if (node instanceof StackPane) {
            StackPane stackPane = (StackPane)node;
             if (paneContainChecker(stackPane))
                 type = PieceType.CHECKER;
        } else if (node instanceof Circle) {
            type = PieceType.CHECKER;
        }
        return type;
    }
    public void pieceMousePressed(MouseEvent mouseEvent) {
        Node node = mouseEvent.getPickResult().getIntersectedNode();
        PieceType type = getType(node);
        StackPane stackPane = getStackPaneFromNode(node);
        Position pos = getPosition(stackPane);
        if (pos != null) {
            if (type == PieceType.CHECKER) {
                prev = pos;
                next = null;
                System.out.println("clicked cell with checker"); // log
                printPos(prev, 1); // log
            }
            else if (type == PieceType.NONE) {
                next = pos;
                System.out.println("clicked empty cell"); // log
                printPos(next, 2); // log
            }
        }
            if (prev != null && next != null) {
                boolean pieceMoved = model.movePiece(prev, next);
                prev = next = null;
            }
//                if (pieceMoved) {
                    view.drawPieces(model.getBoard());
//                }
            }
}