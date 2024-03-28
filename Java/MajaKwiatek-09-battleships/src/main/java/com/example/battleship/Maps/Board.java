package com.example.battleship.Maps;

import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Construction.Field;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.battleship.Construction.Field.*;

public class Board {
    public static final int SIZE = 10;
    private Field[][] gameBoard;
    @Getter(AccessLevel.PUBLIC)
    private final List<Coordinates> adjacentFields;
    @Getter(AccessLevel.PUBLIC)
    private final List<Coordinates> neighbourFields;

    @Getter(AccessLevel.PUBLIC)
    private int hitAndSunkCounter = 0;
    public static final int shipAmount = 10;

    public void incrementHitAndSunkCounter() {
        ++hitAndSunkCounter;
    }

    public Board() {
        adjacentFields = new ArrayList<>();
        neighbourFields = new ArrayList<>();
        this.gameBoard = new Field[SIZE][SIZE];
        adjacentFields.add(new Coordinates(-1, 0));
        adjacentFields.add(new Coordinates(0, -1));
        adjacentFields.add(new Coordinates(0, 0));
        adjacentFields.add(new Coordinates(0, 1));
        adjacentFields.add(new Coordinates(1, 0));

        neighbourFields.add(new Coordinates(-1, -1));
        neighbourFields.add(new Coordinates(-1, 1));
        neighbourFields.add(new Coordinates(1, -1));
        neighbourFields.add(new Coordinates(1, 1));
    }

    public Board(Board gameBoard) {
        this();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j)
                this.gameBoard[i][j] = gameBoard.getField(new Coordinates(i, j));
        }
    }

    public void fileToBoardParser(String fileName) {
        Path path = Path.of(fileName);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.println("path: " + path);
            System.out.println("Error: can't read line from file " + path + " in fileTogameBoardParser " + this.getClass().getSimpleName());
            return;
        }
        ListIterator<String> iterator = lines.listIterator();
        for (int i = 0; i < lines.size(); ++i) {
            String line = iterator.next();
            for (int j = 0; j < line.length(); ++j) {
                char sign = line.charAt(j);
                switch (sign) {
                    case '#' -> setField(new Coordinates(i, j), SHIP);
                    case '@' -> setField(new Coordinates(i, j), TRAFIONY);
                    case '.' -> setField(new Coordinates(i, j), WATER);
                    case '~' -> setField(new Coordinates(i, j), PUDŁO);
                    case '?' -> setField(new Coordinates(i, j), NIEZNANY);
                }
            }
        }
    }

    public void show() {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                System.out.print(gameBoard[i][j].getSign());
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean inRange(Coordinates curr) {
        return curr.col() < 10 && curr.col() > -1 && curr.row() < 10 && curr.row() > -1;
    }

    public void clearBoard(Field field) {
        for (int i = 0; i < SIZE; ++i)
            for (int j = 0; j < SIZE; ++j)
                setField(new Coordinates(i, j), field);
    }

    public void setField(Coordinates curr, Field field) {
        gameBoard[curr.row()][curr.col()] = field;
    }

    public Field getField(Coordinates curr) {
        return gameBoard[curr.row()][curr.col()];
    }

    public Field getField(int i, int j) {
        return gameBoard[i][j];
    }


    public Field getField(String coords) {
        return gameBoard[coords.charAt(0) - 'A'][Integer.parseInt(coords.substring(1)) - 1];
    }

    public static void main(String[] args) {
    }

}
