package com.example.battleship.Maps;

import com.example.battleship.Construction.BattleshipFactory;
import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Construction.Field;
import com.example.battleship.Game.Player;
import com.example.battleship.Game.PlayerMenager;
import com.example.battleship.Game.Server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Game.Player.SERVER;
import static com.example.battleship.Maps.MapMenager.ROOT_PATH;


public class MyMap extends MapMenager {

    public MyMap() {
        board = new Board();
    }

    public MyMap(String fileName) {
        this();
        board.fileToBoardParser(fileName);
    }

    @Override
    public void update(String gotMessage) {
        Coordinates enemyShotCoords = PlayerMenager.getCoordinates(gotMessage);
        Field fieldResponse = getShotResponse(enemyShotCoords);
        board.setField(enemyShotCoords, fieldResponse);
    }

    public Field getShotResponse(Coordinates coords) {
        Field field = board.getField(coords);
        switch (field) {
            case TRAFIONY -> {
                return TRAFIONY;
            }
            case SHIP -> {
                if (isHitAndSunk(coords)) {
                    board.incrementHitAndSunkCounter();
                    if (board.getHitAndSunkCounter() == Board.shipAmount) return OSTATNI_ZATOPIONY;
                    return TRAFIONY_ZATOPIONY;
                }
                return TRAFIONY;
            }
            case TRAFIONY_ZATOPIONY -> {
                return TRAFIONY_ZATOPIONY;
            }
            default -> {
                return PUDŁO;
            }
        }
    }

    public boolean isHitAndSunk(Coordinates firstCoords) {
        Queue<Coordinates> que = new LinkedList<>();
        Stack<Coordinates> visitedShip = new Stack<>();
        que.add(firstCoords);
        visitedShip.push(firstCoords);
        board.setField(firstCoords, TRAFIONY_ZATOPIONY);
        List<Coordinates> adjacentFields = board.getAdjacentFields();

        while (!que.isEmpty()) {
            Coordinates top = que.poll();
            for (Coordinates crd : adjacentFields) {
                Coordinates curr = new Coordinates(crd.row() + top.row(), crd.col() + top.col());
                if (Board.inRange(curr)) {
                    Field field = board.getField(curr);
                    switch (field) {
                        case SHIP -> {
                            board.setField(firstCoords, TRAFIONY);
                            while (!visitedShip.isEmpty())
                                board.setField(visitedShip.pop(), TRAFIONY);
                            return false;
                        }
                        case TRAFIONY -> {
                            board.setField(curr, TRAFIONY_ZATOPIONY);
                            visitedShip.push(curr);
                            que.add(curr);
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
    }
}
