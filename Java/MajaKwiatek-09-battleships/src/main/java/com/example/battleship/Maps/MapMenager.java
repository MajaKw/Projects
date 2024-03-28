package com.example.battleship.Maps;

import com.example.battleship.Construction.Coordinates;
import com.example.battleship.Construction.Field;
import com.example.battleship.Game.Player;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import static com.example.battleship.Construction.Field.*;
import static com.example.battleship.Construction.Field.PUDŁO;

public abstract class MapMenager {

    public static final String ROOT_PATH = "/home/maisho/java/MajaKwiatek-09-battleships/src/";

    @Getter(AccessLevel.PUBLIC)
    Board board;

    public abstract void update(String gotMessage);

}
