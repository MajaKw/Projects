package com.example.battleship.Construction;


import com.example.battleship.Maps.Board;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.scheduling.annotation.AnnotationAsyncExecutionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Maps.Board.SIZE;

public class BattleshipFactory implements BattleshipGenerator {

    @Getter(AccessLevel.PUBLIC)
    private Board board;
    private List<Coordinates> possibleMoves;
    private List<Coordinates> visited;

    public BattleshipFactory() {
        possibleMoves = new ArrayList<>();
        visited = new ArrayList<>();
        board = new Board();
        putAllShips();
    }

    void markVisitedAs(Field field) {
        for (Coordinates crd : visited)
            board.setField(crd, field);
        visited.clear();
    }

    boolean putSingleMast(Coordinates curr) {
        if (board.getField(curr) == VISITED) return false;
        List<Coordinates> neighbourFields = board.getNeighbourFields();
        neighbourFields.addAll(board.getAdjacentFields());
        for (Coordinates crd : neighbourFields) {
            Coordinates temp = new Coordinates(curr.row() + crd.row(), curr.col() + crd.col());
            if (Board.inRange(temp) && board.getField(temp) == SHIP) return false;
        }
        return true;
    }

    boolean putOtherMasts(Coordinates curr, int masts) {
        if (masts == 0) return true;
        List<Coordinates> adjacentFields = board.getAdjacentFields();
        for (Coordinates crd : adjacentFields) {
            Coordinates temp = new Coordinates(curr.row() + crd.row(), curr.col() + crd.col());
            if (Board.inRange(temp) && board.getField(temp) == WATER) possibleMoves.add(temp);
        }
        while (!possibleMoves.isEmpty()) {
            int randomIdx = new Random().nextInt(possibleMoves.size());
            Coordinates randomCoords = possibleMoves.get(randomIdx);
            possibleMoves.remove(randomIdx);
            if (putSingleMast(randomCoords)) {
                curr = randomCoords;
                board.setField(curr, VISITED);
                visited.add(curr);
                return putOtherMasts(curr, --masts);
            }
        }
        return false;
    }

    void putAllShips() {
        board.clearBoard(WATER);
        for (int masts = 4, amount = 1; masts > 0; --masts, ++amount) {
            for (int i = 0; i < amount; ++i) {
                boolean succeeded = putSingleShip(masts);
                if (!succeeded) putAllShips();
            }
        }
    }

    boolean putSingleShip(int masts) {
        List<Coordinates> listOfAllCoordinates = new ArrayList<>();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j)
                listOfAllCoordinates.add(new Coordinates(i, j));
        }
        while (!listOfAllCoordinates.isEmpty()) {
            int randomIdx = new Random().nextInt(listOfAllCoordinates.size());
            Coordinates curr = listOfAllCoordinates.get(randomIdx);
            listOfAllCoordinates.remove(randomIdx);

            if (putSingleMast(curr)) {
                board.setField(curr, VISITED);
                visited.add(curr);
                if (putOtherMasts(curr, masts - 1)) {
                    markVisitedAs(SHIP);
                    return true;
                } else markVisitedAs(WATER);
            }
        }
        return false;
    }

    @Override
    public String generateMap() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                if (board.getField(new Coordinates(i, j)) == SHIP) out.append(SHIP.getSign());
                else out.append(WATER.getSign());
            }
            out.append("\n");
        }
        return out.toString();
    }

    public static void main(String[] args) {
    }

}
