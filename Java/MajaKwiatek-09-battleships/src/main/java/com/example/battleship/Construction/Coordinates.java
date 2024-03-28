package com.example.battleship.Construction;

import com.example.battleship.Maps.Board;

public class Coordinates {
    private int col;
    private int row;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(String coords) {
        row = coords.charAt(0) - 'A';
        col = Integer.parseInt(coords.substring(1).trim()) - 1;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }


    @Override
    public String toString() {
        return Character.toString(row + 'A') + Integer.toString(col + 1);
    }


}

