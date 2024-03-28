package com.example.battleship;

import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Maps.Board;
import com.example.battleship.Maps.EnemyMap;
import com.example.battleship.Maps.MapMenager;
import com.example.battleship.Maps.MyMap;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Game.Player.SERVER;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

@SpringBootTest
public class MapTest {

    MyMap myMap;
    EnemyMap enemyMap;

    final static String ROOT = "src/test/java/resources/";

    final static String ENEMY_MAP__FILE = "enemyMap.txt";
    final static String MY_MAP_FILE = "myMap.txt";

    MapTest() {
        myMap = new MyMap(ROOT + MY_MAP_FILE);
        enemyMap = new EnemyMap(ROOT + ENEMY_MAP__FILE);
    }


    @Test
    public void isHitAndSunk() {
        assertTrue(myMap.isHitAndSunk(new Coordinates("C7")));
        assertTrue(myMap.isHitAndSunk(new Coordinates("A6")));
        assertTrue(myMap.isHitAndSunk(new Coordinates("F6")));
        assertFalse(myMap.isHitAndSunk(new Coordinates("C3")));
        assertFalse(myMap.isHitAndSunk(new Coordinates("I6")));
        assertFalse(myMap.isHitAndSunk(new Coordinates("G10")));
    }
    @Test
    public void allFieldsMarkedAsHitAndSunkWhenHitAndSunkTrue(){
        assertTrue(myMap.isHitAndSunk(new Coordinates("C7")));

        assertEquals(TRAFIONY_ZATOPIONY, myMap.getBoard().getField(new Coordinates("C6")));
        assertEquals(TRAFIONY_ZATOPIONY, myMap.getBoard().getField(new Coordinates("C8")));
        assertEquals(TRAFIONY_ZATOPIONY, myMap.getBoard().getField(new Coordinates("C7")));
        assertEquals(TRAFIONY_ZATOPIONY, myMap.getBoard().getField(new Coordinates("D7")));
    }
    @Test
    public void fieldsDidNotChangeWhenHitAndSunkFalse(){
        assertFalse(myMap.isHitAndSunk(new Coordinates("G10")));

        assertEquals(SHIP, myMap.getBoard().getField(new Coordinates("F8")));
        assertEquals(TRAFIONY, myMap.getBoard().getField(new Coordinates("F9")));
        assertEquals(TRAFIONY, myMap.getBoard().getField(new Coordinates("G9")));
        assertEquals(TRAFIONY, myMap.getBoard().getField(new Coordinates("G10")));
    }

    @Test
    public void getBasicShotResponseMyMap() throws IOException {
        assertEquals(PUDŁO, myMap.getShotResponse(new Coordinates("B2")));
        assertEquals(PUDŁO, myMap.getShotResponse(new Coordinates("F2")));
        assertEquals(TRAFIONY, myMap.getShotResponse(new Coordinates("A3")));
    }

    @Test
    public void incrementHitAndSunk() {
        int prevCounter = myMap.getBoard().getHitAndSunkCounter();
        myMap.getShotResponse(new Coordinates("C7"));
        assertEquals(prevCounter + 1, myMap.getBoard().getHitAndSunkCounter());
    }

    @Test
    public void updateBasicMyMap(){
        String coords_A3 =  "A3";
        assertEquals(SHIP,myMap.getBoard().getField(new Coordinates(coords_A3)));
        myMap.update(coords_A3);
        assertEquals(TRAFIONY,myMap.getBoard().getField(new Coordinates(coords_A3)));

        String coords_B7 =  "B7";
        assertEquals(PUDŁO,myMap.getBoard().getField(new Coordinates(coords_B7)));
        myMap.update(coords_B7);
        assertEquals(PUDŁO,myMap.getBoard().getField(new Coordinates(coords_B7)));

        String coords_A1 =  "A1";
        assertEquals(WATER,myMap.getBoard().getField(new Coordinates(coords_A1)));
        myMap.update(coords_A1);
        assertEquals(PUDŁO,myMap.getBoard().getField(new Coordinates(coords_A1)));

        String coords_F8 =  "F8";
        assertEquals(SHIP,myMap.getBoard().getField(new Coordinates(coords_F8)));
        myMap.update(coords_F8);
        assertEquals(TRAFIONY_ZATOPIONY,myMap.getBoard().getField(new Coordinates(coords_F8)));

        myMap.update(coords_F8);
        assertEquals(TRAFIONY_ZATOPIONY,myMap.getBoard().getField(new Coordinates(coords_F8)));
    }

