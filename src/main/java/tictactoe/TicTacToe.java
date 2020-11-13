package tictactoe;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * Класс отвечающий за игровой процесс
 * @author Kirill Asmanov
 * @since 26.04.2020
 */
public class TicTacToe extends Application {
    /**
     * Заголовок главного окна
     */
    private static final String TITLE = "Крестики-нолики";
    /**
     * Размер игрового поля (5х5)
     */
    private final int size = 3;
    /**
     * Игровое поле
     */
    private final Figure3T[][] cells = new Figure3T[size][size];
    /**
     * Класс, отвечающий за логику победы
     */
    private final Logic3T logic = new Logic3T(cells);
    /**
     * Маркер номера хода текущего игрока
     */
    private int turnOrder = 0;

    /**
     * Выводит сообщение в всплывающем окне
     * @param message - текст сообщения
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(TITLE);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Вызывает проверку на заполненность всех клеток игрового поля
     * @return - true - заполнено все поле
     */
    private boolean checkState() {
        boolean gap = this.logic.hasGap();
        if (!gap) {
            this.showAlert("Все поля запонены! Начните новую Игру!");
        }
        return gap;
    }

    /**
     * Устанавливает счетчик текущего хода на нулевое значение
     */
    private void setTurnZero() {
        this.turnOrder = 0;
    }

    /**
     * Вызывает события по отрисовке символа и проверки условия победы
     * после нажатия игрока на клетку игрового поля
     */
    private EventHandler<MouseEvent> buildMouseEvent(Group panel) {
        return event -> {
            Figure3T rect = (Figure3T) event.getTarget();
            if (this.checkState()) {
                if (turnOrder % 2 == 0) {
                    rect.take(true);
                    panel.getChildren().add(
                            this.buildMarkX(rect.getX(), rect.getY(), 50)
                    );
                    if (this.logic.isWinner(Figure3T::hasMarkX)) {
                        this.showAlert("Победили Крестики! Начните новую Игру!");
                        setTurnZero();
                    } else {
                        this.checkState();
                    }
                } else {
                    rect.take(false);
                    panel.getChildren().add(
                            this.buildMarkO(rect.getX(), rect.getY(), 50)
                    );
                    if (this.logic.isWinner(Figure3T::hasMarkO)) {
                        this.showAlert("Победили Нолики! Начните новую Игру!");
                        setTurnZero();
                    } else {
                        this.checkState();
                    }
                }
                turnOrder++;
            }
        };
    }

// ==== Инициализация игрового поля ====

    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();
        HBox control = new HBox();
        control.setPrefHeight(40);
        control.setSpacing(10.0);
        control.setAlignment(Pos.BASELINE_CENTER);
        Button start = new Button("Начать");
        start.setOnMouseClicked(
                event -> border.setCenter(this.buildGrid())
        );
        control.getChildren().addAll(start);
        border.setBottom(control);
        border.setCenter(this.buildGrid());
        stage.setScene(new Scene(border, 300, 300));
        stage.setTitle(TITLE);
        stage.setResizable(true);
        stage.setMinHeight(300);
        stage.setMinWidth(300);
        stage.show();
    }

    /**
     * Строит сетку из клеток
     * @return группа клеток
     */
    private Group buildGrid() {
        Group panel = new Group();
        for (int y = 0; y != this.size; y++) {
            for (int x = 0; x != this.size; x++) {
                Figure3T rect = this.buildRectangle(x, y, 50);
                this.cells[y][x] = rect;
                panel.getChildren().add(rect);
                rect.setOnMouseClicked(this.buildMouseEvent(panel));
            }
        }
        return panel;
    }

    /**
     * Строит клетку игрового поля в заданных координатах с заданным размером
     * @param x - координата Х
     * @param y - координата Y
     * @param size - размер клетки
     * @return клетка
     */
    private Figure3T buildRectangle(int x, int y, int size) {
        Figure3T rect = new Figure3T();
        rect.setX(x * size);
        rect.setY(y * size);
        rect.setHeight(size);
        rect.setWidth(size);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        return rect;
    }

    /**
     * Строит символ "нолика" в заданных координатах с заданным размером
     * @param x - координата Х
     * @param y - координата Y
     * @param size - размер символа
     */
    private Group buildMarkO(double x, double y, int size) {
        Group group = new Group();
        int radius = size / 2;
        Circle circle = new Circle(x + radius, y + radius, radius - 10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        group.getChildren().add(circle);
        return group;
    }

    /**
     * Строит символ "крестика" в заданных координатах с заданным размером
     * @param x - координата Х
     * @param y - координата Y
     * @param size - размер символа
     */
    private Group buildMarkX(double x, double y, int size) {
        Group group = new Group();
        group.getChildren().addAll(
                new Line(
                        x + 10, y  + 10,
                        x + size - 10, y + size - 10
                ),
                new Line(
                        x + size - 10, y + 10,
                        x + 10, y + size - 10
                )
        );
        return group;
    }
}
