package com.example.checkers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainScreenController controller = new MainScreenController(stage);
    }
}
// алгоритм хода

// сходить p1 на p2
// p1 и p2 корректны (p1 шашка или дамка, p2 пустая клетка)
// нужно продолжать бить оставшиеся фигуры
    // p1 это фигура которая которая била в прошлый раз
        // сходить p1 на p2 это бить
            // переместить p1 на p2, удалить битую фигуру
                // если фигура стала дамкой
                    // сделать фигуру дамкой
                // если можно еще бить
                    // установить флаг нужно продолжать бить, запомнить фигуру которая должна бить
        // сходить p1 на p2 это простое перемещение
            // break
// шашки текущего цвета могут бить
    // сходить p1 на p2 это бить
        // переместить p1 на p2, удалить битую фигуру
            // если фигура стала дамкой
                // сделать фигуру дамкой
            // если можно еще бить
                // установить флаг нужно продолжать бить, запомнить фигуру которая должна бить
// шашки текущего цвета не могут бить
    // переместить p1 на p2
        // если фигура стала дамкой
            // сделать фигуру дамкой