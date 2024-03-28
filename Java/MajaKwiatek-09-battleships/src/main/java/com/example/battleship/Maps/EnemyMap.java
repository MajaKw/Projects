package com.example.battleship.Maps;

import com.example.battleship.Construction.BattleshipFactory;
import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Construction.Field;
import com.example.battleship.Game.Player;
import com.example.battleship.Game.PlayerMenager;
import com.sun.source.tree.ReturnTree;
import org.bouncycastle.crypto.engines.CAST5Engine;
import org.springframework.boot.autoconfigure.web.servlet.JspTemplateAvailabilityProvider;
import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Game.PlayerMenager.getCoordinates;
import static com.example.battleship.Game.PlayerMenager.myShot;
import static com.example.battleship.Maps.Board.SIZE;
import static com.example.battleship.Maps.MapMenager.ROOT_PATH;

public class EnemyMap extends MapMenager {
    public EnemyMap() {
        board = new Board();
        for (int i = 0; i < SIZE; ++i)
            for (int j = 0; j < SIZE; ++j)
                board.setField(new Coordinates(i, j), NIEZNANY);
    }

    public EnemyMap(String fileName) {
        board = new Board();
        board.fileToBoardParser(fileName);
    }

    public void uncover(String shot) {
        Coordinates coords = new Coordinates(shot);
        List<Coordinates> adjacentFields = board.getAdjacentFields();
        adjacentFields.add(new Coordinates(0, 0));
        Queue<Coordinates> shipQue = new LinkedList<>();
        Stack<Coordinates> visited = new Stack<>();
        shipQue.add(coords);

        while (!shipQue.isEmpty()) {
            coords = shipQue.poll();
            for (Coordinates adj : adjacentFields) {
                Coordinates curr = new Coordinates(adj.row() + coords.row(), adj.col() + coords.col());
                if (Board.inRange(curr) && board.getField(curr) != VISITED_SHIP && board.getField(curr) != VISITED_WATER) {
                    Field field = board.getField(curr);
                    visited.push(curr);
                    switch (field) {
                        case SHIP -> {
                            shipQue.add(curr);
                            board.setField(curr, VISITED_SHIP);
                            markNeighbourFieldsAsWater(curr, visited);
                        }
                        case NIEZNANY, WATER -> board.setField(curr, VISITED_WATER);

                    }
                }
            }
        }
        rollBackVisited(visited);
    }

    public void rollBackVisited(Stack<Coordinates> visited) {
        while (!visited.isEmpty()) {
            Coordinates top = visited.pop();
            Field field = board.getField(top);
            switch (field) {
                case VISITED_SHIP -> {
                    board.setField(top, SHIP);
                }
                case VISITED_WATER -> board.setField(top, WATER);
            }
        }
    }

    public void markNeighbourFieldsAsWater(Coordinates coords, Stack<Coordinates> visited) {
        List<Coordinates> neighbourFields = board.getNeighbourFields();
        for (Coordinates neighbour : neighbourFields) {
            Coordinates curr = new Coordinates(neighbour.row() + coords.row(), neighbour.col() + coords.col());
            if (Board.inRange(curr) && board.getField(curr) == NIEZNANY) {
                board.setField(curr, VISITED_WATER);
                visited.push(curr);
            }
        }
    }

    @Override
    public void update(String gotMessage) {
        Field fieldRsponse = PlayerMenager.getField(gotMessage);
        switch (fieldRsponse) {
            case PUDŁO -> board.setField(new Coordinates(myShot), WATER);
            case TRAFIONY_ZATOPIONY, OSTATNI_ZATOPIONY -> {
                board.setField(new Coordinates(myShot), SHIP);
                uncover(myShot);
            }
            case TRAFIONY -> board.setField(new Coordinates(myShot), SHIP);

        }
    }

}
