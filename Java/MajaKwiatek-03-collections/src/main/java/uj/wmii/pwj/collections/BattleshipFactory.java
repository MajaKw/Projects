package uj.wmii.pwj.collections;


import java.util.*;

import static uj.wmii.pwj.collections.BattleshipFactory.Field.*;


public class BattleshipFactory implements BattleshipGenerator {

    record Coordinates(int row, int col) {
    }

    enum Field {
        SHIP, WATER, VISITED
    }

    static final int SIZE = 10;
    private Field board[][];

    private List<Coordinates> possibleMoves;
    private final List<Coordinates> adjacentFields;
    private final List<Coordinates> neighbourFields;

    private List<Coordinates> visited;

    BattleshipFactory() {
        possibleMoves = new ArrayList<>();
        adjacentFields = new ArrayList<>();
        neighbourFields = new ArrayList<>();
        visited = new ArrayList<>();

        board = new Field[SIZE][];
        for (int i = 0; i < SIZE; ++i) {
            board[i] = new Field[SIZE];
            for (int j = 0; j < SIZE; ++j) {
                board[i][j] = WATER;
            }
        }

        adjacentFields.add(new Coordinates(-1, 0));
        adjacentFields.add(new Coordinates(0, -1));
        adjacentFields.add(new Coordinates(0, 0));
        adjacentFields.add(new Coordinates(0, 1));
        adjacentFields.add(new Coordinates(1, 0));

        neighbourFields.add(new Coordinates(-1, -1));
        neighbourFields.add(new Coordinates(-1, 1));
        neighbourFields.add(new Coordinates(1, -1));
        neighbourFields.add(new Coordinates(1, 1));
        neighbourFields.addAll(adjacentFields);

        putAllShips();
    }

    void setField(Coordinates curr, Field val) {
        board[curr.row][curr.col] = val;
    }

    Field getField(Coordinates curr) {
        return board[curr.row][curr.col];
    }

    void clearBoard(){
        for (int i = 0; i < SIZE; ++i)
            for (int j = 0; j < SIZE; ++j)
                board[i][j] = WATER;
    }

    boolean inRange(Coordinates curr) {
        return curr.col < 10 && curr.col > -1 && curr.row < 10 && curr.row > -1;
    }

    void markVisitedAs(Field field) {
        for (Coordinates crd : visited)
            setField(crd, field);
        visited.clear();
    }

    boolean putSingleMast(Coordinates curr) {
        if (getField(curr) == VISITED) return false;

        for (Coordinates crd : neighbourFields) {
            Coordinates temp = new Coordinates(curr.row + crd.row, curr.col + crd.col);
            if (inRange(temp) && getField(temp) == SHIP) return false;
        }
        return true;
    }

    boolean putOtherMasts(Coordinates curr, int masts) {
        if (masts == 0) return true;
        for (Coordinates crd : adjacentFields) {
            Coordinates temp = new Coordinates(curr.row + crd.row, curr.col + crd.col);
            if (inRange(temp) && getField(temp) == WATER) possibleMoves.add(temp);
        }
        while (!possibleMoves.isEmpty()) {
            int randomIdx = new Random().nextInt(possibleMoves.size());
            Coordinates randomCoords = possibleMoves.get(randomIdx);
            possibleMoves.remove(randomIdx);
            if (putSingleMast(randomCoords)) {
                curr = randomCoords;
                setField(curr, VISITED);
                visited.add(curr);
                return putOtherMasts(curr, --masts);
            }
        }
        return false;
    }



    void putAllShips() {
        clearBoard();
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
                setField(curr, VISITED);
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
                if (board[i][j] == SHIP) out.append("#");
                else out.append(".");
                // enum ze znakiem
            }
        }
        return out.toString();
    }
}