    @Test
    public void uncoverNearbyFieldsAfterSinkingShip(){
        enemyMap.uncover("A7");

        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("A7")));
        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("A6")));
        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("B7")));

        assertEquals(WATER,enemyMap.getBoard().getField(new Coordinates("A5")));
        assertEquals(WATER,enemyMap.getBoard().getField("B5"));
        assertEquals(WATER,enemyMap.getBoard().getField("B6"));
        assertEquals(WATER,enemyMap.getBoard().getField("B8"));
        assertEquals(WATER,enemyMap.getBoard().getField("C6"));
        assertEquals(WATER,enemyMap.getBoard().getField("C7"));
        assertEquals(WATER,enemyMap.getBoard().getField("C8"));
        assertEquals(WATER,enemyMap.getBoard().getField("A8"));

        enemyMap.uncover("D5");

        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("D5")));
        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("D6")));
        assertEquals(SHIP,enemyMap.getBoard().getField(new Coordinates("D7")));

        assertEquals(WATER,enemyMap.getBoard().getField("D4"));
        assertEquals(WATER,enemyMap.getBoard().getField("D8"));

        assertEquals(WATER,enemyMap.getBoard().getField("C4"));
        assertEquals(WATER,enemyMap.getBoard().getField("C5"));
        assertEquals(WATER,enemyMap.getBoard().getField("C6"));
        assertEquals(WATER,enemyMap.getBoard().getField("C7"));
        assertEquals(WATER,enemyMap.getBoard().getField("C8"));

        assertEquals(WATER,enemyMap.getBoard().getField("E4"));
        assertEquals(WATER,enemyMap.getBoard().getField("E5"));
        assertEquals(WATER,enemyMap.getBoard().getField("E6"));
        assertEquals(WATER,enemyMap.getBoard().getField("E7"));
        assertEquals(WATER,enemyMap.getBoard().getField("E8"));

    }

    boolean sameBoards(Board board_1, Board board_2){
        for(int i=0; i<Board.SIZE; ++i){
            for(int j=0; j<Board.SIZE; ++j)
                if(board_1.getField(i, j) != board_2.getField(i,j)) return false;
        }
        return true;
    }
    @Test
    public void uncoveredTest(){
        enemyMap.uncover("B7");
        enemyMap.uncover("C2");
        enemyMap.uncover("D6");
        enemyMap.uncover("F5");
        enemyMap.uncover("I3");
        enemyMap.uncover("I10");

        Board uncoveredBoard = new Board();
        uncoveredBoard.fileToBoardParser(ROOT + "uncovered.txt");
        assertTrue(sameBoards(uncoveredBoard, enemyMap.getBoard()));
    }

    @Test
    public void uncoveredOneShips(){
        enemyMap = new EnemyMap(ROOT + "one_ships.txt");
        enemyMap.uncover("A1");
        enemyMap.uncover("C1");
        enemyMap.uncover("B9");
        enemyMap.uncover("F4");
        enemyMap.uncover("F6");
        enemyMap.uncover("G9");
        enemyMap.uncover("H7");

        Board uncoveredBoard = new Board();
        uncoveredBoard.fileToBoardParser(ROOT + "one_ships_uncovered.txt");
        assertTrue(sameBoards(uncoveredBoard, enemyMap.getBoard()));
    }

}
