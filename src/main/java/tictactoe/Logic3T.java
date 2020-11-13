package tictactoe;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Класс логики расчета условия победы при игре
 * @author Kirill Asmanov
 * @since 26.04.2020
 */
public class Logic3T {
    /**
     * Хранит таблицу игрового поля
     */
    private final Figure3T[][] table;

    public Logic3T(Figure3T[][] table) {
        this.table = table;
    }

    /**
     * Проверяет заполненность одинаковым символом столбца/строки/диагонали на игровом поле, начиная
     * с заданной ячейки и двигаясь по направлению, характеризуемому [deltaX, deltaY]
     * @param predicate определяет проверяемый символ
     * @param startX координата x начальной ячейки
     * @param startY координата y начальной ячейки
     * @param deltaX задает направление движения от начальной ячейки по оси х
     * @param deltaY задает направление движения от начальной ячейки по оси у
     * @return true/false если заполнен или нет соответственно
     */
    public boolean fillBy(Predicate<Figure3T> predicate, int startX, int startY, int deltaX, int deltaY) {
        boolean result = true;
        for (int index = 0; index != this.table.length; index++) {
            Figure3T cell = this.table[startX][startY];
            startX += deltaX;
            startY += deltaY;
            if (!predicate.test(cell)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Проверяет условие победы игрока, в зависимости от того, чей сейчас ход
     * @param symbolXorY маркер символа того, чей был совершен ход
     * @return true/false если победил или нет соответственно
     */

    public boolean isWinner(Predicate<Figure3T> symbolXorY) {
        boolean isWin = false;
        for (int diagonalIndex = 0; diagonalIndex < table.length; diagonalIndex++) {
            if (diagonalIndex == 0) {
                isWin = this.fillBy(symbolXorY, 0, 0, 1, 1) ||
                        this.fillBy(symbolXorY, this.table.length - 1 , 0, -1, 1);
                if (isWin) { break; }
            }
            isWin = this.fillBy(symbolXorY, diagonalIndex, 0, 0, 1) ||
                    this.fillBy(symbolXorY, 0, diagonalIndex, 1, 0);
            if (isWin) { break; }
        }
        return isWin;
    }

    /**
     * Проверяет заполненность игрового поля
     * @return true - не заполнено, false - заполнено
     */
    public boolean hasGap() {
        return Arrays.stream(table).flatMap(Arrays::stream).anyMatch(e -> !e.hasMarkO() && !e.hasMarkX());
    }
}
