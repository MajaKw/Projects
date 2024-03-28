package com.example.battleship.Construction;

public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance(){
        return new BattleshipFactory();
    }

}
