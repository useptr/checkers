package com.example.checkers;

import com.example.checkers.enums.PieceType;
import com.example.checkers.models.Checkers;
import com.example.checkers.models.Position;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class MainScreenController {
    private Checkers model = new Checkers(); // Модель
    private MainScreen view; // Представление
    Position prev=null; // Текущая координата
    Position next=null; // Следующая координата
    public void exportMenuItemSelected(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Экспортировать доску");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter( "BOARD files (*.board)","*.board");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(view.getStage());//Указываем текущую сцену
        if (file != null) {
            try{
                FileOutputStream fout=new FileOutputStream(file.getPath());
                ObjectOutputStream out=new ObjectOutputStream(fout);
                out.writeObject(model.getBoardDTO());
                out.flush();
                out.close();
            }catch(Exception e){System.out.println(e);}
        }
    } // Обработка события "Экспорт поля"
    public void resetMenuItemSelected(ActionEvent event) {
        model.reset();
        updateView();
    }// Обработка события "Сброс поля"
    public void restartMenuItemSelected(ActionEvent event) {
        model.restart();
        updateView();
    }// Обработка события "Перезапуск игры"
    public void importMenuItemSelected(ActionEvent event){
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Импортировать доску");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter( "BOARD files (*.board)","*.board");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(view.getStage());//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            try{
                ObjectInputStream in=new ObjectInputStream(new FileInputStream(file.getPath()));
                model.setBoardFromString(in);
                in.close();
            }catch(Exception e){System.out.println(e);}
        }
        updateView();
//        view.drawPieces(model.getBoard());
    }// Обработка события "Импорт поля"
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
    public MainScreenController(Stage stage) {
        view = new MainScreen(stage, 600, 700);
        view.setBoardMouseHandler(this::pieceMousePressed);
        view.initField(model.getBoard().getWidth(), model.getBoard().getHeight());
        view.setImportMenuItemEvent(this::importMenuItemSelected);
        view.setExportMenuItem(this::exportMenuItemSelected);
        view.setResetMenuItem(this::resetMenuItemSelected);
        view.setRestartMenuItem(this::restartMenuItemSelected);
//        view.drawPieces(model.getBoard());
        updateView();
    }
    public void setScore() {
        view.setScoreLabel(model.getScore());
    }
    public void setTurn() {
        view.setIsTurnLabel(model.getTurn());
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
        }else if (node instanceof ImageView && node.getParent() instanceof StackPane) {
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
        else if (node instanceof ImageView) {
            type = PieceType.CHECKER;
        }
        return type;
    }
    public void updateView() {
//        model.checkEndGame();
        setTurn();
        setScore();
        view.drawPieces(model.getBoard());
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
        updateView();
//                }
            }
}